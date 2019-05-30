package magym.feature.audiosearch.mvi

import androidx.paging.PagedList
import magym.core.data.data.entity.Audio

sealed class AudioSearchAction {
	
	object LoadDataStarted : AudioSearchAction()
	
	class AudiosReceived(val audios: PagedList<Audio>) : AudioSearchAction()
	
	class LoadDataFailed(val error: Throwable) : AudioSearchAction()
	
}