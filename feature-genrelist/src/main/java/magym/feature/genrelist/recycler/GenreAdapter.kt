package magym.feature.genrelist.recycler

import android.view.View
import magym.core.common.recycler.simple.BaseAdapter
import magym.core.data.data.entity.Genre
import magym.feature.genrelist.R

internal class GenreAdapter(
	private val onClick: (Genre) -> Unit
) : BaseAdapter<Genre, GenreViewHolder>() {
	
	override fun onLayoutRequested(viewType: Int) = R.layout.item_genre
	
	override fun onCreateViewHolder(view: View, viewType: Int) = GenreViewHolder(view, ::onClick)
	
	
	private fun onClick(position: Int) = onClick.invoke(items[position])
	
}