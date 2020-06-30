@file:Suppress("NOTHING_TO_INLINE")

package common

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

open class FragmentArg<T>(
        private inline val getter: Bundle.(String) -> T,
        private inline val setter: Bundle.(String, T) -> Unit
) {

    operator fun getValue(fragment: Fragment, property: KProperty<*>): T =
            fragment.arguments!!.getter(property.name)

    operator fun setValue(fragment: Fragment, property: KProperty<*>, value: T) =
            fragment.arguments!!.setter(property.name, value)
}

inline fun <T : Serializable?> Fragment.serializable() = FragmentArg({ getSerializable(it) as T }, { name, value -> this.putSerializable(name, value) })
inline fun <T : Parcelable?> Fragment.parcelable() = FragmentArg({ getParcelable<T>(it) }, { name, value -> this.putParcelable(name, value) })
inline fun Fragment.int(defValue: Int = 0) = FragmentArg({ getInt(it, defValue) }, { name, value -> this.putInt(name, value) })
inline fun Fragment.boolean(defValue: Boolean = false) = FragmentArg({ getBoolean(it, defValue) }, { name, value -> this.putBoolean(name, value) })
inline fun <T : Serializable?> Fragment.string(defValue: String? = null) = FragmentArg({ getString(it, defValue) }, { name, value -> this.putString(name, value) })

inline fun <T : Fragment> T.arguments(block: FragmentArgDsl.() -> Unit) = this.apply {
    arguments = FragmentArgDsl(Bundle()).apply(block).bundle
}

class FragmentArgDsl(val bundle: Bundle) {
    operator fun <F : Fragment, T> KProperty1<F, T?>.plusAssign(value: T) {
        when (value) {
            is Parcelable   -> bundle.putParcelable(name, value)
            is Serializable -> bundle.putSerializable(name, value)
            is Int          -> bundle.putInt(name, value)
            is Boolean      -> bundle.putBoolean(name, value)
            is String       -> bundle.putString(name, value)
            is Long         -> bundle.putLong(name, value)
            else            -> throw IllegalArgumentException()
        }
    }
}
