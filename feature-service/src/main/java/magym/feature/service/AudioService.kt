package magym.feature.service

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
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
import magym.core.common.extention.appName
import magym.core.common.extention.isOreoOrMore
import magym.core.data.data.entity.Audio
import magym.feature.service.provide.AudioServiceRepository
import magym.feature.service.util.createAudioPlayerNotification


class AudioService : MediaBrowserServiceCompat(), AudioServiceRepository {
	
	// TODO: Снижать громкость, а не ставить на паузу
	// AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK
	// setWillPauseWhenDucked
	
	// TODO: Реализация ACTION_SKIP_TO_NEXT и ACTION_SKIP_TO_PREVIOUS
	
	// TODO: Кеширование времени воспроизведения
	
	// Fixme: Падение при закрытии уведомления
	
	private val context by lazy { this as Context }
	private val binder = LocalBinder()
	
	private var audio: Audio? = null
	
	private val metadataBuilder = MediaMetadataCompat.Builder()
	private val exoPlayer: ExoPlayer by lazy { ExoPlayerFactory.newSimpleInstance(this) }
	private val audioManager: AudioManager by lazy { getSystemService(Context.AUDIO_SERVICE) as AudioManager }
	private val mediaSession: MediaSessionCompat by lazy { MediaSessionCompat(this, "myTag-PlayerService") }
	private val stateBuilder = Builder().setActions(getAudioActions())
	
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
	
	private var mediaSessionCallback: MediaSessionCompat.Callback = object : MediaSessionCompat.Callback() {
		
		override fun onSkipToPrevious() {
		
		}
		
		override fun onPlay() {
			audio?.let { playAudio(it) }
		}
		
		override fun onPause() {
			pauseAudio()
		}
		
		override fun onSkipToNext() {
		
		}
		
		override fun onStop() {
			stopAudio()
		}
		
	}
	
	
	override fun onCreate() {
		super.onCreate()
		
		mediaSession.apply {
			val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON, null, context, MediaButtonReceiver::class.java)
			
			setMediaButtonReceiver(
				PendingIntent.getBroadcast(
					context,
					0,
					mediaButtonIntent,
					0
				)
			)
			
			setFlags(
				MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
			)
			
			setCallback(mediaSessionCallback)
			
			// Укажем activity, которую запустит система, если пользователь
			// заинтересуется подробностями данной сессии
			/*val activityIntent = Intent(applicationContext, MainActivity::class.java)
			mediaSession.setSessionActivity(
				PendingIntent.getActivity(applicationContext, 0, activityIntent, 0)
			)*/
		}
	}
	
	override fun onDestroy() {
		mediaSession.release()
		super.onDestroy()
	}
	
	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		MediaButtonReceiver.handleIntent(mediaSession, intent)
		return super.onStartCommand(intent, flags, startId)
	}
	
	override fun onBind(intent: Intent): IBinder = binder
	
	override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {}
	
	override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? = null
	
	
	override fun playAudio(audio: Audio) {
		this.audio = audio
		
		val audioFocusResult = if (isOreoOrMore()) {
			audioManager.requestAudioFocus(audioFocusRequest!!)
		} else {
			@Suppress("DEPRECATION")
			audioManager.requestAudioFocus(
				audioFocusChangeListener,
				AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN
			)
		}
		
		if (audioFocusResult != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) return
		mediaSession.isActive = true
		
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
		
		exoPlayer.prepare(audio.url, this)
		exoPlayer.playWhenReady = true
		isBecomingNoisyReceiver = true
		
		refreshNotificationAndForegroundStatus(STATE_PLAYING)
	}
	
	private fun pauseAudio() {
		exoPlayer.playWhenReady = false
		
		setPlaybackState(STATE_PAUSED, PLAYBACK_POSITION_UNKNOWN)
		
		isBecomingNoisyReceiver = false
		refreshNotificationAndForegroundStatus(STATE_PAUSED)
	}
	
	private fun stopAudio() {
		if (isOreoOrMore()) {
			audioManager.abandonAudioFocusRequest(audioFocusRequest!!)
		} else {
			@Suppress("DEPRECATION")
			audioManager.abandonAudioFocus(audioFocusChangeListener)
		}
		
		exoPlayer.playWhenReady = false
		mediaSession.isActive = false
		
		setPlaybackState(STATE_STOPPED, PLAYBACK_POSITION_UNKNOWN)
		
		isBecomingNoisyReceiver = false
		
		refreshNotificationAndForegroundStatus(STATE_STOPPED)
		stopSelf()
	}
	
	
	private fun refreshNotificationAndForegroundStatus(playbackState: Int) {
		when (playbackState) {
			STATE_PLAYING -> startForeground(
				NOTIFICATION_ID,
				createAudioPlayerNotification(playbackState, mediaSession)
			)
			
			STATE_PAUSED -> {
				notify(NOTIFICATION_ID, createAudioPlayerNotification(playbackState, mediaSession))
				stopForeground(false)
			}
			
			else -> stopForeground(true)
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
		
		const val NOTIFICATION_ID = 888
		
		private fun getAudioActions() = ACTION_PLAY or ACTION_STOP or
			ACTION_PAUSE or ACTION_PLAY_PAUSE or
			ACTION_SKIP_TO_NEXT or ACTION_SKIP_TO_PREVIOUS
		
		fun Context.notify(id: Int, notification: Notification) {
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
	}
	
	/**
	 * Class used for the client Binder.  Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	inner class LocalBinder : Binder() {
		// Return this instance of Service so clients can call public methods
		val service: AudioService
			get() = this@AudioService
		
		val mediaSessionToken: MediaSessionCompat.Token
			get() = mediaSession.sessionToken
	}
	
}