package magym.core.data.data.entity

import magym.core.common.recycler.KeyEntity

data class Genre(
	override val id: Int,
	val title: String
) : KeyEntity<Int>