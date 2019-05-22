package magym.feature.service

import io.reactivex.subjects.BehaviorSubject
import magym.core.data.data.entity.Audio

class AudioPlayerState {
	
	val audio: BehaviorSubject<Audio> = BehaviorSubject.create()
	
}