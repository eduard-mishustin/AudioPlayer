package magym.feature.genrelist.mvi

internal sealed class GenreListSubscription {
	
	class RemoteRequestError(val error: Throwable) : GenreListSubscription()
	
}