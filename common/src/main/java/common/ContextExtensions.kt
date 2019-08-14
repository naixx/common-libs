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

inline fun Context.handleError(): (Throwable) -> Unit = {
    errorLogger?.invoke(it)
}

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
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

inline fun <reified T : Activity> Context.startActivity(options: Bundle? = null, block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply { block() }, options)
}

inline fun <reified T : Activity> Activity.startActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    block: Intent.() -> Unit = {}
) {
    startActivityForResult(Intent(this, T::class.java).apply { block() }, requestCode, options)
}

inline fun <reified T : Activity> Fragment.startActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    block: Intent.() -> Unit = {}
) {
    startActivityForResult(Intent(this.context, T::class.java).apply { block() }, requestCode, options)
}

inline fun <reified T : Activity> Activity.startActivityWithoutAnimation(
    requestCode: Int = -1,
    options: Bundle? = null,
    block: Intent.() -> Unit = {}
) {
    startActivity<T>(requestCode, options, block)
    overridePendingTransition(0, 0)
}

fun Activity.finishWithoutAnimation() {
    finish()
    overridePendingTransition(0, 0)
}

fun Activity.safelyStartIntent(intent: Intent, requestCode: Int = -1) {
    if (intent.resolveActivity(packageManager) != null) {
        when (requestCode) {
            -1   -> startActivity(intent)
            else -> startActivityForResult(intent, requestCode)
        }
    } else {
        handleError()(IllegalStateException("No application found to handle capture intent"))
    }
}

fun Context.drawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)
fun Context.color(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)
fun Context.inflater(): LayoutInflater = LayoutInflater.from(this)
