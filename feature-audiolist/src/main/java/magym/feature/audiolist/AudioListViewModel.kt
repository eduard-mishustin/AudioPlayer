package magym.feature.audiolist

import io.reactivex.Completable
import io.reactivex.Observable
import magym.core.common.mvi.MviViewModel
import magym.core.data.domain.repository.AudioRepository
import magym.feature.audiolist.mvi.AudioListAction
import magym.feature.audiolist.mvi.AudioListIntent
import magym.feature.audiolist.mvi.AudioListSubscription
import magym.feature.audiolist.mvi.AudioListViewState
import magym.feature.service.AudioPlayerState

class AudioListViewModel(
	private val repository: AudioRepository,
	private val audioPlayerState: AudioPlayerState
) : MviViewModel<AudioListIntent, AudioListAction, AudioListViewState, AudioListSubscription>(AudioListViewState()) {
	
	override fun act(state: AudioListViewState, intent: AudioListIntent): Observable<out AudioListAction> =
		when (intent) {
			is AudioListIntent.LoadData -> repository.getAudios(intent.genreId, intent.filterQuery)
				.map<AudioListAction> { AudioListAction.AudiosReceived(it) }
				.startWith(AudioListAction.LoadDataStarted)
				.onErrorReturn { AudioListAction.LoadDataFailed(it) }
			
			is AudioListIntent.PlayAudio -> Completable.fromAction { audioPlayerState.audio.onNext(intent.audio) }
				.andThen(super.act(state, intent))
		}
	
	override fun reduce(oldState: AudioListViewState, action: AudioListAction) = when (action) {
		AudioListAction.LoadDataStarted -> oldState.copy(
			isLoading = true
		)
		
		is AudioListAction.AudiosReceived -> oldState.copy(
			isLoading = action.audios.isEmpty(),
			audios = action.audios
		)
		
		is AudioListAction.LoadDataFailed -> oldState.copy(
			isLoading = false
		)
	}
	
	override fun publishSubscription(action: AudioListAction, state: AudioListViewState) = when (action) {
		is AudioListAction.LoadDataFailed -> AudioListSubscription.RemoteRequestError(action.error)
		else -> super.publishSubscription(action, state)
	}
	
}