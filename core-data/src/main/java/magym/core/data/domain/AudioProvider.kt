package magym.core.data.domain

import io.reactivex.Observable
import magym.core.data.data.entity.Audio
import magym.core.data.data.entity.Genre
import magym.core.data.data.parser.AudioApi

internal class AudioProvider(private val parser: AudioApi) : GenreRepository, AudioRepository {
	
	override fun getGenre(id: Int): Observable<Genre> =
		Observable.fromCallable { parser.getGenre(id) }
	
	override fun getGenres(): Observable<List<Genre>> =
		Observable.fromCallable { parser.getGenres() }
	
	override fun getAudios(genreId: Int): Observable<List<Audio>> =
		Observable.fromCallable { parser.getAudios(genreId) }
	
}