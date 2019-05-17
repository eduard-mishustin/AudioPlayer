package magym.feature.genrelist.recycler

import android.view.View
import kotlinx.android.synthetic.main.item_genre.*
import magym.core.common.extention.onClick
import magym.core.common.recycler.BaseViewHolder
import magym.core.data.data.entity.Genre

class GenreViewHolder(
	containerView: View,
	private val onClick: (Int) -> Unit
) : BaseViewHolder<Genre>(containerView) {
	
	init {
		layout.onClick = { approveClick { onClick.invoke(adapterPosition) } }
	}
	
	override fun bind(item: Genre) {
		title.text = item.title
	}
	
}