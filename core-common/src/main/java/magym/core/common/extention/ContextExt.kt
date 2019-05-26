package magym.core.common.extention

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import magym.core.common.R

val Context.appName: String get() = getString(R.string.app_name)

@ColorInt
fun Resources.getColorCompat(@ColorRes id: Int): Int {
	return ResourcesCompat.getColor(this, id, null)
}

fun isOreoOrMore() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O