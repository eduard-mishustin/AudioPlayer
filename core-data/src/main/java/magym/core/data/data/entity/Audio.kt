package magym.core.data.data.entity

import magym.core.common.recycler.KeyEntity

data class Audio(
    override val id: Int,
    val name: String,
    val group: String,
    val url: String,
    val coverUrl: String
) : KeyEntity<Int>