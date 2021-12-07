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

import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.schedulers.Schedulers
import rx.schedulers.Schedulers.io
import rx.subscriptions.CompositeSubscription

operator fun CompositeSubscription.plusAssign(s: Subscription) {
    this.add(s)
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Observable<T>.observeOnMain(): Observable<T> = this.observeOn(mainThread())

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Observable<T>.subscribeOnIo(): Observable<T> = this.subscribeOn(io())

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Observable<T>.io2main(): Observable<T> = this.subscribeOn(Schedulers.io()).observeOn(mainThread())

internal val main = Handler(Looper.getMainLooper())
fun <T> Observable<T>.withProgress(context: Context, cancellable: Boolean = false): Observable<T> {
    val p = ProgressDialog(context)
    p.setCancelable(cancellable)
    return this.observeOnMain()
            .doOnSubscribe { main.post { p.show() } }
            .doOnTerminate { main.post { p.dismiss() } }
}

fun <T> Observable<T>.withProgress(view: View): Observable<T> {
    return this.observeOnMain()
            .doOnSubscribe { view.post { view.visibility = View.VISIBLE } }
            .doOnTerminate { view.post { view.visibility = View.GONE } }
}
