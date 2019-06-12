package magym.core.common.extention

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

var View.onClick: () -> Unit
	get() = {}
	set(value) = setOnClickListener { value() }


fun RecyclerView.init(
	adapter: RecyclerView.Adapter<*>,
	layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
) {
	this.layoutManager = layoutManager
	this.adapter = adapter
}


fun Context.inflate(@LayoutRes resource: Int, root: ViewGroup? = null, attachToRoot: Boolean = false): View {
	val inflater = LayoutInflater.from(this)
	return inflater.inflate(resource, root, attachToRoot)
}

fun View.inflate(@LayoutRes resource: Int, root: ViewGroup? = null, attachToRoot: Boolean = false): View {
	return context.inflate(resource, root, attachToRoot)
}


fun View.closeSoftKeyboard() {
	val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
	imm.hideSoftInputFromWindow(windowToken, 0)
}
