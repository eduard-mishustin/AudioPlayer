package magym.feature.audiolist.recycler

import android.view.View
import magym.core.common.recycler.paged.PagedBaseAdapter
import magym.core.data.data.entity.Audio
import magym.feature.audiolist.R

internal class AudioAdapter(
	private val onClick: (Audio) -> Unit
) : PagedBaseAdapter<Audio, AudioViewHolder>() {
	
	override fun onLayoutRequested(viewType: Int) = R.layout.item_audio
	
	override fun onCreateViewHolder(view: View, viewType: Int) = AudioViewHolder(view, ::onClick)
	
	
	private fun onClick(position: Int) {
		getItem(position)?.let { onClick.invoke(it) }
	}
	
}