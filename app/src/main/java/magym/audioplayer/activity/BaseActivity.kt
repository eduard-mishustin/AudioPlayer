package magym.audioplayer.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import magym.audioplayer.R
import magym.audioplayer.navigation.Navigator
import magym.feature.service.AudioService
import magym.feature.service.provide.AudioServiceConnection
import magym.feature.service.provide.AudioServiceProxy
import magym.feature.service.provide.AudioServiceSignatory
import org.koin.android.ext.android.get

abstract class BaseActivity : AppCompatActivity(), AudioServiceConnection {

	private val navigator: Navigator = get() // TODO: Add to lifecycle

	private val serviceProxy: AudioServiceProxy = get()

	private val serviceSignatory by lazy { AudioServiceSignatory(this, this) } // TODO: Add to lifecycle


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		serviceSignatory.start()
	}

	override fun onStart() {
		super.onStart()
		serviceSignatory.bind()
	}

	override fun onResume() {
		super.onResume()
		navigator.bind(findNavController(R.id.nav_host_fragment))
	}

	override fun onPause() {
		navigator.unbind()
		super.onPause()
	}

	override fun onStop() {
		serviceSignatory.unbind()
		super.onStop()
	}

	final override fun serviceConnected(service: AudioService) {
		serviceProxy.service = service
		onServiceConnected()
	}
	
	final override fun serviceDisconnected() {
		serviceProxy.service = null
		onServiceDisconnected()
	}


	protected open fun onServiceConnected() {}
	protected open fun onServiceDisconnected() {}
	
}