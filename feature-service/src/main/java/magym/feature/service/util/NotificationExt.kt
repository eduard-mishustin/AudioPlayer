package magym.feature.service.util

import android.app.Notification
import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat.*
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver.buildMediaButtonPendingIntent
import magym.feature.service.AudioService.Companion.CHANNEL_ID
import magym.feature.service.R

internal fun Context.createAudioPlayerNotification(
	playbackState: Int,
	mediaSession: MediaSessionCompat
): Notification = MediaStyleHelper.from(this, mediaSession).apply {
	val context = this@createAudioPlayerNotification
	
	addAction(createActionSkipToPrevios())
	
	if (playbackState == STATE_PLAYING) addAction(createActionPause())
	else addAction(createActionPlay())
	
	addAction(createActionSkipToNext())
	
	setStyle(createAudioPlayerStyle(mediaSession))
	
	setSmallIcon(R.drawable.ic_launcher)
	color = ContextCompat.getColor(context, R.color.my_material_grey_800)
	setShowWhen(false)
	priority = NotificationCompat.PRIORITY_HIGH
	setOnlyAlertOnce(true)
	setChannelId(CHANNEL_ID)
}.build()

private fun Context.createActionSkipToNext(): NotificationCompat.Action {
	return NotificationCompat.Action(
		R.drawable.exo_controls_next,
		"Next",
		buildMediaButtonPendingIntent(
			this,
			ACTION_SKIP_TO_NEXT
		)
	)
}

private fun Context.createActionPlay(): NotificationCompat.Action {
	return NotificationCompat.Action(
		R.drawable.exo_controls_play,
		"Play",
		buildMediaButtonPendingIntent(
			this,
			ACTION_PLAY_PAUSE
		)
	)
}

private fun Context.createActionPause(): NotificationCompat.Action {
	return NotificationCompat.Action(
		R.drawable.exo_controls_pause,
		"Pause",
		buildMediaButtonPendingIntent(
			this,
			ACTION_PLAY_PAUSE
		)
	)
}

private fun Context.createActionSkipToPrevios() = NotificationCompat.Action(
	R.drawable.exo_controls_previous,
	"Previous",
	buildMediaButtonPendingIntent(
		this,
		ACTION_SKIP_TO_PREVIOUS
	)
)

private fun Context.createAudioPlayerStyle(
	mediaSession: MediaSessionCompat
) = androidx.media.app.NotificationCompat.MediaStyle()
	.setShowActionsInCompactView(1)
	.setMediaSession(mediaSession.sessionToken)
	.setShowCancelButton(true)
	.setCancelButtonIntent(
		buildMediaButtonPendingIntent(
			this,
			ACTION_STOP
		)
	)
