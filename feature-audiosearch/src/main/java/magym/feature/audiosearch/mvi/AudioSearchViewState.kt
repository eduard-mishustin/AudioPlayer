package magym.feature.audiosearch.mvi

import androidx.paging.PagedList
import magym.core.data.data.entity.Audio

data class AudioSearchViewState(
	val isLoading: Boolean = false,
	val audios: PagedList<Audio>? = null
)