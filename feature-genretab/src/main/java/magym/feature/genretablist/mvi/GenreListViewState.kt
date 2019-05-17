package magym.feature.genretablist.mvi

import magym.core.data.data.entity.Genre

data class GenreListViewState(
	val isLoading: Boolean = false,
	val genres: List<Genre> = arrayListOf(),
	val currentGenre: Genre = Genre(0, "")
)