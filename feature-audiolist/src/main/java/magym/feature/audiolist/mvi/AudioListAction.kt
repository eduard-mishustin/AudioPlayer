package magym.feature.audiolist.mvi

import magym.core.data.data.entity.Audio

sealed class AudioListAction {
	
	object LoadDataStarted : AudioListAction()
	
	class AudiosReceived(val audios: List<Audio>) : AudioListAction()
	
	class LoadDataFailed(val error: Throwable) : AudioListAction()
	
}