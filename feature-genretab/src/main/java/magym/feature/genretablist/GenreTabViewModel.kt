package magym.feature.genretablist

import io.reactivex.Observable
import magym.core.common.extention.toObservable
import magym.core.common.mvi.MviViewModel
import magym.core.data.domain.repository.GenreRepository
import magym.feature.genretablist.mvi.GenreListAction
import magym.feature.genretablist.mvi.GenreListIntent
import magym.feature.genretablist.mvi.GenreListSubscription
import magym.feature.genretablist.mvi.GenreListViewState

internal class GenreTabViewModel(
	private val repository: GenreRepository
) : MviViewModel<GenreListIntent, GenreListAction, GenreListViewState, GenreListSubscription>(GenreListViewState()) {
	
	override fun act(state: GenreListViewState, intent: GenreListIntent): Observable<out GenreListAction> =
		when (intent) {
			GenreListIntent.LoadData -> repository.getGenres()
				.map<GenreListAction> { GenreListAction.GenresReceived(it) }
				.startWith(GenreListAction.LoadDataStarted)
				.onErrorReturn { GenreListAction.LoadDataFailed(it) }
			
			is GenreListIntent.ChangeCurrentGenre -> if (!state.isSearchMode) {
				GenreListAction.ChangeCurrentGenre(intent.index).toObservable()
			} else {
				super.act(state, intent)
			}
			
			is GenreListIntent.ChangeSearchMode -> if (intent.isSearchMode != state.isSearchMode) {
				GenreListAction.ChangeSearchMode(intent.isSearchMode, intent.currentPageItem).toObservable()
			} else {
				super.act(state, intent)
			}
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
		
		is GenreListAction.ChangeCurrentGenre -> oldState.copy(
			currentGenre = oldState.genres[action.index]
		)
		
		is GenreListAction.ChangeSearchMode -> oldState.copy(
			currentPageItem = if (action.isSearch) action.currentPageItem else oldState.currentPageItem,
			isSearchMode = action.isSearch
		)
	}
	
	override fun publishSubscription(action: GenreListAction, state: GenreListViewState) = when (action) {
		is GenreListAction.LoadDataFailed -> GenreListSubscription.RemoteRequestError(action.error)
		else -> super.publishSubscription(action, state)
	}
	
}