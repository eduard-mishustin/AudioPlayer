package magym.core.common.recycler.delegate

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import magym.core.common.extention.inflate
import magym.core.common.recycler.KeyEntity
import magym.core.common.recycler.approveClick

/**
 * https://github.com/Liverm0r/DelegateAdapters
 */
abstract class KDelegateAdapter<T : KeyEntity<*>>(
	@LayoutRes private val layoutId: Int
) : IDelegateAdapter<T> {
	
	protected lateinit var recyclerView: RecyclerView
	protected lateinit var context: Context
	
	protected val resources by lazy { context.resources!! }
	
	protected open fun KViewHolder.approveClick(callback: (position: Int) -> Unit) = (this as RecyclerView.ViewHolder).approveClick(callback)
	
	final override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
		this.recyclerView = recyclerView
	}
	
	final override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int,
		items: () -> List<T>
	): KViewHolder {
		if (!::context.isInitialized) {
			context = parent.context
		}
		
		val containerView = parent.inflate(layoutId)
		
		return KViewHolder(containerView) {
			it.onCreated { items.invoke()[it.adapterPosition] }
		}
	}
	
	final override fun onBindViewHolder(holder: KViewHolder, items: List<T>, position: Int) {
		holder.onBind(items[position])
	}
	
	/**
	 * Аналог RecyclerView.onCreateViewHolder()
	 *
	 * Необходимо помнить, что элемент списка получается на основе позиции адаптера,
	 * но так как позиция адаптера инициализируется в onBindViewHolder после onCreateViewHolder,
	 * необходимо учитывать это и не просить item сразу в onCreated.
	 *
	 * Учитываем это и используем approveClick следущим образом:
	 * approveClick { item.invoke() }
	 */
	open fun KViewHolder.onCreated(item: () -> T) = Unit
	
	/**
	 * Аналог RecyclerView.onBindViewHolder()
	 */
	open fun KViewHolder.onBind(item: T) = Unit
	
	protected fun getString(@StringRes id: Int): String = resources.getString(id)
	
}