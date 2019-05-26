package magym.core.data.domain

import androidx.paging.PagedList
import androidx.paging.toObservable
import io.reactivex.Observable
import magym.core.common.extention.launch
import magym.core.common.recycler.paged.PagedBaseAdapter.Companion.defaultPagingConfig
import magym.core.data.data.base.dao.AudioDao
import magym.core.data.data.base.dao.GenreDao
import magym.core.data.data.entity.Audio
import magym.core.data.data.entity.Genre
import magym.core.data.data.parser.AudioParserApi
import magym.core.data.domain.boundary.AudioBoundaryCallback
import magym.core.data.domain.repository.AudioRepository
import magym.core.data.domain.repository.GenreRepository

internal class DataProvider(
	private val audioDao: AudioDao,
	private val genreDao: GenreDao,
	private val parser: AudioParserApi
) : GenreRepository, AudioRepository {
	
	override fun getGenre(id: Int): Observable<Genre> =
		Observable.fromCallable { parser.getGenre(id) }
	
	override fun getGenres(filterQuery: String): Observable<List<Genre>> {
		launch { genreDao.insert(parser.getGenres()) }
		return genreDao.getGenres("%$filterQuery%")
	}
	
	override fun getAudios(genreId: Int, filterQuery: String): Observable<PagedList<Audio>> {
		val audioBoundaryCallback = AudioBoundaryCallback(
			audioDao = audioDao,
			parser = parser,
			genreId = genreId
		)
		
		return audioDao.getAudios(genreId, "%$filterQuery%").toObservable(
			config = defaultPagingConfig,
			boundaryCallback = audioBoundaryCallback
		)
	}
	
}