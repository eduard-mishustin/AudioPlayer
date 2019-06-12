package magym.core.common.recycler.simple

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import magym.core.common.extention.inflate
import magym.core.common.recycler.BaseViewHolder
import magym.core.common.recycler.EntityDiffCallback
import magym.core.common.recycler.KeyEntity

abstract class BaseAdapter<T : KeyEntity<*>, VH : BaseViewHolder<T>>
	: RecyclerView.Adapter<VH>() {
	
	var items: List<T> = listOf()
		set(value) {
			val callback = EntityDiffCallback(field, value) { it.id }
			val result = DiffUtil.calculateDiff(callback)
			
			field = value
			
			result.dispatchUpdatesTo(this)
		}
	
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
	
	override fun getItemCount() = items.size
	
	override fun getItemId(position: Int) = items[position].id.hashCode().toLong()
	
	override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
	
	
	abstract fun onLayoutRequested(viewType: Int): Int
	
	abstract fun onCreateViewHolder(view: View, viewType: Int): VH
	
	
	fun getItemPosition(id: Any) = items.indexOfFirst { it.id == id }
	
}