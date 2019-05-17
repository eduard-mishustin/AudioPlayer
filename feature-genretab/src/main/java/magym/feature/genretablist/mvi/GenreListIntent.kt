package magym.feature.genretablist.mvi

sealed class GenreListIntent {
	
	object LoadData : GenreListIntent()
	
	class ChangeCurrentGenre(val index: Int) : GenreListIntent()
	
}