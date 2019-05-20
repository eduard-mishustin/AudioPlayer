package magym.feature.service.provide

import magym.core.data.data.entity.Audio

internal class AudioServiceProvider(private val proxy: AudioServiceProxy) : AudioServiceRepository {
	
	private val repository: AudioServiceRepository? get() = proxy.service
	
	override fun playAudio(audio: Audio) {
		repository?.playAudio(audio)
	}
	
}