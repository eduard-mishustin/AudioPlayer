package magym.feature.genrelist

import io.reactivex.Observable
import magym.core.common.mvi.MviViewModel
import magym.core.data.domain.GenreRepository
import magym.feature.genrelist.mvi.GenreListAction
import magym.feature.genrelist.mvi.GenreListIntent
import magym.feature.genrelist.mvi.GenreListSubscription
import magym.feature.genrelist.mvi.GenreListViewState

internal class GenreListViewModel(
	private val repository: GenreRepository
) : MviViewModel<GenreListIntent, GenreListAction, GenreListViewState, GenreListSubscription>(GenreListViewState()) {
	
	override fun act(state: GenreListViewState, intent: GenreListIntent): Observable<GenreListAction> = when (intent) {
		GenreListIntent.LoadData -> repository.getGenres()
			.map<GenreListAction> { GenreListAction.GenresReceived(it) }
			.startWith(GenreListAction.LoadDataStarted)
			.onErrorReturn { GenreListAction.LoadDataFailed(it) }
	}
	
	override fun reduce(oldState: GenreListViewState, action: GenreListAction) = when (action) {
		GenreListAction.LoadDataStarted -> oldState.copy(
			isLoading = true
		)
		
		is GenreListAction.GenresReceived -> oldState.copy(
			isLoading = false,
			genres = action.genres
		)
		
		is GenreListAction.LoadDataFailed -> oldState.copy(
			isLoading = false
		)
	}
	
	override fun publishSubscription(action: GenreListAction, state: GenreListViewState) = when (action) {
		is GenreListAction.LoadDataFailed -> GenreListSubscription.RemoteRequestError(action.error)
		else -> super.publishSubscription(action, state)
	}
	
}