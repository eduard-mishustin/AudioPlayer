package magym.core.data.domain

import io.reactivex.Observable
import magym.core.data.data.entity.Audio

interface AudioRepository {
	
	fun getAudios(genreId: Int, filterQuery: String): Observable<List<Audio>>
	
}