package magym.feature.service.util

internal interface IMediaSessionCallback {
	
	fun onPlay()
	
	fun onPause()
	
	fun onStop()
	
	fun onSkipToPrevious()
	
	fun onSkipToNext()
	
}