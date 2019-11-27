package magym.core.common.recycler

import androidx.recyclerview.widget.RecyclerView

val RecyclerView.ViewHolder.hasPosition: Boolean
	get() = adapterPosition != RecyclerView.NO_POSITION

fun RecyclerView.ViewHolder.approveClick(callback: (position: Int) -> Unit): Boolean {
	if (hasPosition) {
		callback.invoke(adapterPosition)
		return true
	}
	
	return false
}