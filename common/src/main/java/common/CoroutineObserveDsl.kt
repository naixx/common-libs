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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Uses view lifecycle scope
 */

context(V) private inline fun <V, T> Flow<T>.observeInternal(
    crossinline onNext: suspend (T) -> Unit = {},
    scope: CoroutineScope,
    customContext: CoroutineContext = EmptyCoroutineContext
): Job =
    scope.launch(customContext) {
        coroutineScope {
            collect { onNext(it) }
        }
    }

private inline fun launchInternal(
    crossinline onNext: suspend () -> Unit,
    scope: CoroutineScope,
    customContext: CoroutineContext = EmptyCoroutineContext
): Job = scope.launch(customContext) {
    coroutineScope {
        onNext()
    }
}

context(LifecycleOwner)
fun <T> Flow<T>.observe(onNext: suspend (T) -> Unit = {}) = observeInternal(onNext, lifecycleScope)

/**
 * Uses view lifecycle scope
 */
context(Fragment)
fun <T> Flow<T>.observe(onNext: suspend (T) -> Unit = {}) = observeInternal(onNext, viewLifecycleOwner.lifecycleScope)

context(ViewModel)
fun <T> Flow<T>.observe(onNext: suspend (T) -> Unit = {}) = observeInternal(onNext, viewModelScope)

fun LifecycleOwner.launch(onNext: suspend () -> Unit): Job = launchInternal(onNext, lifecycleScope)

/**
 * Uses view lifecycle scope
 */
fun Fragment.launch(onNext: suspend () -> Unit): Job = launchInternal(onNext, viewLifecycleOwner.lifecycleScope)
fun ViewModel.launch(onNext: suspend () -> Unit): Job = launchInternal(onNext, viewModelScope)

//--------- error handling -----------

interface CoroutineExceptionHandlerProvider {
    val exceptionHandler: CoroutineExceptionHandler
}

context(V, CoroutineExceptionHandlerProvider) private inline fun <V, T> Flow<T>.observeInternalWithExceptionHandling(
    crossinline onNext: suspend (T) -> Unit = {},
    scope: CoroutineScope
): Job = observeInternal(onNext, scope, exceptionHandler)

context(CoroutineExceptionHandlerProvider) private inline fun <V> V.launchInternalWithExceptionHandling(
    crossinline onNext: suspend () -> Unit,
    scope: CoroutineScope
): Job = launchInternal(onNext, scope, exceptionHandler)

context(ViewModel, CoroutineExceptionHandlerProvider)
fun <T> Flow<T>.observe(onNext: suspend (T) -> Unit = {}): Job = observeInternalWithExceptionHandling<ViewModel, T>(onNext, viewModelScope)

context(Fragment, CoroutineExceptionHandlerProvider)
fun <T> Flow<T>.observe(onNext: suspend (T) -> Unit = {}): Job =
    observeInternalWithExceptionHandling<Fragment, T>(onNext, viewLifecycleOwner.lifecycleScope)

context(LifecycleOwner, CoroutineExceptionHandlerProvider)
fun <T> Flow<T>.observe(onNext: suspend (T) -> Unit = {}): Job =
    observeInternalWithExceptionHandling<LifecycleOwner, T>(onNext, lifecycleScope)

context(CoroutineExceptionHandlerProvider)
fun ViewModel.launch(onNext: suspend () -> Unit = {}): Job = launchInternalWithExceptionHandling(onNext, viewModelScope)

context(CoroutineExceptionHandlerProvider)
fun Fragment.launch(onNext: suspend () -> Unit = {}): Job =
    launchInternalWithExceptionHandling(onNext, viewLifecycleOwner.lifecycleScope)

context(CoroutineExceptionHandlerProvider)
fun LifecycleOwner.launch(onNext: suspend () -> Unit = {}): Job =
    launchInternalWithExceptionHandling(onNext, lifecycleScope)
