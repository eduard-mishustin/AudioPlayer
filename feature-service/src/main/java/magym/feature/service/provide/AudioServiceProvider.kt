package magym.feature.service.provide

internal class AudioServiceProvider(private val proxy: AudioServiceProxy) : AudioServiceRepository {

    private val repository: AudioServiceRepository? get() = proxy.service

    override fun playAudio(audioUrl: String) {
        repository?.playAudio(audioUrl)
    }

}