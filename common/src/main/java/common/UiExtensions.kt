@file:JvmName("UI")

package common

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.transition.Transition
import androidx.transition.TransitionManager

fun View.onGlobalLayoutOnce(runnable: () -> Unit) {
    val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            runnable()
        }
    }
    viewTreeObserver.addOnGlobalLayoutListener(listener)
}

@JvmOverloads
@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): T {
    return LayoutInflater.from(this.context).inflate(resource, this, attachToRoot) as T
}

val Int.dp: Int
    @JvmName("dpToPx")
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dp: Float
    @JvmName("dpToPx")
    get() = (this * Resources.getSystem().displayMetrics.density)

//fun animateSlideOutTransition(activity: Activity) {
//    activity.overridePendingTransition(R.anim.no_anim, R.anim.slide_out_left)
//}

fun View.hideKeyBoard() {
    val imm = this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun TextView.showKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.beginDelayedTransition(transition: Transition? = null) = (this as? ViewGroup)?.let {
    TransitionManager.beginDelayedTransition(it, transition)
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun <T : View?> Activity.bindView(resId: Int) = lazy { this.findViewById<T>(resId) }

