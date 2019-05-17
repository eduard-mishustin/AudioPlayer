package magym.audioplayer.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import magym.audioplayer.R
import magym.core.common.ActivityProvider
import magym.core.common.extention.init

class MainActivity : BaseActivity(), ActivityProvider {

	// TODO: Везде где можно расставить internal

	override var isLoading: Boolean
		get() = progressbar.isVisible
		set(value) {
			progressbar.isVisible = value
		}

	override var titleToolbar: String
		get() = supportActionBar?.title.toString()
		set(value) {
			supportActionBar?.title = value
		}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		toolbar.init(this)
	}

	override fun onSupportNavigateUp(): Boolean = findNavController(R.id.nav_host_fragment).navigateUp()

}