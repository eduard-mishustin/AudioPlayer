package magym.core.data.domain

import io.reactivex.Observable
import magym.core.common.extention.launch
import magym.core.data.data.base.dao.AudioDao
import magym.core.data.data.base.dao.GenreDao
import magym.core.data.data.entity.Audio
import magym.core.data.data.entity.Genre
import magym.core.data.data.parser.AudioParserApi

internal class AudioProvider(
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
	
	override fun getAudios(genreId: Int, filterQuery: String): Observable<List<Audio>> {
		launch { audioDao.insert(parser.getAudios(genreId)) }
		return audioDao.getAudios(genreId, "%$filterQuery%")
	}
	
}