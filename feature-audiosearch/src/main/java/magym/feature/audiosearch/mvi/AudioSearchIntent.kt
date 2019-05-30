package magym.feature.audiosearch.mvi

import magym.core.data.data.entity.Audio

sealed class AudioSearchIntent {
	
	class LoadData(val filterQuery: String = "") : AudioSearchIntent()
	
	class PlayAudio(val audio: Audio) : AudioSearchIntent()
	
}