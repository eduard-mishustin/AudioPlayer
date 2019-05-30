package magym.core.data.domain.boundary

import androidx.paging.PagedList
import magym.core.common.extention.launch
import magym.core.data.data.base.dao.AudioDao
import magym.core.data.data.base.dao.AudioGenreDao
import magym.core.data.data.entity.Audio
import magym.core.data.data.entity.AudioGenre
import magym.core.data.data.parser.AudioParserApi

internal class AudioBoundaryCallback(
	private val audioDao: AudioDao,
	private val audioGenreDao: AudioGenreDao,
	private val parser: AudioParserApi,
	private val genreId: Int,
	private val filterQuery: String
) : PagedList.BoundaryCallback<Audio>() {
	
	// todo add retry by callback
	
	private var lastIndex = 0
	
	override fun onZeroItemsLoaded() {
		loadData()
	}
	
	override fun onItemAtEndLoaded(itemAtEnd: Audio) {
		lastIndex += COUNT_VIDEOS_ON_PAGE
		loadData()
	}
	
	private fun loadData() {
		launch {
			val audios =
				if (filterQuery.isEmpty()) (parser.getAudios(genreId, lastIndex))
				else parser.searchAudios(filterQuery, lastIndex)
			
			val keys = audios.map { AudioGenre("${genreId}_${it.id}", it.id, genreId) }
			
			audioGenreDao.insert(keys)
			audioDao.insert(audios)
		}
	}
	
	private companion object {
		private const val COUNT_VIDEOS_ON_PAGE = 48
	}
	
}