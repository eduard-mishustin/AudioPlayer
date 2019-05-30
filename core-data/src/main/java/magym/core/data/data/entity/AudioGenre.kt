package magym.core.data.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import magym.core.common.recycler.KeyEntity

/*
* Зависимость многие ко многим между аудио и жанром.
*
* Но, как я узнал позже, для текущего апи это неприменимо, на каждый жанр одного и того же аудио у аудио разные id
* */

@Entity
data class AudioGenre(
	@PrimaryKey
	override val id: String,
	
	val audioId: Int,
	val genreId: Int
) : KeyEntity<String>