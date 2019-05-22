package magym.feature.genretablist.mvi

internal sealed class GenreListIntent {
	
	object LoadData : GenreListIntent()
	
	class ChangeCurrentGenre(val index: Int) : GenreListIntent()
	
}