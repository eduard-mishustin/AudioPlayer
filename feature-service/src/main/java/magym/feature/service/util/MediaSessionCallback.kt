package magym.feature.service.util

import android.support.v4.media.session.MediaSessionCompat

internal class MediaSessionCallback(
	private val callback: IMediaSessionCallback
) : MediaSessionCompat.Callback() {
	
	override fun onPlay() = callback.onPlay()
	
	override fun onPause() = callback.onPause()
	
	override fun onStop() = callback.onStop()
	
	override fun onSkipToPrevious() = callback.onSkipToPrevious()
	
	override fun onSkipToNext() = callback.onSkipToNext()
	
}