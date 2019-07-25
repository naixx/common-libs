@file:Suppress("NOTHING_TO_INLINE")

package common

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.KProperty

open class Extra<T>(
        private inline val getter: Intent.(String) -> T,
        private inline val setter: Intent.(String, T) -> Unit
) {

    operator fun getValue(activity: Activity, property: KProperty<*>): T =
            activity.intent.getter(property.name)

    operator fun setValue(activity: Activity, property: KProperty<*>, value: T) =
        activity.intent.setter(property.name, value)
}

inline fun Activity.string() = Extra(Intent::getStringExtra) { name, value -> putExtra(name, value) }
inline fun Activity.boolean(default: Boolean = false) = Extra({ getBooleanExtra(it, default) }, { name, value -> this.putExtra(name, value) })
inline fun <T : Parcelable?> Activity.parcelable() = Extra({ getParcelableExtra<T>(it) }, { name, value -> this.putExtra(name, value) })
inline fun <T : Serializable?> Activity.serializable() = Extra({ getSerializableExtra(it) as T }, { name, value -> this.putExtra(name, value) })
inline fun Activity.int(default: Int = 0) = Extra({ getIntExtra(it, default) }, { name, value -> this.putExtra(name, value) })
inline fun Activity.long(default: Long = 0) = Extra({ getLongExtra(it, default) }, { name, value -> this.putExtra(name, value) })
