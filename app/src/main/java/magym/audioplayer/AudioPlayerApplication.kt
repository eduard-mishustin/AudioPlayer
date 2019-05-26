package magym.audioplayer

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import magym.core.common.extention.appName
import magym.core.common.extention.isOreoOrMore
import magym.feature.service.AudioService.Companion.CHANNEL_ID
import org.koin.android.ext.android.startKoin

internal class AudioPlayerApplication : Application() {
	
	override fun onCreate() {
		super.onCreate()
		createNotificationChannel()
		
		startKoin(
			androidContext = this,
			modules = koinModules
		)
	}
	
	
	private fun createNotificationChannel() {
		if (!isOreoOrMore()) return
		
		val channel = NotificationChannel(CHANNEL_ID, appName, NotificationManager.IMPORTANCE_LOW).apply {
			//setShowBadge(false)
			lockscreenVisibility = Notification.VISIBILITY_PUBLIC
		}
		
		val notificationManager = getSystemService(NotificationManager::class.java)
		notificationManager.createNotificationChannel(channel)
	}
	
}