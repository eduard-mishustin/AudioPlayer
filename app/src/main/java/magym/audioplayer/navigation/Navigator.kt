package magym.audioplayer.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import magym.audioplayer.R
import magym.core.common.AudioPlayerNavigation
import magym.core.common.KEY_GENRE_ID

internal class Navigator : AudioPlayerNavigation {
	
	private var navController: NavController? = null
	
	
	override fun toAudioList(genreId: Int) {
		navController?.navigateWithPop(
			R.id.genreTabFragment,
			bundleOf(Pair(KEY_GENRE_ID, genreId))
		)
	}
	
	override fun toAudioDetail(audioId: Int) {
		/*navController?.navigateWithPop(
			R.id.,
			bundleOf(Pair(KEY_AUDIO_ID, audioId))
		)*/
	}
	
	override fun onBackPressed() {
		navController?.navigateUp()
	}
	
	
	fun bind(navController: NavController) {
		this.navController = navController
	}
	
	fun unbind() {
		navController = null
	}
	
	
	private companion object {
		
		private fun NavController.navigateWithPop(
			@IdRes screenId: Int,
			args: Bundle? = null,
			navOptions: NavOptions? = null
		) {
			val currentScreenId = currentDestination?.id
			
			if (currentScreenId != screenId) {
				if (popBackStack(screenId, false)) return
				navigate(screenId, args, navOptions)
			}
		}
		
	}
}