package magym.feature.audiosearch

import io.reactivex.Completable
import io.reactivex.Observable
import magym.core.common.mvi.MviViewModel
import magym.core.data.domain.repository.AudioRepository
import magym.feature.audiosearch.mvi.AudioSearchAction
import magym.feature.audiosearch.mvi.AudioSearchIntent
import magym.feature.audiosearch.mvi.AudioSearchSubscription
import magym.feature.audiosearch.mvi.AudioSearchViewState
import magym.feature.service.AudioPlayerState

class AudioSearchViewModel(
	private val repository: AudioRepository,
	private val audioPlayerState: AudioPlayerState
) : MviViewModel<AudioSearchIntent, AudioSearchAction, AudioSearchViewState, AudioSearchSubscription>(
	AudioSearchViewState()
) {
	
	override fun act(state: AudioSearchViewState, intent: AudioSearchIntent): Observable<out AudioSearchAction> =
		when (intent) {
			is AudioSearchIntent.LoadData -> if (intent.filterQuery.isNotEmpty()) { // todo
				repository.getAudios(filterQuery = intent.filterQuery)
					.asFlowSource(AudioSearchIntent.LoadData::class)
					.map<AudioSearchAction> { AudioSearchAction.AudiosReceived(it) }
					.startWith(AudioSearchAction.LoadDataStarted)
					.onErrorReturn { AudioSearchAction.LoadDataFailed(it) }
			} else {
				super.act(state, intent)
			}
			
			is AudioSearchIntent.PlayAudio -> Completable.fromAction { audioPlayerState.audio.onNext(intent.audio) }
				.toObservable()
		}
	
	override fun reduce(oldState: AudioSearchViewState, action: AudioSearchAction) = when (action) {
		AudioSearchAction.LoadDataStarted -> oldState.copy(
			isLoading = true
		)
		
		is AudioSearchAction.AudiosReceived -> oldState.copy(
			isLoading = false,
			audios = action.audios
		)
		
		is AudioSearchAction.LoadDataFailed -> oldState.copy(
			isLoading = false
		)
	}
	
	override fun publishSubscription(action: AudioSearchAction, state: AudioSearchViewState) = when (action) {
		is AudioSearchAction.LoadDataFailed -> AudioSearchSubscription.RemoteRequestError(action.error)
		else -> super.publishSubscription(action, state)
	}
	
}