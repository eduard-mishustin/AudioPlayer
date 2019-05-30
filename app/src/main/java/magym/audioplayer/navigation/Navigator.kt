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
	
	
	override fun toGenreTab(genreId: Int) {
		navController?.navigateWithSameCheck(
			R.id.genreTabFragment,
			bundleOf(KEY_GENRE_ID to genreId)
		)
	}
	
	override fun toAudioSearch() {
		navController?.navigateWithSameCheck(R.id.audioSearchFragment)
	}
	
	override fun toAudioDetail(audioId: Int) {
		/*navController?.navigateWithSameCheck(
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
		
		private fun NavController.navigateWithSameCheck(
			@IdRes screenId: Int,
			args: Bundle? = null,
			navOptions: NavOptions? = null
		) {
			val currentScreenId = currentDestination?.id
			
			if (currentScreenId != screenId) {
				navigate(screenId, args, navOptions)
			}
		}
		
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