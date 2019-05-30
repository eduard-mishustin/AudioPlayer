package magym.core.common.recycler.delegate

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import magym.core.common.extention.inflate
import magym.core.common.recycler.KeyEntity

/**
 * https://github.com/Liverm0r/DelegateAdapters
 */
abstract class KDelegateAdapter<T : KeyEntity<*>>(
	@LayoutRes private val layoutId: Int
) : IDelegateAdapter<T> {
	
	protected lateinit var context: Context
	
	protected val KViewHolder.hasPosition: Boolean
		get() = adapterPosition != NO_POSITION
	
	protected open fun KViewHolder.approveClick(callback: (position: Int) -> Unit): Boolean {
		if (hasPosition) {
			callback.invoke(adapterPosition)
			return true
		}
		
		return false
	}
	
	final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, items: () -> List<T>): KViewHolder {
		context = parent.context
		val containerView = parent.inflate(layoutId)
		
		return KViewHolder(containerView) {
			it.onCreate { items.invoke()[it.adapterPosition] }
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
	 * необходимо учитывать это и не просить item сразу в onCreate.
	 *
	 * Учитываем это и используем approveClick следущим образом:
	 * approveClick { item.invoke() }
	 */
	open fun KViewHolder.onCreate(item: () -> T) = Unit
	
	/**
	 * Аналог RecyclerView.onBindViewHolder()
	 */
	open fun KViewHolder.onBind(item: T) = Unit
	
}