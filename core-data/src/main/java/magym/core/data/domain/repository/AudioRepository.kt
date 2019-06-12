package magym.core.data.domain.repository

import androidx.paging.PagedList
import io.reactivex.Observable
import magym.core.data.data.entity.Audio

interface AudioRepository {
	
	fun getAudios(genreId: Int = 0, filterQuery: String = ""): Observable<PagedList<Audio>>
	
}