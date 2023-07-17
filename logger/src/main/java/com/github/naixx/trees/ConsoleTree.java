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

import timber.log.L;

//Only for tests!
@Deprecated
public abstract class ConsoleTree extends L.Tree {
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
