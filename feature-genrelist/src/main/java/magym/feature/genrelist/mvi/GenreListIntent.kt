package magym.feature.genrelist.mvi

internal sealed class GenreListIntent {
	
	class LoadData(val filterQuery: String = "") : GenreListIntent()
	
}