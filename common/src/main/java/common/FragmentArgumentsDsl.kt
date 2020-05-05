@file:Suppress("NOTHING_TO_INLINE")

package common

import android.os.Bundle
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.reflect.KProperty

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
inline fun Fragment.int(defValue: Int = 0) = FragmentArg({ getInt(it, defValue) }, { name, value -> this.putInt(name, value) })
inline fun Fragment.boolean(defValue: Boolean = false) = FragmentArg({ getBoolean(it, defValue) }, { name, value -> this.putBoolean(name, value) })
inline fun <T : Serializable?> Fragment.string(defValue: String? = null) = FragmentArg({ getString(it, defValue) }, { name, value -> this.putString(name, value) })

inline fun <T : Fragment> T.arguments(block: Bundle.() -> Unit) = this.apply {
    arguments = Bundle().apply(block)
}
