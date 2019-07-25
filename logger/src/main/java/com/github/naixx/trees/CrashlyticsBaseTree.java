package com.github.naixx.trees;

import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public class CrashlyticsBaseTree extends Timber.HollowTree {

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

    @Override
    public void d(String message, Object... args) {
        log(LogLevel.DEBUG, createTag(), formatString(message, args));
    }

    @Override
    public void d(Throwable t, String message, Object... args) {
        log(LogLevel.DEBUG, createTag(), formatString(message, args), t);
    }

    @Override
    public void v(String message, Object... args) {
    }

    @Override
    public void v(Throwable t, String message, Object... args) {
    }

    @Override
    public void i(String message, Object... args) {
        log(LogLevel.INFO, createTag(), formatString(message, args));
    }

    @Override
    public void i(Throwable t, String message, Object... args) {
        log(LogLevel.INFO, createTag(), formatString(message, args), t);
    }

    @Override
    public void w(String message, Object... args) {
        log(LogLevel.WARNING, createTag(), formatString(message, args));
    }

    @Override
    public void w(Throwable t, String message, Object... args) {
        log(LogLevel.WARNING, createTag(), formatString(message, args), t);
    }

    @Override
    public void e(String message, Object... args) {
        log(LogLevel.ERROR, createTag(), formatString(message, args));
    }

    @Override
    public void e(Throwable t, String message, Object... args) {
        //usually it is connection errors, we don't need them in logs
        if (t instanceof UnknownHostException || t instanceof SocketTimeoutException) {
            return;
        }
        logError(t, message, args);
    }

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
        Crashlytics.getInstance().core.log(logLevel.priority, tag, message);
        if (t != null) {
            String httpMessage = httpErrorMessage(t);
            if (!TextUtils.isEmpty(httpMessage)) {
                Crashlytics.getInstance().core.log(LogLevel.ERROR.priority, tag, httpMessage);
            }
            Crashlytics.logException(t);
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
