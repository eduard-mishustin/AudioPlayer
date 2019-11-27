package magym.core.common.recycler.delegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import magym.core.common.recycler.KeyEntity

class CompositeDelegateAdapter private constructor(
	private val typeToAdapterMap: MutableMap<Int, IDelegateAdapter<KeyEntity<*>>>
) : RecyclerView.Adapter<KViewHolder>() {
	
	var items: List<KeyEntity<*>> = listOf()
		set(value) {
			/*
			FIXME: Bug: При своевременном обновлении происходит заметная перерисовка элемента списка (моргание)
			 Такое чувтсво, что view holder пересоздаётся при каждом обновлении, хотя onCreateViewHolder не вызывается
			 Такое происходит только при использовании DiffUtil
			 а при обычном notifyDataSetChanged всё работает достаточно корректно и без подвисаний, но тут больше мороки и проблемы так или иначе имеются
			*/
			
			//val callback = EntityDiffCallback(field, value) { it.id }
			//val result = DiffUtil.calculateDiff(callback)
			//field = value
			//result.dispatchUpdatesTo(this)
			
			field = value
			notifyDataSetChanged()
		}
	
	init {
		setHasStableIds(true)
	}
	
	constructor(delegateAdapter: IDelegateAdapter<out KeyEntity<*>>) : this(
		delegateAdapter.let {
			val typeToAdapterMap: MutableMap<Int, IDelegateAdapter<KeyEntity<*>>> = mutableMapOf()
			@Suppress("UNCHECKED_CAST")
			typeToAdapterMap.put(FIRST_VIEW_TYPE, delegateAdapter as IDelegateAdapter<KeyEntity<*>>)
			typeToAdapterMap
		}
	)
	
	override fun getItemViewType(position: Int): Int {
		for (key in FIRST_VIEW_TYPE until typeToAdapterMap.size) {
			val delegate = typeToAdapterMap[key]!!
			
			if (delegate.isForViewType(items, position)) {
				return key
			}
		}
		
		throw NullPointerException("Can not get viewType for position $position")
	}
	
	override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
		super.onAttachedToRecyclerView(recyclerView)
		typeToAdapterMap.forEach { it.value.onAttachedToRecyclerView(recyclerView) }
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KViewHolder {
		return typeToAdapterMap[viewType]!!
			.onCreateViewHolder(parent, viewType) { items }
	}
	
	override fun onBindViewHolder(holder: KViewHolder, position: Int) {
		typeToAdapterMap[getItemViewType(position)]!!
			.onBindViewHolder(holder, items, position)
	}
	
	override fun getItemCount() = items.size
	
	override fun getItemId(position: Int) = items[position].id.hashCode().toLong()
	
	
	class Builder {
		
		private var count = FIRST_VIEW_TYPE
		private val typeToAdapterMap: MutableMap<Int, IDelegateAdapter<KeyEntity<*>>> =
			mutableMapOf()
		
		fun add(delegateAdapter: IDelegateAdapter<out KeyEntity<*>>): Builder {
			@Suppress("UNCHECKED_CAST")
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