package magym.feature.service.provide

import magym.core.data.data.entity.Audio

interface AudioServiceRepository {
	
	fun playAudio(audio: Audio)
	
}