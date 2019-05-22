package magym.feature.genrelist.mvi

internal sealed class GenreListIntent {
	
	object LoadData : GenreListIntent()
	
}