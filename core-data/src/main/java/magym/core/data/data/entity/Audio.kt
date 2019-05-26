package magym.core.data.data.entity

import magym.core.common.recycler.KeyEntity

data class Audio(
	override val id: Int,
	val genreId: Int,
	val title: String,
	val artist: String,
	val duration: Long,
	val url: String,
	val posterUrl: String
) : KeyEntity<Int>