package magym.feature.audiolist.mvi

sealed class AudioListIntent {
	
	class LoadData(val genreId: Int) : AudioListIntent()

    class PlayAudio(val audioUrl: String) : AudioListIntent()
	
}