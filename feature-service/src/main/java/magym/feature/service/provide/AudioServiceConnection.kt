package magym.feature.service.provide

import magym.feature.service.AudioService

interface AudioServiceConnection {
	
	fun serviceConnected(service: AudioService)
	
	fun serviceDisconnected()
	
}