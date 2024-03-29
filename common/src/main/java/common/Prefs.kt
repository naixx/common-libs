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

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class PrefsDsl(private val name_: String? = null) {
    internal lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = if (!name_.isNullOrBlank()) context.getSharedPreferences(name_, Context.MODE_PRIVATE)
        else PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun PrefsDsl.int(defaultValue: Int = 0, key: String? = null): ReadWriteProperty<PrefsDsl, Int> =
            PrefPrimitiveDelegate(key, defaultValue, SharedPreferences::getInt, SharedPreferences.Editor::putInt)

    fun PrefsDsl.bool(defaultValue: Boolean = false, key: String? = null): ReadWriteProperty<PrefsDsl, Boolean> =
            PrefPrimitiveDelegate(key, defaultValue, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)

    fun PrefsDsl.long(defaultValue: Long = 0, key: String? = null): ReadWriteProperty<PrefsDsl, Long> =
            PrefPrimitiveDelegate(key, defaultValue, SharedPreferences::getLong, SharedPreferences.Editor::putLong)

    fun PrefsDsl.float(defaultValue: Float = 0f, key: String? = null): ReadWriteProperty<PrefsDsl, Float> =
            PrefPrimitiveDelegate(key, defaultValue, SharedPreferences::getFloat, SharedPreferences.Editor::putFloat)

    fun PrefsDsl.string(defaultValue: String = "", key: String? = null): ReadWriteProperty<PrefsDsl, String> =
            PrefPrimitiveDelegate(key, defaultValue, { key, defValue -> getString(key, defValue) ?: defaultValue }, SharedPreferences.Editor::putString)

    fun PrefsDsl.stringSet(defaultValue: Set<String> = emptySet(), key: String? = null): ReadWriteProperty<PrefsDsl, Set<String>> =
            PrefPrimitiveDelegate(key, defaultValue, { key, defValues -> getStringSet(key, defValues) ?: defaultValue }, SharedPreferences.Editor::putStringSet)

    inline fun <reified E : Enum<E>> PrefsDsl.enum(defaultValue: E, key: String? = null): ReadWriteProperty<PrefsDsl, E> =
            PrefPrimitiveDelegate(
                    key,
                    defaultValue,
                    { s: String, e: E -> enumValueOf(getString(s, null) ?: defaultValue.name) },
                    { s: String, e: E -> putString(s, e.name) }
            )
}

@PublishedApi
internal class PrefPrimitiveDelegate<T>(
        private val key: String?,
        private val defaultValue: T,
        val getter: SharedPreferences.(String, T) -> T,
        val setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
) : ReadWriteProperty<PrefsDsl, T> {
    override fun getValue(thisRef: PrefsDsl, property: KProperty<*>): T {
        return thisRef.sharedPreferences.getter(key ?: property.name, defaultValue)
    }

    override fun setValue(thisRef: PrefsDsl, property: KProperty<*>, value: T) =
            thisRef.sharedPreferences.edit().setter(key ?: property.name, value).apply()
}
