package magym.core.data.data.parser

import magym.core.data.data.entity.Audio
import magym.core.data.data.entity.Genre
import magym.core.data.util.BASE_URL
import magym.core.data.util.getDocument
import magym.core.data.util.second
import org.jsoup.nodes.Element

internal class AudioParser : AudioApi {
	
	private val Element.id
		get() = select("a").first()
			.attr("href")
			.substringAfterLast('/')
			.toInt()
	
	override fun getGenre(id: Int) = getDocument(BASE_URL + GENRE_URL + id)
		.getElementsByClass("content-title")
		.first()
		.text()
		.removeSuffix(" музыка")
		.let { Genre(id, it) }
	
	override fun getGenres() = getDocument(BASE_URL + GENRES_URL)
		.getElementsByClass("content-new-link")
		.map {
			val title = it.getElementsByClass("content-new__item-title").text()
			Genre(it.id, title)
		}

	override fun getAudios(genreId: Int) = getDocument(BASE_URL + GENRE_URL + genreId)
		.getElementsByClass("playlist-item")
		.map {
			val name = it.getElementsByClass("playlist-item-title").text()
			val group = it.getElementsByClass("playlist-item-subtitle").text()
			val url = it.select("a").first().attr("data-url")
			val coverPath = it.select("a").second()?.attr("data-img") ?: 0
			Audio(it.id, name, group, url, BASE_URL + coverPath)
		}

	private companion object {
		
		private const val GENRE_URL = "genre/"
		
		private const val GENRES_URL = "genres"
		
	}
	
}