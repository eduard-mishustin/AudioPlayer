package magym.core.data.domain

import io.reactivex.Observable
import magym.core.data.data.entity.Genre

interface GenreRepository {
	
	fun getGenres(): Observable<List<Genre>>
	
	fun getGenre(id: Int): Observable<Genre>
	
}