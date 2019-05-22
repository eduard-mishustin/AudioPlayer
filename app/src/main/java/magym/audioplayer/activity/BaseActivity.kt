package magym.audioplayer.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import magym.audioplayer.R
import magym.audioplayer.navigation.Navigator
import org.koin.android.ext.android.get

abstract class BaseActivity : AppCompatActivity() {
	
	private val navigator: Navigator = get()
	
	override fun onResume() {
		super.onResume()
		navigator.bind(findNavController(R.id.nav_host_fragment))
	}
	
	override fun onPause() {
		navigator.unbind()
		super.onPause()
	}
	
	override fun onSupportNavigateUp(): Boolean = findNavController(R.id.nav_host_fragment).navigateUp()
	
}