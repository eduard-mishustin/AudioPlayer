package magym.core.common.extention

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.removeFragment(fragment: Fragment?) {
	fragment?.let {
		beginTransaction().remove(it)
			.commit()
	}
}