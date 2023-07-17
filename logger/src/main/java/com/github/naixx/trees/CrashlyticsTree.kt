package com.github.naixx.trees

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.L

public class CrashlyticsTree : L.DebugTree() {

    private enum class LogLevel(val priority: Int) {
        VERBOSE(2), DEBUG(3), INFO(4), WARNING(5), ERROR(6)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val logLevel = LogLevel.values().firstOrNull { it.priority == priority } ?: LogLevel.DEBUG
        val info = getInfo(Throwable().stackTrace)
        val message = "${logLevel.name} [$tag] $message >> ${t?.message} >> $info"
        FirebaseCrashlytics.getInstance().log(message)
        if (t != null) {
            FirebaseCrashlytics.getInstance().recordException(t)
        }
    }

    private fun getInfo(stackTrace: Array<StackTraceElement>): String? {
        val s1 = stackTrace[4]
        val s2 = stackTrace[5]
        return String.format("%s (%s:%d) in ", methodName(s2), file(s2), line(s2)) + String.format(
            "%s (%s:%d)",
            methodName(s1),
            file(s1),
            line(s1)
        )
    }

    private fun methodName(stackTrace: StackTraceElement): String? {
        return stackTrace.methodName
    }

    private fun line(stackTrace: StackTraceElement): Int {
        return stackTrace.lineNumber
    }

    private fun file(stackTrace: StackTraceElement): String? {
        return stackTrace.fileName
    }

    private fun httpErrorMessage(e: Throwable?): String? {
//        if (e instanceof HttpException) {
        //for validation errors we want to see all fields, not just message. So information is not lost
//            return ErrorHandler.getErrorBody((HttpException) e);
//        }
        return e?.message
    }
}

