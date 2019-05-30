package magym.core.common.recycler.delegate

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import magym.core.common.recycler.EntityDiffCallback
import magym.core.common.recycler.KeyEntity

class CompositeDelegateAdapter private constructor(
	private val typeToAdapterMap: SparseArray<IDelegateAdapter<KeyEntity<*>>>
) : RecyclerView.Adapter<KViewHolder>() {
	
	var items: List<KeyEntity<*>> = emptyList()
		set(value) {
			val callback = EntityDiffCallback(field, value) { it.id }
			val result = DiffUtil.calculateDiff(callback)
			
			field = value
			
			result.dispatchUpdatesTo(this)
		}
	
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KViewHolder {
		return typeToAdapterMap.get(viewType)
			.onCreateViewHolder(parent, viewType) { items }
	}
	
	override fun onBindViewHolder(holder: KViewHolder, position: Int) {
		typeToAdapterMap.get(getItemViewType(position))
			.onBindViewHolder(holder, items, position)
	}
	
	override fun getItemViewType(position: Int): Int {
		for (i in FIRST_VIEW_TYPE until typeToAdapterMap.size()) {
			val delegate = typeToAdapterMap.valueAt(i)
			
			if (delegate.isForViewType(items, position)) {
				return typeToAdapterMap.keyAt(i)
			}
		}
		
		throw NullPointerException("Can not get viewType for position $position")
	}
	
	override fun getItemCount() = items.size
	
	
	class Builder {
		
		private var count: Int = 0
		private val typeToAdapterMap: SparseArray<IDelegateAdapter<KeyEntity<*>>> = SparseArray()
		
		fun add(delegateAdapter: IDelegateAdapter<out KeyEntity<*>>): Builder {
			typeToAdapterMap.put(count++, delegateAdapter as IDelegateAdapter<KeyEntity<*>>)
			return this
		}
		
		fun build(): CompositeDelegateAdapter {
			if (count == 0) throw IllegalArgumentException("Register at least one adapter")
			return CompositeDelegateAdapter(typeToAdapterMap)
		}
		
	}
	
	companion object {
		
		private const val FIRST_VIEW_TYPE = 0
		
	}
	
}