package magym.feature.genretablist.mvi

internal sealed class GenreListSubscription {
	
	class ChangePageSelected(val index: Int) : GenreListSubscription()
	
	class RemoteRequestError(val error: Throwable) : GenreListSubscription()
	
}