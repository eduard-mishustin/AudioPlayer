package magym.core.common.extention

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableSource

fun <T : Any> ObservableSource<T>.toObservable() = Observable.wrap(this)!!

fun <T> T.toObservable() = Observable.just(this)

fun <T> Completable.map(item: () -> T) = andThen(Observable.just<T>(item.invoke()))