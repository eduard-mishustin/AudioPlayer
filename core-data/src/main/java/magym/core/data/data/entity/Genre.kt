package magym.core.data.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import magym.core.common.recycler.KeyEntity

@Entity
data class Genre(
	@PrimaryKey
	override val id: Int,
	
	val title: String
) : KeyEntity<Int>