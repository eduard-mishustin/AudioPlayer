package magym.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import magym.core.common.extention.argumentIsNotExist
import magym.core.common.extention.createSnackbarWithAction
import magym.core.common.extention.toast
import org.koin.android.ext.android.get

abstract class BaseFragment : Fragment() {
	
	protected abstract val layoutId: Int
	
	protected val navigation: AudioPlayerNavigation = get()
	
	protected val activityProvider by lazy { activity as ActivityProvider }
	
	protected val supportFragmentManager by lazy { (activity as FragmentActivity).supportFragmentManager }
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(layoutId, container, false)
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