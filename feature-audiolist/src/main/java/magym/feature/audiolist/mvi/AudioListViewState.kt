package magym.feature.audiolist.mvi

import magym.core.data.data.entity.Audio

data class AudioListViewState(
	val isLoading: Boolean = false,
	val audios: List<Audio> = arrayListOf()
)