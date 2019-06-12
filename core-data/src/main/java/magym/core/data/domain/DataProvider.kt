package magym.core.data.domain

import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toObservable
import io.reactivex.Observable
import magym.core.common.extention.launch
import magym.core.common.recycler.paged.PagedBaseAdapter.Companion.defaultPagingConfig
import magym.core.data.data.base.dao.AudioDao
import magym.core.data.data.base.dao.AudioGenreDao
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
	private val audioGenreDao: AudioGenreDao,
	private val parser: AudioParserApi
) : GenreRepository, AudioRepository {
	
	// todo add retry by rx not callback
	
	override fun getGenre(id: Int): Observable<Genre> =
		Observable.fromCallable { parser.getGenre(id) }
	
	override fun getGenres(filterQuery: String): Observable<List<Genre>> {
		launch { genreDao.insert(parser.getGenres()) }
		return genreDao.getGenres("%$filterQuery%")
	}
	
	override fun getAudios(genreId: Int, filterQuery: String): Observable<PagedList<Audio>> {
		val audioBoundaryCallback = AudioBoundaryCallback(
			audioDao = audioDao,
			audioGenreDao = audioGenreDao,
			parser = parser,
			genreId = genreId,
			filterQuery = filterQuery
		)
		
		val daoQuery: DataSource.Factory<Int, Audio> =
			if (filterQuery.isEmpty()) audioDao.getAudios(genreId)
			else audioDao.getAudios("%$filterQuery%")
		
		return daoQuery.toObservable(
			config = defaultPagingConfig,
			boundaryCallback = audioBoundaryCallback
		)
	}
	
}