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
