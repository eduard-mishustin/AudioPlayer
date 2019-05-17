package magym.core.common

interface AudioPlayerNavigation {
	
	fun toAudioList(genreId: Int)
	
	fun toAudioDetail(audioId: Int)
	
	fun onBackPressed()
	
}