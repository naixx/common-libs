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

@file:Suppress("NOTHING_TO_INLINE", "unused")

package common.extras

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

open class ActivityExtra<T>(
    private inline val getter: Intent.(String) -> T,
    private inline val setter: Intent.(String, T) -> Unit,
) {

    operator fun getValue(activity: Activity, property: KProperty<*>): T =
        (activity.intent ?: Intent()).getter(property.name)

    operator fun setValue(activity: Activity, property: KProperty<*>, value: T) {
        if (activity.intent == null)
            activity.intent = Intent()
        activity.intent.setter(property.name, value)
    }
}

inline fun <T : Serializable> Activity.extraSerializable(): ActivityExtra<T> =
    ActivityExtra({ getSerializableExtra(it) as T }, { name, value -> this.putExtra(name, value) })

inline fun <T : Parcelable?> Activity.extraParcelable() =
    ActivityExtra({ getParcelableExtra<T>(it) }, { name, value -> this.putExtra(name, value) })

inline fun Activity.extraInt(defValue: Int = 0) =
    ActivityExtra({ getIntExtra(it, defValue) }, { name, value -> this.putExtra(name, value) })

inline fun Activity.extraLong(defValue: Long = 0) =
    ActivityExtra({ getLongExtra(it, defValue) }, { name, value -> this.putExtra(name, value) })

inline fun Activity.extraFloat(defValue: Float = 0f) =
    ActivityExtra({ getFloatExtra(it, defValue) }, { name, value -> this.putExtra(name, value) })

inline fun Activity.extraBoolean(defValue: Boolean = false) =
    ActivityExtra({ getBooleanExtra(it, defValue) }, { name, value -> this.putExtra(name, value) })

inline fun Activity.extraString(defValue: String? = null) = ActivityExtra({
    getStringExtra(it) ?: defValue
}, { name, value -> this.putExtra(name, value) })

inline fun Activity.extraIntArray() = ActivityExtra({ getIntArrayExtra(it) }, { name, value -> this.putExtra(name, value) })

open class ActivityExtraDsl<A : Activity>(val bundle: Bundle) {

    inline operator fun KProperty1<A, Boolean>.plusAssign(value: Boolean) {
        bundle.putBoolean(name, value)
    }

    inline operator fun KProperty1<A, String>.plusAssign(value: String) {
        bundle.putString(name, value)
    }

    inline operator fun KProperty1<A, Int>.plusAssign(value: Int) {
        bundle.putInt(name, value)
    }

    inline operator fun KProperty1<A, Long>.plusAssign(value: Long) {
        bundle.putLong(name, value)
    }

    inline operator fun KProperty1<A, Float>.plusAssign(value: Float) {
        bundle.putFloat(name, value)
    }

    inline operator fun KProperty1<A, IntArray>.plusAssign(value: IntArray) {
        bundle.putIntArray(name, value)
    }
}

class ActivityExtraDslAny<A : Activity, T : Any?, U : Any?, V : Any?, Q : Any?>(bundle: Bundle) : ActivityExtraDsl<A>(bundle) {

    private inline fun <V> KProperty1<A, V>.put(value: V) {
        when (value) {
            null            -> bundle.putString(name, null)
            is Parcelable   -> bundle.putParcelable(name, value)
            is Serializable -> bundle.putSerializable(name, value)
            else            -> throw IllegalArgumentException("Value should be Parcelable or Serializable! $value")
        }
    }

    operator fun KProperty1<A, T>.plusAssign(value: T) {
        put(value)
    }

    @JvmName("timesAssign2")
    operator fun KProperty1<A, U>.plusAssign(value: U) {
        put(value)
    }

    @JvmName("timesAssign3")
    operator fun KProperty1<A, V>.plusAssign(value: V) {
        put(value)
    }

    @JvmName("timesAssign4")
    operator fun KProperty1<A, Q>.plusAssign(value: Q) {
        put(value)
    }
}

class ActivityExtraDslUnsafe<A : Activity, T : Any?, U : Any?, V : Any?, Q : Any?>(bundle: Bundle) : ActivityExtraDsl<A>(bundle) {

