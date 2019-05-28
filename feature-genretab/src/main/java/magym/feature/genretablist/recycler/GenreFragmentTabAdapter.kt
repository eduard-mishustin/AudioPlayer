package magym.feature.genretablist.recycler

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import magym.core.common.recycler.simple.EntityDiffCallback
import magym.core.data.data.entity.Genre
import magym.feature.audiolist.AudioListFragment

internal class GenreFragmentTabAdapter(
	fragmentManager: FragmentManager,
	lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
	
	var items: List<Genre> = listOf()
		set(value) {
			val callback = EntityDiffCallback(field, value) { it.id }
			val result = DiffUtil.calculateDiff(callback)
			
			field = value
			
			result.dispatchUpdatesTo(this)
		}
	
	val lastFragmentIndex get() = fragments.size - 1
	
	private val fragments: MutableList<Fragment> = mutableListOf()
	
	init {
		registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
			override fun onChanged() {
				items.forEach { fragments.add(AudioListFragment.newInstance(it.id)) }
			}
			
			override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {}
			
			override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
				for (i in positionStart until positionStart + itemCount) {
					fragments.add(AudioListFragment.newInstance(items[i].id))
				}
			}
			
			override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {}
			
			override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {}
		})
	}
	
	
	override fun getItem(position: Int) = fragments[position]
	
	override fun getItemCount() = fragments.size
	
	
	fun addFragmentToEnd() {
		fragments.add(AudioListFragment.newInstance(0))
	}
	
	fun removeLastFragment() {
		fragments.remove(fragments.last())
	}
	
	fun getItemPosition(id: Any) = items.indexOfFirst { it.id == id }
	
}