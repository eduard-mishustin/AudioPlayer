package magym.core.common.recycler.paged

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.paging.Config
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import magym.core.common.extention.inflate
import magym.core.common.recycler.BaseViewHolder
import magym.core.common.recycler.KeyEntity

abstract class PagedBaseAdapter<T : KeyEntity<*>, VH : BaseViewHolder<T>> :
	PagedListAdapter<T, VH>(getDiffItemCallback()) {
	
	protected lateinit var context: Context
		private set
	
	
	override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
		super.onAttachedToRecyclerView(recyclerView)
		context = recyclerView.context
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
		val view = parent.inflate(onLayoutRequested(viewType), parent)
		return onCreateViewHolder(view, viewType)
	}
	
	override fun onBindViewHolder(holder: VH, position: Int) {
		getItem(position)?.let { holder.bind(it) }
	}
	
	
	abstract fun onLayoutRequested(viewType: Int): Int
	
	abstract fun onCreateViewHolder(view: View, viewType: Int): VH
	
	
	companion object {
		
		val defaultPagingConfig = Config(
			pageSize = 75,
			prefetchDistance = 140,
			enablePlaceholders = true
		)
		
		private fun <T : KeyEntity<*>> getDiffItemCallback() = object : DiffUtil.ItemCallback<T>() {
			
			override fun areItemsTheSame(
				oldConcert: T,
				newConcert: T
			) = oldConcert.id == newConcert.id
			
			override fun areContentsTheSame(
				oldConcert: T,
				newConcert: T
			) = oldConcert == newConcert
			
		}
		
	}
	
}