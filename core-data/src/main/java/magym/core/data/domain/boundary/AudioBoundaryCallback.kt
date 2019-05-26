package magym.core.data.domain.boundary

import androidx.paging.PagedList
import magym.core.common.extention.launch
import magym.core.data.data.base.dao.AudioDao
import magym.core.data.data.entity.Audio
import magym.core.data.data.parser.AudioParserApi

internal class AudioBoundaryCallback(
	private val audioDao: AudioDao,
	private val parser: AudioParserApi,
	private val genreId: Int
) : PagedList.BoundaryCallback<Audio>(){
	
	override fun onZeroItemsLoaded() {
		launch { audioDao.insert(parser.getAudios(genreId)) }
	}
	
	override fun onItemAtEndLoaded(itemAtEnd: Audio) {
		launch { audioDao.insert(parser.getAudios(genreId, itemAtEnd.index + 1)) }
	}
	
}