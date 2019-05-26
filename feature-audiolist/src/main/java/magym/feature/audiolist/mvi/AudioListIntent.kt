package magym.feature.audiolist.mvi

import magym.core.data.data.entity.Audio

sealed class AudioListIntent {
	
	class LoadData(val genreId: Int, val filterQuery: String = "") : AudioListIntent()
	
	class PlayAudio(val audio: Audio) : AudioListIntent()
	
}