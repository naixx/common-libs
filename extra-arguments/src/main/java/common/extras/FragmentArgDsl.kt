@file:Suppress("NOTHING_TO_INLINE")
package common.extras

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

open class FragmentArg<T>(
        private inline val getter: Bundle.(String) -> T,
        private inline val setter: Bundle.(String, T) -> Unit
) {

    operator fun getValue(fragment: Fragment, property: KProperty<*>): T =
            (fragment.arguments ?: Bundle()).getter(property.name)

    operator fun setValue(fragment: Fragment, property: KProperty<*>, value: T) {
        if (fragment.arguments == null)
            fragment.arguments = Bundle()
        fragment.requireArguments().setter(property.name, value)
    }
}

inline fun <T : Serializable?> Fragment.serializable() = FragmentArg({ getSerializable(it) as T }, { name, value -> this.putSerializable(name, value) })
inline fun <T : Parcelable?> Fragment.parcelable() = FragmentArg({ getParcelable<T>(it) }, { name, value -> this.putParcelable(name, value) })
inline fun Fragment.int(defValue: Int = 0) = FragmentArg({ getInt(it, defValue) }, { name, value -> this.putInt(name, value) })
inline fun Fragment.long(defValue: Long = 0) = FragmentArg({ getLong(it, defValue) }, { name, value -> this.putLong(name, value) })
inline fun Fragment.float(defValue: Float = 0f) = FragmentArg({ getFloat(it, defValue) }, { name, value -> this.putFloat(name, value) })
inline fun Fragment.boolean(defValue: Boolean = false) = FragmentArg({ getBoolean(it, defValue) }, { name, value -> this.putBoolean(name, value) })
inline fun Fragment.string(defValue: String? = null) = FragmentArg({ getString(it, defValue) }, { name, value -> this.putString(name, value) })
inline fun Fragment.intArray() = FragmentArg({ getIntArray(it) }, { name, value -> this.putIntArray(name, value) })

inline fun <T : Fragment> T.arguments(block: FragmentArgDsl.(T) -> Unit) = this.apply {
    arguments = FragmentArgDsl(Bundle()).apply { block(this@arguments) }.bundle
}

class FragmentArgDsl(val bundle: Bundle) {

    inline operator fun KProperty0<Parcelable>.plusAssign(value: Parcelable) {
        bundle.putParcelable(name, value)
    }

    inline operator fun KProperty0<Boolean>.plusAssign(value: Boolean) {
        bundle.putBoolean(name, value)
    }

    inline operator fun KProperty0<String>.plusAssign(value: String) {
        bundle.putString(name, value)
    }

    inline operator fun KProperty0<Int>.plusAssign(value: Int) {
        bundle.putInt(name, value)
    }

    inline operator fun KProperty0<Long>.plusAssign(value: Long) {
        bundle.putLong(name, value)
    }

    inline operator fun KProperty0<Float>.plusAssign(value: Float) {
        bundle.putFloat(name, value)
    }

    inline operator fun KProperty0<IntArray>.plusAssign(value: IntArray) {
        bundle.putIntArray(name, value)
    }

    inline operator fun KProperty0<Serializable>.timesAssign(value: Serializable) {
        bundle.putSerializable(name, value)
    }
}
