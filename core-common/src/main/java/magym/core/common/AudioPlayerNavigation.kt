package magym.core.common

interface AudioPlayerNavigation {
	
	fun toGenreTab(genreId: Int)
	
	fun toAudioSearch()
	
	fun toAudioDetail(audioId: Int)
	
	fun onBackPressed()
	
}