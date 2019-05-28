package magym.core.data.data.parser

import magym.core.data.data.entity.Audio
import magym.core.data.data.entity.Genre

internal interface AudioParserApi {
	
	fun getAudios(genreId: Int, page: Int = 0): List<Audio>
	
	fun searchAudios(filterQuery: String, page: Int = 0): List<Audio>
	
	fun getGenres(): List<Genre>
	
	fun getGenre(id: Int): Genre
	
}