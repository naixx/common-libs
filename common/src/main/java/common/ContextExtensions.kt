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

@file:Suppress("NOTHING_TO_INLINE")

package common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

var errorLogger: ((Throwable?) -> Unit)? = null

fun Context.handleError(vararg apiErrors: Pair<Int, Int>): (Throwable) -> Unit = handleError(apiErrors.toMap())

fun Context.handleError(apiErrors: Map<Int, Int>): (Throwable) -> Unit {
    val c = this.applicationContext
    return { throwable: Throwable? ->
        errorLogger?.invoke(throwable)
        val resId = (throwable?.cause as? ApiException)?.code?.let(apiErrors::get)
        resId?.let {
            c.toast(it)
        } ?: run {
            c.toast(throwable?.message ?: throwable.toString())
        }

    }
}

fun Fragment.handleError(): (Throwable) -> Unit = requireContext().handleError()
fun Fragment.handleError(apiErrors: Map<Int, Int>): (Throwable) -> Unit = requireContext().handleError(apiErrors)
fun Fragment.handleError(vararg apiErrors: Pair<Int, Int>): (Throwable) -> Unit = requireContext().handleError(*apiErrors)

fun Activity.toast(message: String) {
    runOnUiThread {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Activity.toast(@StringRes resId: Int) {
    runOnUiThread {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}

fun Context.toast(message: String) {
    main.post {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Context.toast(@StringRes resId: Int) {
    main.post {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}

inline fun <reified T: Activity> Context.startActivity(options: Bundle? = null, block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply { block() }, options)
}

inline fun <reified T: Activity> Activity.startActivity(
        requestCode: Int = -1,
        options: Bundle? = null,
        block: Intent.() -> Unit = {}
) {
    startActivityForResult(Intent(this, T::class.java).apply { block() }, requestCode, options)
}

inline fun <reified T: Activity> Fragment.startActivity(
        requestCode: Int = -1,
        options: Bundle? = null,
        block: Intent.() -> Unit = {}
) {
    startActivityForResult(Intent(this.context, T::class.java).apply { block() }, requestCode, options)
}

fun Activity.safelyStartIntent(intent: Intent, requestCode: Int = -1) {
    if (intent.resolveActivity(packageManager) != null) {
        when (requestCode) {
            -1 -> startActivity(intent)
            else -> startActivityForResult(intent, requestCode)
        }
    } else {
        handleError()(IllegalStateException("No application found to handle capture intent"))
    }
}

fun Context.drawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)
fun Context.color(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)
fun Context.inflater(): LayoutInflater = LayoutInflater.from(this)
