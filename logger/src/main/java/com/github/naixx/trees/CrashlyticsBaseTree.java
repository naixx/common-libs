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

package com.github.naixx.trees;

import android.text.TextUtils;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.L;

public abstract class CrashlyticsBaseTree extends L.Tree {

    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("\\$\\d+$");

    protected enum LogLevel {
        VERBOSE(2),
        DEBUG(3),
        INFO(4),
        WARNING(5),
        ERROR(6);

        final int priority;

        LogLevel(int priority) {this.priority = priority;}
    }

//    @Override
//    public void d(String message, Object... args) {
//        log(LogLevel.DEBUG, createTag(), formatString(message, args));
//    }
//
//    @Override
//    public void d(Throwable t, String message, Object... args) {
//        log(LogLevel.DEBUG, createTag(), formatString(message, args), t);
//    }
//
//    @Override
//    public void v(String message, Object... args) {
//    }
//
//    @Override
//    public void v(Throwable t, String message, Object... args) {
//    }
//
//    @Override
//    public void i(String message, Object... args) {
//        log(LogLevel.INFO, createTag(), formatString(message, args));
//    }
//
//    @Override
//    public void i(Throwable t, String message, Object... args) {
//        log(LogLevel.INFO, createTag(), formatString(message, args), t);
//    }
//
//    @Override
//    public void w(String message, Object... args) {
//        log(LogLevel.WARNING, createTag(), formatString(message, args));
//    }
//
//    @Override
//    public void w(Throwable t, String message, Object... args) {
//        log(LogLevel.WARNING, createTag(), formatString(message, args), t);
//    }
//
//    @Override
//    public void e(String message, Object... args) {
//        log(LogLevel.ERROR, createTag(), formatString(message, args));
//    }
//
//    @Override
//    public void e(Throwable t, String message, Object... args) {
//        //usually it is connection errors, we don't need them in logs
//        if (t instanceof UnknownHostException || t instanceof SocketTimeoutException) {
//            return;
//        }
//        logError(t, message, args);
//    }

    protected void logError(Throwable t, String message, Object... args) {
        log(LogLevel.ERROR, createTag(), formatString(message, args), t);
    }

    private void log(LogLevel logLevel, String tag, String message) {
        log(logLevel, tag, message, null);
    }

    protected void log(LogLevel logLevel, String tag, String message, Throwable t) {
        String info = getInfo(new Throwable().getStackTrace());
        logToCrashlytics(logLevel, tag, String.format("%s >> %s", message, info), t);
    }

    protected void logToCrashlytics(LogLevel logLevel, String tag, String message, Throwable t) {
        FirebaseCrashlytics.getInstance().log(logLevel.name() + " [" + tag + "] " + message);
        if (t != null) {
            String httpMessage = httpErrorMessage(t);
            if (!TextUtils.isEmpty(httpMessage)) {
                FirebaseCrashlytics.getInstance().log(logLevel.name() + " [" + tag + "] " + httpMessage);
            }
            FirebaseCrashlytics.getInstance().recordException(t);
        }
    }

    private String getInfo(StackTraceElement[] stackTrace) {
        StackTraceElement s1 = stackTrace[6];
        StackTraceElement s2 = stackTrace[7];
        return String.format("%s (%s:%d) in ", methodName(s2), file(s2), line(s2)) +
               String.format("%s (%s:%d)", methodName(s1), file(s1), line(s1));
    }

    private static String methodName(StackTraceElement stackTrace) {
        return stackTrace.getMethodName();
    }

    private static int line(StackTraceElement stackTrace) {
        return stackTrace.getLineNumber();
    }

    private static String file(StackTraceElement stackTrace) {
        return stackTrace.getFileName();
    }

    /**
     * Create a tag from the calling class (4 up).
     * This is not exactly fast but then again this really shouldn't be called a lot
     */
    private static String createTag() {
        String tag = new Throwable().getStackTrace()[5].getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        return tag.substring(tag.lastIndexOf('.') + 1);
    }

    protected static String formatString(String message, Object... args) {
        return args.length == 0 ? message : String.format(message, args);
    }

    private static String httpErrorMessage(Throwable e) {
//        if (e instanceof HttpException) {
        //for validation errors we want to see all fields, not just message. So information is not lost
//            return ErrorHandler.getErrorBody((HttpException) e);
//        }
        return e != null ? e.getMessage() : null;
    }
}
