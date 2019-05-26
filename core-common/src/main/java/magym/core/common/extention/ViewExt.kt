package magym.core.common.extention

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

var View.onClick: () -> Unit
	get() = {}
	set(value) = setOnClickListener { value() }


fun Toolbar.init(activity: AppCompatActivity) = apply {
	activity.setSupportActionBar(this)
}

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