package magym.feature.audiolist.recycler

import android.annotation.SuppressLint
import android.view.View
import kotlinx.android.synthetic.main.item_audio.*
import magym.core.common.extention.onClick
import magym.core.common.recycler.BaseViewHolder
import magym.core.data.data.entity.Audio

class AudioViewHolder(
	containerView: View,
	private val onClick: (Int) -> Unit
) : BaseViewHolder<Audio>(containerView) {
	
	init {
		layout.onClick = { approveClick { onClick.invoke(adapterPosition) } }
	}

	@SuppressLint("SetTextI18n")
	override fun bind(item: Audio) {
		title.text = "${item.artist} – ${item.title}"
	}
	
}