package magym.feature.featureaudioadapter

import android.view.View
import magym.core.common.recycler.paged.PagedBaseAdapter
import magym.core.data.data.entity.Audio

class AudioAdapter(
	private val onClick: (Audio) -> Unit
) : PagedBaseAdapter<Audio, AudioViewHolder>() {
	
	override fun onLayoutRequested(viewType: Int) = R.layout.item_audio
	
	override fun onCreateViewHolder(view: View, viewType: Int) = AudioViewHolder(view, ::onClick)
	
	
	private fun onClick(position: Int) {
		getItem(position)?.let { onClick.invoke(it) }
	}
	
}