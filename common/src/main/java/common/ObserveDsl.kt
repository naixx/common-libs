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

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import rx.Emitter
import rx.Observable

context(LifecycleOwner)
fun <T> LiveData<T>.observe(observer: (T) -> Unit = {}) {
    this.observe(this@LifecycleOwner) { observer(it) }
}

context(Fragment)
fun <T> LiveData<T>.observe(observer: (T) -> Unit = {}) {
    this.observe(viewLifecycleOwner) { observer(it) }
}

context(Fragment, LifecycleOwner)
fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit = {}) {
    this.observe(viewLifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer(t)
            removeObserver(this)
        }
    })
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

