package magym.core.data.domain

import io.reactivex.Observable
import magym.core.data.data.entity.Audio

interface AudioRepository {
	
	fun getAudios(genreId: Int): Observable<List<Audio>>
	
}