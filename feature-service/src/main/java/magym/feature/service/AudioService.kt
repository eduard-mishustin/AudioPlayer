package magym.feature.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat.*
import androidx.core.app.NotificationManagerCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import magym.core.common.extention.appName
import magym.core.common.extention.isOreoOrMore
import magym.core.data.data.entity.Audio
import magym.feature.service.util.IMediaSessionCallback
import magym.feature.service.util.MediaSessionCallback
import magym.feature.service.util.createAudioPlayerNotification
import org.koin.android.ext.android.get


class AudioService : MediaBrowserServiceCompat(), IMediaSessionCallback {
	
	// TODO: Снижать громкость, а не ставить на паузу
	// AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK
	// setWillPauseWhenDucked
	
	// TODO: Реализация ACTION_SKIP_TO_NEXT и ACTION_SKIP_TO_PREVIOUS
	
	// TODO: Кеширование времени воспроизведения
	
	// TODO: Открытие MainActivity по нажатию на уведомление
	
	// TODO Сразу запускать audio на паузе
	
	// fixme: Пересоздание уведомления при закрытии app'а. Проблема с startForeground()
	
	private val serviceState: AudioPlayerState = get()
	private val disposable = CompositeDisposable()
	
	// First test audio
	//private lateinit var audio: Audio
	private var audio: Audio = Audio(
		id = 47829250,
		genreId = 6,
		title = "Come As You Are",
		artist = "Nirvana",
		duration = 5072438000,
		url = "https://muzlo.me/get/music/20170830/muzlome_Nirvana_-_Come_As_You_Are_47829250.mp3",
		posterUrl = "https://muzlo.me//cover/song/47829250.jpg"
	)
	
	private val metadataBuilder = MediaMetadataCompat.Builder()
	private val exoPlayer: ExoPlayer by lazy { ExoPlayerFactory.newSimpleInstance(this) }
	private val audioManager: AudioManager by lazy { getSystemService(Context.AUDIO_SERVICE) as AudioManager }
	private val mediaSession: MediaSessionCompat by lazy { MediaSessionCompat(this, "myTag-PlayerService") }
	private val mediaSessionCallback = MediaSessionCallback(this)
	private val stateBuilder = Builder().setActions(getAudioActions())
	
	private var activeState = STATE_PAUSED
	
