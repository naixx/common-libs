package com.github.naixx.trees;

import timber.log.Timber;

//Only for tests!
@Deprecated
public class ConsoleTree implements Timber.Tree {
    @Override
    public void v(String message, Object... args) {
        System.out.println(message);
    }

    @Override
    public void v(Throwable t, String message, Object... args) {
        System.out.println(message);
    }

    @Override
    public void d(String message, Object... args) {
        System.out.println(message);
    }

    @Override
    public void d(Throwable t, String message, Object... args) {
        System.out.println(message);
    }

    @Override
    public void i(String message, Object... args) {
        System.out.println(message);
    }

    @Override
    public void i(Throwable t, String message, Object... args) {
        System.out.println(message);
    }

    @Override
    public void w(String message, Object... args) {
        System.out.println(message);
    }

    @Override
    public void w(Throwable t, String message, Object... args) {
        System.out.println(message);
    }

    @Override
    public void e(String message, Object... args) {
        System.out.println(message);
    }

    @Override
    public void e(Throwable t, String message, Object... args) {
        System.out.println(message);
    }
}
