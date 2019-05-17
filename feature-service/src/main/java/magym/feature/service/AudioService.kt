package magym.feature.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import magym.feature.service.AudioNotificationManager.Companion.NOTIFICATION_ID
import magym.feature.service.provide.AudioServiceRepository

class AudioService : Service(), AudioServiceRepository {

	private val binder = LocalBinder()

	private val notificationManager by lazy { AudioNotificationManager(this) }
	

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY

	override fun onBind(intent: Intent): IBinder = binder

	override fun playAudio(audioUrl: String) {
		startForeground(NOTIFICATION_ID, notificationManager.buildNotification(audioUrl))
	}


	/**
	 * Class used for the client Binder.  Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	inner class LocalBinder : Binder() {
		// Return this instance of Service so clients can call public methods
		val service: AudioService
			get() = this@AudioService
	}
	
}