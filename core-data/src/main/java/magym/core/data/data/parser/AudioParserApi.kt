package magym.core.data.data.parser

import magym.core.data.data.entity.Audio
import magym.core.data.data.entity.Genre

internal interface AudioParserApi {
	
	fun getGenre(id: Int): Genre
	
	fun getGenres(): List<Genre>
	
	fun getAudios(genreId: Int, page: Int = 0): List<Audio>
	
}