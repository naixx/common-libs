/*
 * Copyright 2021 Rostislav Chekan
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package common

import android.content.Context
import androidx.lifecycle.*
import rx.Emitter
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

interface LiveDataObserveDsl : LifecycleOwner {
    fun <T> LiveData<T>.observe(observer: (T) -> Unit = {}) {
        this.observe(this@LiveDataObserveDsl, androidx.lifecycle.Observer { observer(it) })
    }
}

interface FragmentLiveDataObserveDsl : LifecycleOwner {

    fun getViewLifecycleOwner(): LifecycleOwner

    fun <T> LiveData<T>.observe(observer: (T) -> Unit = {}) {
        this.observe(getViewLifecycleOwner(), androidx.lifecycle.Observer { observer(it) })
    }

    fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit = {}) {
        this.observe(getViewLifecycleOwner(), object : Observer<T> {
            override fun onChanged(t: T) {
                observer(t)
                removeObserver(this)
            }
        })
    }
}

interface RxObserveDsl : LifecycleOwner {

    fun getContext(): Context?

    fun <T> Observable<T>.withProgress(cancellable: Boolean = false) = this.withProgress(getContext()!!, cancellable)

    fun <T> Observable<T>.observe(onNext: (T) -> Unit = {}): Subscription = observe(onNext, *emptyArray())

    /**
     * A convenience method to show toasts for backend api error codes. Does not respect fragment's view lifecycle
     */
    fun <T> Observable<T>.observe(onNext: (T) -> Unit = {}, vararg apiErrors: Pair<Int, Int>): Subscription {
        val subscription = this
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        onNext,
                        getContext()?.handleError(*apiErrors))
        this@RxObserveDsl.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY)
                    subscription.unsubscribe()
            }
        })
        return subscription
    }
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

