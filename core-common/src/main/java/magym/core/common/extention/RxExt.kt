package magym.core.common.extention

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

fun <T : Any> ObservableSource<T>.toObservable() = Observable.wrap(this)!!
fun <T> T.toObservable() = Observable.just(this)

fun <T> Completable.map(item: () -> T) = andThen(Observable.just<T>(item.invoke()))

fun launch(callback: () -> Unit) {
	Completable.fromAction { callback.invoke() }
		.subscribeOn(Schedulers.io())
		.doOnError { it.log() }
		.subscribe()
}