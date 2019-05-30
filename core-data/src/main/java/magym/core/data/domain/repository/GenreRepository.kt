package magym.core.data.domain.repository

import io.reactivex.Observable
import magym.core.data.data.entity.Genre

interface GenreRepository {
	
	fun getGenre(id: Int): Observable<Genre>
	
	fun getGenres(filterQuery: String = ""): Observable<List<Genre>>
	
}