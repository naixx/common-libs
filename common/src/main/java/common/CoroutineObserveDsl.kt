package common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

interface FragmentFlowObserveDsl : CoroutineObserveDsl {

    override val observeScope: CoroutineScope
        get() = getViewLifecycleOwner().lifecycleScope

    fun getViewLifecycleOwner(): LifecycleOwner
}

interface FlowObserveDsl : CoroutineObserveDsl {

    override val observeScope: CoroutineScope
        get() = getLifecycle().coroutineScope

    fun getLifecycle(): Lifecycle
}

interface CoroutineObserveDsl {

    val exceptionHandler: CoroutineExceptionHandler

    val observeScope: CoroutineScope

    fun <T> Flow<T>.observe(onNext: suspend (T) -> Unit = {}): Job {
        return observeScope.launch(exceptionHandler) {
            coroutineScope {
                this@observe.collect { onNext(it) }
            }
        }
    }

    fun CoroutineObserveDsl.launch(onNext: suspend () -> Unit): Job {
        return observeScope.launch(exceptionHandler) {
            coroutineScope {
                onNext()
            }
        }
    }
}


