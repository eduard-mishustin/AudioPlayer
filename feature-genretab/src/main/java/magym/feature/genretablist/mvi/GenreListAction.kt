package magym.feature.genretablist.mvi

import magym.core.data.data.entity.Genre

internal sealed class GenreListAction {
	
	object LoadDataStarted : GenreListAction()
	
	class GenresReceived(val genres: List<Genre>) : GenreListAction()
	
	class LoadDataFailed(val error: Throwable) : GenreListAction()
	
	class ChangeCurrentGenre(val index: Int) : GenreListAction()
	
}