package magym.feature.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class AudioNotificationManager(private val context: Context) {

    private val notificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    //fun updateNotification() = notificationManager.notify(NOTIFICATION_ID, buildNotification())

    @SuppressLint("PrivateResource")
    fun buildNotification(audioUrl: String): Notification {
        return NotificationCompat.Builder(context.applicationContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.notification_icon_background)
            setContentTitle("")
            setContentText("")
        }.build()
    }

    
    companion object {

        const val CHANNEL_ID = "channelId"

        const val NOTIFICATION_ID = 888

    }

}