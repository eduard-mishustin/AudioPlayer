package magym.core.data.data.parser

import magym.core.common.extention.stringDateToLong
import magym.core.data.BuildConfig.BASE_URL
import magym.core.data.BuildConfig.SHORT_BASE_URL
import magym.core.data.data.entity.Audio
import magym.core.data.data.entity.Genre
import magym.core.data.util.getDocument
import magym.core.data.util.second
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

internal class AudioParser : AudioParserApi {
	
	override fun getAudios(genreId: Int, page: Int) =
		getDocument(BASE_URL + GENRE_PATH + genreId + PAGES_PATH + page)
			.getAudiosOnPage()
	
	override fun searchAudios(filterQuery: String, page: Int) =
		getDocument(BASE_URL + SEARCH_PATH + PAGES_PATH + page + SEARCH_SUFFIX + filterQuery)
			.getAudiosOnPage()
	
	override fun getGenres() = getDocument(BASE_URL + GENRES_PATH)
		.getElementsByClass("content-new-link")
		.map {
			Genre(
				id = it.id,
				title = it.getElementsByClass("content-new__item-title").text()
			)
		}
	
	override fun getGenre(id: Int) = getDocument(BASE_URL + GENRE_PATH + id)
		.getElementsByClass("content-title")
		.first()
		.text()
		.removeSuffix(" музыка")
		.let { Genre(id, it) }
	
	
	private fun Document.getAudiosOnPage() = getElementsByClass("playlist-item")
		.map {
			Audio(
				id = it.id,
				title = it.getElementsByClass("playlist-item-title").text(),
				artist = it.getElementsByClass("playlist-item-subtitle").text(),
				duration = it.getElementsByClass("playlist-item-duration").text().stringDateToLong(),
				url = it.select("a").first().attr("data-url"),
				posterUrl = SHORT_BASE_URL + it.select("a").second()?.attr("data-img")
			)
		}
	
	private companion object {
		
		private const val GENRE_PATH = "genre/"
		private const val GENRES_PATH = "genres"
		
		private const val PAGES_PATH = "/start/"
		
		private const val SEARCH_PATH = "search"
		private const val SEARCH_SUFFIX = "?q="
		
		private val Element.id
			get() = select("a").first()
				.attr("href")
				.substringAfterLast('/')
				.toInt()
		
	}
	
}