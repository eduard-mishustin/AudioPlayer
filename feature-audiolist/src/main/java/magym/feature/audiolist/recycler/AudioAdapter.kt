package magym.feature.audiolist.recycler

import android.view.View
import magym.core.common.recycler.BaseAdapter
import magym.core.data.data.entity.Audio
import magym.feature.audiolist.R

internal class AudioAdapter(
	private val onClick: (Audio) -> Unit
) : BaseAdapter<Audio, AudioViewHolder>() {
	
	override fun onLayoutRequested(viewType: Int) = R.layout.item_audio
	
	override fun onCreateViewHolder(view: View, viewType: Int) = AudioViewHolder(view, ::onClick)
	
	
	private fun onClick(position: Int) = onClick.invoke(items[position])
	
}