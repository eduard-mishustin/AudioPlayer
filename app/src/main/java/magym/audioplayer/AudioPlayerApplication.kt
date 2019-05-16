package magym.audioplayer

import android.app.Application
import org.koin.android.ext.android.startKoin

class AudioPlayerApplication : Application() {
	
	override fun onCreate() {
		super.onCreate()
		
		startKoin(
			androidContext = this,
			modules = koinModules
		)
	}
	
}