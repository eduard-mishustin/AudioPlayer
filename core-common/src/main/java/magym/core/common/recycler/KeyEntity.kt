package magym.core.common.recycler

interface KeyEntity<Key> {
	
	val id: Key
	
	override fun equals(other: Any?): Boolean
	
}