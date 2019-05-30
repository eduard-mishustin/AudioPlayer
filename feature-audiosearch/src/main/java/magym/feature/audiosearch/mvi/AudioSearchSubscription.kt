package magym.feature.audiosearch.mvi

sealed class AudioSearchSubscription {
	
	class RemoteRequestError(val error: Throwable) : AudioSearchSubscription()
	
}