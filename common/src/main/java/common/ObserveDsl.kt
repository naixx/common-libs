package common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

interface LiveDataObserveDsl : LifecycleOwner {
    fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        this.observe(this@LiveDataObserveDsl, androidx.lifecycle.Observer { observer.invoke(it) })
    }
}
