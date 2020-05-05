package common

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import rx.Emitter
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

interface LiveDataObserveDsl : LifecycleOwner {
    fun <T> LiveData<T>.observe(observer: (T) -> Unit = {}) {
        this.observe(this@LiveDataObserveDsl, androidx.lifecycle.Observer { observer.invoke(it) })
    }
}

interface FragmentLiveDataObserveDsl : LifecycleOwner {

    fun getViewLifecycleOwner(): LifecycleOwner

    fun <T> LiveData<T>.observe(observer: (T) -> Unit = {}) {
        this.observe(getViewLifecycleOwner(), androidx.lifecycle.Observer { observer.invoke(it) })
    }
}

interface RxObserveDsl {

    fun getContext(): Context?

    fun <T> Observable<T>.observe(onNext: (T) -> Unit = {}): Subscription = this.observeOn(AndroidSchedulers.mainThread()).subscribe(
            onNext,
            getContext()?.handleError())
}

fun <T> LiveData<T>.toObservable(): Observable<T> = Observable.create<T>({ emitter ->
    val observer: (t: T) -> Unit = {
        emitter.onNext(it)
    }
    this@toObservable.observeForever(observer)
    emitter.setCancellation {
        this@toObservable.removeObserver(observer)
    }
}, Emitter.BackpressureMode.LATEST)

