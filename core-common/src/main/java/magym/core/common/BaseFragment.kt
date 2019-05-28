package magym.core.common

import android.os.Bundle
import android.view.*
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import magym.core.common.extention.argumentIsNotExist
import magym.core.common.extention.createSnackbarWithAction
import magym.core.common.extention.toast
import org.koin.android.ext.android.get

abstract class BaseFragment : Fragment() {
	
	protected abstract val layoutId: Int
	
	@MenuRes
	protected open val menuResource: Int? = null
	
	protected val navigation: AudioPlayerNavigation = get()
	protected val activityProvider by lazy { activity as ActivityProvider }
	
	protected val activity by lazy { getActivity() as AppCompatActivity }
	
	protected val supportActionBar by lazy { activity.supportActionBar }
	protected val supportFragmentManager by lazy { activity.supportFragmentManager }
	
	protected val disposable by lazy { CompositeDisposable() }
	
	protected var titleToolbar: String
		get() = supportActionBar?.title.toString()
		set(value) {
			supportActionBar?.title = value
		}
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		menuResource?.let { setHasOptionsMenu(true) }
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(layoutId, container, false)
	}
	
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)
		menuResource?.let { inflater.inflate(it, menu) }
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		disposable.clear()
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> activity.onBackPressed()
			else -> return super.onOptionsItemSelected(item)
		}
		
		return true
	}
	
	
	protected fun Toolbar.init(enableArrowUp: Boolean = false) {
		activity.setSupportActionBar(this)
		activity.supportActionBar?.setDisplayHomeAsUpEnabled(enableArrowUp)
	}
	
	protected fun checkArguments(vararg keys: String) {
		keys.forEach {
			if (argumentIsNotExist(it)) {
				toast("Ошибка получения данных")
				navigation.onBackPressed()
			}
		}
	}
	
	protected fun View.showErrorSnackBarWithAction(onActionClick: () -> Unit) {
		createSnackbarWithAction(
			message = "Ошибка получения данных",
			actionText = "Повторить",
			onActionClick = { onActionClick.invoke() }
		).show()
	}
	
	protected companion object {
		
		/**
		 * Прикрепление аргументов из лямбды к фрагменту
		 */
		fun <F : BaseFragment> F.withArguments(bundleInitialization: Bundle.() -> Unit): F {
			arguments = Bundle().apply { bundleInitialization(this) }
			return this
		}
		
	}
	
}