    private inline fun <V> KProperty1<A, V>.put(value: V) {
        when (value) {
            null            -> bundle.putString(name, null)
            is Parcelable   -> bundle.putParcelable(name, value)
            is Serializable -> bundle.putSerializable(name, value)
            else            -> throw IllegalArgumentException("Value should be Parcelable or Serializable! $value")
        }
    }

    operator fun KProperty1<A, T>.plusAssign(value: Parcelable) {
        put(value)
    }

    operator fun KProperty1<A, T>.timesAssign(value: Serializable) {
        put(value)
    }

    @JvmName("timesAssign2")
    operator fun KProperty1<A, U>.plusAssign(value: U) {
        put(value)
    }

    @JvmName("timesAssign3")
    operator fun KProperty1<A, V>.plusAssign(value: V) {
        put(value)
    }

    @JvmName("timesAssign4")
    operator fun KProperty1<A, Q>.plusAssign(value: Q) {
        put(value)
    }
}

@JvmName("extras0")
@CheckResult
inline fun <reified A : Activity> extrasUnsafe(block: ActivityExtraDslUnsafe<A, Any, Nothing, Nothing, Nothing>.() -> Unit = {}): ExtraStarter =
    ExtraStarter(ActivityExtraDslUnsafe<A, Any, Nothing, Nothing, Nothing>(Bundle()).apply { block() }.bundle, A::class.java)

@JvmName("extras1")
@CheckResult
inline fun <reified A : Activity, T1 : Any?> extras(block: ActivityExtraDslAny<A, T1, Nothing, Nothing, Nothing>.() -> Unit = {}): ExtraStarter =
    ExtraStarter(ActivityExtraDslAny<A, T1, Nothing, Nothing, Nothing>(Bundle()).apply { block() }.bundle, A::class.java)

@JvmName("extras2")
@CheckResult
inline fun <reified A : Activity, T1 : Any?, T2 : Any?> extras(block: ActivityExtraDslAny<A, T1, T2, Nothing, Nothing>.() -> Unit = {}): ExtraStarter =
    ExtraStarter(ActivityExtraDslAny<A, T1, T2, Nothing, Nothing>(Bundle()).apply { block() }.bundle, A::class.java)

@JvmName("extras3")
@CheckResult
inline fun <reified A : Activity, T1 : Any?, T2 : Any?, T3 : Any?> extras(block: ActivityExtraDslAny<A, T1, T2, T3, Nothing>.() -> Unit = {}): ExtraStarter =
    ExtraStarter(ActivityExtraDslAny<A, T1, T2, T3, Nothing>(Bundle()).apply { block() }.bundle, A::class.java)

@JvmName("extras4")
@CheckResult
inline fun <reified A : Activity, T1 : Any?, T2 : Any?, T3 : Any?, T4 : Any?> extras(block: ActivityExtraDslAny<A, T1, T2, T3, T4>.() -> Unit = {}): ExtraStarter =
    ExtraStarter(ActivityExtraDslAny<A, T1, T2, T3, T4>(Bundle()).apply { block() }.bundle, A::class.java)

class ExtraStarter @PublishedApi internal constructor(internal val bundle: Bundle, internal val clazz: Class<*>) {

    fun start(context: Context) {
        context.startActivity(intent(context).putExtras(bundle))
    }

    fun intent(context: Context): Intent = Intent(context, clazz).putExtras(bundle)
}

val Context.start: ExtraStarter.() -> Unit
    get() = {
        start(this@start)
    }

val Context.intent: ExtraStarter.() -> Unit
    get() = {
        intent(this@intent)
    }

val Activity.start: ExtraStarter.() -> Unit
    get() = {
        start(this@start)
    }

val Activity.startForResult: ExtraStarter.(requestCode: Int) -> Unit
    get() = { requestCode ->
        startActivityForResult(intent(this@startForResult), requestCode, null)
    }

val Fragment.start: ExtraStarter.() -> Unit
    get() = {
        start(requireContext())
    }

val Fragment.startForResult: ExtraStarter.(requestCode: Int) -> Unit
    get() = { requestCode ->
        startActivityForResult(intent(requireContext()), requestCode, null)
    }

val Fragment.intent: ExtraStarter.() -> Unit
    get() = {
        intent(requireContext())
    }
