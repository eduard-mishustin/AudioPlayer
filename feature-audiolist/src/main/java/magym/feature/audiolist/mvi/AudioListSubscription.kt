package magym.feature.audiolist.mvi

sealed class AudioListSubscription {
	
	class RemoteRequestError(val error: Throwable) : AudioListSubscription()
	
}