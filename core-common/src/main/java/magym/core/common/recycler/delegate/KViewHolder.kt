package magym.core.common.recycler.delegate

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

class KViewHolder(
	override val containerView: View,
	onCreated: (KViewHolder) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
	
	init {
		onCreated.invoke(this)
	}
	
}