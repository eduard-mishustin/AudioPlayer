package magym.feature.service.provide

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import magym.feature.service.AudioService

class AudioServiceSignatory(
	private val context: Context,
	private val serviceConnection: AudioServiceConnection
) /*: LifecycleObserver*/ {
	
	private val serviceIntent = Intent(context, AudioService::class.java)
	
	private val connection = object : ServiceConnection {
		override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
			val binder = iBinder as AudioService.LocalBinder
			val serviceFacade = binder.service
			serviceConnection.serviceConnected(serviceFacade)
		}
		
		override fun onServiceDisconnected(arg0: ComponentName) {
			serviceConnection.serviceDisconnected()
		}
	}

	//@OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
	fun start() {
		context.startService(serviceIntent)
	}

	//@OnLifecycleEvent(Lifecycle.Event.ON_START)
	fun bind() {
		context.bindService(serviceIntent, connection, 0)
	}

	//@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
	fun unbind() {
		serviceConnection.serviceDisconnected()
		context.unbindService(connection)
	}
	
}