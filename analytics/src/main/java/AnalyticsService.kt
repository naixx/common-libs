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

package common.analytics

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

annotation class Param(val value: String)

typealias AnalyticsCall = (name: String, parameterNames: List<String>, args: List<Any>) -> Unit
typealias AnalyticsConverter = (Any) -> Any

@PublishedApi
internal var analyticsConverter: AnalyticsConverter = { it }

inline fun <reified T> createAdapter(noinline analyticsCall: AnalyticsCall) = createAdapter(T::class.java, analyticsCall)

@PublishedApi
internal fun <T> createAdapter(impl: Class<T>, analyticsCall: AnalyticsCall): T {
    val serviceMethodCache = ConcurrentHashMap<Method, CallableMethod>()
    return Proxy.newProxyInstance(impl.classLoader, arrayOf(impl), object : InvocationHandler {
        override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any? {
            val a: Array<out Any>? = args?.map(analyticsConverter)?.toTypedArray()
            val def = tryDefault(impl, method)
            if (def != null)
                return if (args != null)
                    def.invoke(this, proxy, *args)
                else def.invoke(this, proxy)

            val callableMethod = serviceMethodCache.getOrPut(method) {
                val parameters = method.parameterAnnotations.flatMap { it.filterIsInstance(Param::class.java).map { it.value } }
                CallableMethod(method.name, parameters)
            }
            analyticsCall.invoke(callableMethod.name, callableMethod.parameterNames, a.orEmpty().toList())
            return null
        }
    }) as T
}

inline fun <reified T> createAggregate(vararg impls: T, noinline converter: AnalyticsConverter = { it }): T {
    analyticsConverter = converter
    return Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _: Any?, method: Method, args: Array<out Any>? ->
        impls.forEach {
            if (args != null)
                method.invoke(it, *args)
            else
                method.invoke(it)
        }
        null
    } as T
}

private fun tryDefault(impl: Class<*>, method: Method): Method? =
    impl
        .declaredClasses.filter { it.name.contains("DefaultImpls") }
        .flatMap { it.declaredMethods.toList() }
        .firstOrNull { it.name == method.name && it.parameterTypes.drop(1).toTypedArray().contentEquals(method.parameterTypes) }

private data class CallableMethod(val name: String, val parameterNames: List<String>)
