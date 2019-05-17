package magym.feature.genrelist.mvi

sealed class GenreListSubscription {
	
	class RemoteRequestError(val error: Throwable) : GenreListSubscription()
	
}