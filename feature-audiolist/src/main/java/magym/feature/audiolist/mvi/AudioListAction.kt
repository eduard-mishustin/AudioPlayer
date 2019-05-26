package magym.feature.audiolist.mvi

import androidx.paging.PagedList
import magym.core.data.data.entity.Audio

sealed class AudioListAction {
	
	object LoadDataStarted : AudioListAction()
	
	class AudiosReceived(val audios: PagedList<Audio>) : AudioListAction()
	
	class LoadDataFailed(val error: Throwable) : AudioListAction()
	
}