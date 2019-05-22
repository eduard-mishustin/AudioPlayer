package magym.core.data

import magym.core.data.data.parser.AudioParser
import magym.core.data.domain.AudioProvider
import org.junit.Assert
import org.junit.Test

class AudioRepositoryTest {
	
	// todo Убрать нетворк, подставлять дефолтную страницу для парсинга
	
	private val parser = AudioParser()
	private val provider = AudioProvider(parser)
	
	@Test
	fun getGenre() {
		val genres = provider.getGenres().blockingFirst()
		
		val genreObservable = provider.getGenre(genres[0].id)
		val genre = genreObservable.blockingFirst()
		
		Assert.assertEquals(genres[0].title, genre.title)
	}
	
	@Test
	fun getGenres() {
		val genresObservable = provider.getGenres()
		genresObservable.test().assertNoErrors()
	}
	
	@Test
	fun getAudios() {
		val genres = provider.getGenres().blockingFirst()
		
		val audiosObservable = provider.getAudios(genres[0].id)
		audiosObservable.test().assertNoErrors()
	}
	
}