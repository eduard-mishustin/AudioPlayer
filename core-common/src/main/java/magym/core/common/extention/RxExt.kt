package magym.core.common.extention

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

fun <T : Any> ObservableSource<T>.toObservable() = Observable.wrap(this)!!

fun <T> T.toObservable(): Observable<out T> = Observable.just(this)

/**
 * Аналог функции [Observable.map], но только для [Completable]
 */
fun <T> Completable.map(item: () -> T) = andThen(Observable.just<T>(item.invoke()))

fun <T> Observable<T>.subscribeOnIo() {
	subscribeOn(Schedulers.io()).subscribe()
}

fun launch(job: () -> Unit) {
	Observable.fromCallable {
		job.invoke()
	}.subscribeOnIo()
}

@CheckReturnValue
fun <T> async(job: () -> T): Observable<T> {
	return Observable.fromCallable {
		job.invoke()
	}.subscribeOn(Schedulers.io())
}

@CheckReturnValue
fun <T : Any> Observable<T>.awaitUI(jobUI: (T) -> Unit): Disposable {
	return observeOn(AndroidSchedulers.mainThread())
		.subscribeBy(onNext = jobUI)
}