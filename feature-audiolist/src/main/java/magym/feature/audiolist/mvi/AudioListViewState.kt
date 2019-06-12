package magym.feature.audiolist.mvi

import androidx.paging.PagedList
import magym.core.data.data.entity.Audio

data class AudioListViewState(
	val isLoading: Boolean = false,
	val audios: PagedList<Audio>? = null
)