package magym.core.data.data.parser

import magym.core.data.data.entity.Audio
import magym.core.data.data.entity.Genre

internal interface AudioApi {
	
	fun getGenre(id: Int): Genre
	
	fun getGenres(): List<Genre>
	
	fun getAudios(genreId: Int): List<Audio>
	
}