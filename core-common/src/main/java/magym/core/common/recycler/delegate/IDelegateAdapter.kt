package magym.core.common.recycler.delegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import magym.core.common.recycler.KeyEntity

interface IDelegateAdapter<T : KeyEntity<*>> {
	
	fun isForViewType(items: List<*>, position: Int): Boolean
	
	fun onAttachedToRecyclerView(recyclerView: RecyclerView)
	
	fun onCreateViewHolder(parent: ViewGroup, viewType: Int, items: () -> List<T>): KViewHolder
	
	fun onBindViewHolder(holder: KViewHolder, items: List<T>, position: Int)
	
}