	private val audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
		when (focusChange) {
			AudioManager.AUDIOFOCUS_GAIN -> mediaSessionCallback.onPlay()
			AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> mediaSessionCallback.onPause()
			else -> mediaSessionCallback.onPause()
		}
	}
	
	private val becomingNoisyReceiver: BroadcastReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
				mediaSessionCallback.onPause()
			}
		}
	}
	
	private var isBecomingNoisyReceiver = false
		set(value) {
			if (value && !isBecomingNoisyReceiver) {
				registerReceiver(
					becomingNoisyReceiver,
					IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
				)
			} else if (!value && isBecomingNoisyReceiver) {
				unregisterReceiver(becomingNoisyReceiver)
			}
			
			field = value
		}
	
	private val audioFocusRequest: AudioFocusRequest?
		get() = if (isOreoOrMore()) {
			val audioAttributes = AudioAttributes.Builder()
				.setUsage(AudioAttributes.USAGE_MEDIA)
				.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
				.build()
			
			AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
				.setOnAudioFocusChangeListener(audioFocusChangeListener)
				.setAcceptsDelayedFocusGain(false)
				.setWillPauseWhenDucked(true)
				.setAudioAttributes(audioAttributes)
				.build()
		} else null
	
	override fun onCreate() {
		super.onCreate()
		
		mediaSession.setMediaButtonReceiver(getPendingMediaButtonIntent())
		mediaSession.setCallback(mediaSessionCallback)
		mediaSession.setFlags(
			MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
		)
		
		subscribeToData()
		
		onPlay()
		onPause()
	}
	
	override fun onDestroy() {
		mediaSession.release()
		disposable.clear()
		super.onDestroy()
	}
	
	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		MediaButtonReceiver.handleIntent(mediaSession, intent)
		
		// fixme (ниже костыль): Context.startForegroundService() did not then call Service.startForeground()
		// Тут таже проблема, что и ниже. Похоже, что кто-то где-то вызывает start или stop foreground
		if (activeState == STATE_PAUSED) {
			startForeground(NOTIFICATION_ID, createAudioPlayerNotification(activeState, mediaSession))
			stopForeground(false)
		}
		if (activeState == STATE_STOPPED) {
			startForeground(NOTIFICATION_ID, createAudioPlayerNotification(activeState, mediaSession))
			stopForeground(true)
		}
		
		return Service.START_STICKY
	}
	
	override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {}
	
	override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? = null
	
	override fun onPlay() {
		metadataBuilder.apply {
			putBitmap(
				MediaMetadataCompat.METADATA_KEY_ART,
				BitmapFactory.decodeResource(resources, 0)
			)
			putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.title)
			putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audio.artist)
			putLong(MediaMetadataCompat.METADATA_KEY_DURATION, audio.duration)
		}
		
		mediaSession.setMetadata(metadataBuilder.build())
		mediaSession.isActive = true
		
		setPlaybackState(STATE_PLAYING, PLAYBACK_POSITION_UNKNOWN)
		exoPlayer.playWhenReady = true
		isBecomingNoisyReceiver = true
		exoPlayer.prepare(audio.url, this)
		
		refreshNotificationAndForegroundStatus(STATE_PLAYING)
	}
	
	override fun onPause() {
		setPlaybackState(STATE_PAUSED, PLAYBACK_POSITION_UNKNOWN)
		exoPlayer.playWhenReady = false
		isBecomingNoisyReceiver = false
		
		refreshNotificationAndForegroundStatus(STATE_PAUSED)
	}
	
	override fun onStop() {
		if (isOreoOrMore()) {
			audioManager.abandonAudioFocusRequest(audioFocusRequest!!)
		} else {
			@Suppress("DEPRECATION")
			audioManager.abandonAudioFocus(audioFocusChangeListener)
		}
		
		mediaSession.isActive = false
		setPlaybackState(STATE_STOPPED, PLAYBACK_POSITION_UNKNOWN)
		exoPlayer.playWhenReady = false
		isBecomingNoisyReceiver = false
		
		refreshNotificationAndForegroundStatus(STATE_STOPPED)
	}
	
	override fun onSkipToPrevious() {
	
	}
	
	override fun onSkipToNext() {
	
	}
	
	
	private fun subscribeToData() {
		disposable += serviceState.audio.subscribe {
			this.audio = it
			onPlay()
		}
	}
	
	private fun refreshNotificationAndForegroundStatus(playbackState: Int) {
		activeState = playbackState
		
		when (playbackState) {
			STATE_PLAYING -> startForeground(
				NOTIFICATION_ID,
				createAudioPlayerNotification(playbackState, mediaSession)
			)
			
			STATE_PAUSED -> {
				notify(NOTIFICATION_ID, createAudioPlayerNotification(playbackState, mediaSession))
				stopForeground(false)
			}
			
			else -> {
				// fixme (ниже костыль): Context.startForegroundService() did not then call Service.startForeground()
				// Нельзя два раза подряд вызывать stopForeground, иначе падение
				// https://stackoverflow.com/questions/44425584/context-startforegroundservice-did-not-then-call-service-startforeground
				startForeground(NOTIFICATION_ID, createAudioPlayerNotification(playbackState, mediaSession))
				
				stopForeground(true)
			}
		}
	}
	
	private fun setPlaybackState(
		@State state: Int,
		position: Long,
		playbackSpeed: Float = 1f
	) {
		mediaSession.setPlaybackState(
			stateBuilder.setState(
				state,
				position,
				playbackSpeed
			).build()
		)
	}
	
	
	companion object {
		
		const val CHANNEL_ID = "channelId"
		
		private const val NOTIFICATION_ID = 888
		
		private fun getAudioActions() = ACTION_PLAY or ACTION_STOP or
			ACTION_PAUSE or ACTION_PLAY_PAUSE or
			ACTION_SKIP_TO_NEXT or ACTION_SKIP_TO_PREVIOUS
		
		private fun Context.notify(id: Int, notification: Notification) {
			NotificationManagerCompat
				.from(this)
				.notify(id, notification)
		}
		
		private fun ExoPlayer.prepare(url: String, context: Context) {
			val dataSourceFactory = DefaultDataSourceFactory(
				context,
				Util.getUserAgent(context, context.appName),
				DefaultBandwidthMeter()
			)
			
			val mediaSource = ExtractorMediaSource
				.Factory(dataSourceFactory)
				.createMediaSource(Uri.parse(url))
			
			prepare(mediaSource)
		}
		
		private fun Context.getPendingMediaButtonIntent(): PendingIntent {
			val mediaButtonIntent = Intent(
				Intent.ACTION_MEDIA_BUTTON,
				null,
				this,
				MediaButtonReceiver::class.java
			)
			
			return PendingIntent.getBroadcast(
				this,
				0,
				mediaButtonIntent,
				0
			)
		}
		
	}
	
}