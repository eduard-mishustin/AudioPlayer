package magym.feature.audiolist

import io.reactivex.subjects.BehaviorSubject

class SearchViewProvider {
	
	var textChanges: BehaviorSubject<CharSequence> = BehaviorSubject.create()
	
}