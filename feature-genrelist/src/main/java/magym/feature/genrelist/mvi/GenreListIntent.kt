package magym.feature.genrelist.mvi

sealed class GenreListIntent {
	
	object LoadData : GenreListIntent()
	
}