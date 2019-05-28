package magym.core.common.mvi

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlin.reflect.KClass

abstract class MviViewModel<Intent : Any, Action : Any, State : Any, Subscription : Any>(
	initialState: State
) : ViewModel() {
	
	val subscription: ObservableSource<Subscription>
		get() = subscriptionSubject
	
	val state: ObservableSource<State>
		get() = stateSubject
	
	private val intentsSubject = PublishSubject.create<Intent>()
	private val subscriptionSubject = PublishSubject.create<Subscription>()
	private val stateSubject = BehaviorSubject.create<State>()
	
	private val flows = mutableMapOf<KClass<out Intent>, Observable<*>>()
	
	private val disposable = CompositeDisposable()
	
	init {
		stateSubject.onNext(initialState)
		
		disposable += intentsSubject
			.flatWithLatestFrom(state, ::onIntentReceived)
			.withLatestFrom(state, ::onActionReceived)
			.distinctUntilChanged()
			.subscribeBy(onNext = stateSubject::onNext)
	}
	
	override fun onCleared() {
		super.onCleared()
		disposable.clear()
	}
	
	
	fun postIntent(intent: Intent) = intentsSubject.onNext(intent)
	
	
	protected open fun act(state: State, intent: Intent): Observable<out Action> = Observable.empty()
	
	protected open fun reduce(oldState: State, action: Action): State = oldState
	
	protected open fun publishSubscription(action: Action, state: State): Subscription? = null
	
	
	protected fun <T : Any> Observable<T>.asFlowSource(intentType: KClass<out Intent>): Observable<T> {
		val isFlowLaunched = flows.containsKey(intentType)
		
		if (!isFlowLaunched) {
			flows[intentType] = SwitchableObservable(this)
		}
		
		@Suppress("UNCHECKED_CAST")
		val flow = flows[intentType] as Observable<T>
		
		flow.switchSource(this)
		
		return if (isFlowLaunched) Observable.empty() else flow
	}
	
	
	private fun onIntentReceived(intent: Intent, state: State) = act(state, intent)
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
	
	private fun onActionReceived(action: Action, oldState: State): State {
		val newState = reduce(oldState, action)
		val subscription = publishSubscription(action, newState)
		
		subscription?.let { subscriptionSubject.onNext(it) }
		
		return newState
	}
	
	
	private companion object {
		
		private inline fun <T, U, R> Observable<T>.flatWithLatestFrom(
			other: ObservableSource<U>,
			crossinline combiner: (T, U) -> ObservableSource<out R>
		): Observable<R> {
			return withLatestFrom(other, combiner).flatMap { it }
		}
		
	}
	
}