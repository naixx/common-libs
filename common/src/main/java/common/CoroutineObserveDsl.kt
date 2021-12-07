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


