package magym.feature.genretablist.recycler

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import magym.core.common.recycler.EntityDiffCallback
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
			notifyDataSetChanged()
		}

	override fun createFragment(position: Int): Fragment {
		return AudioListFragment.newInstance(items[position].id)
	}

	override fun getItemCount() = items.size
	
	
	fun getItemPosition(id: Any) = items.indexOfFirst { it.id == id }
	
}