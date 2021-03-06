/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.sl4a;

import android.content.Context;
import android.widget.Toast;

public class Log {
    private Log() {
        // Utility class.
    }

    private static String getTag() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String fullClassName = stackTraceElements[4].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        int lineNumber = stackTraceElements[4].getLineNumber();
        return "sl4a." + className + ":" + lineNumber;
    }

    private static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static void v(String message) {
        android.util.Log.v(getTag(), message);
    }

    public static void v(String message, Throwable e) {
        android.util.Log.v(getTag(), message, e);
    }

    public static void v(Context context, String message) {
        toast(context, message);
        android.util.Log.v(getTag(), message);
    }

    public static void v(Context context, String message, Throwable e) {
        toast(context, message);
        android.util.Log.v(getTag(), message, e);
    }

    public static void e(Throwable e) {
        android.util.Log.e(getTag(), "Error", e);
    }

    public static void e(String message) {
        android.util.Log.e(getTag(), message);
    }

    public static void e(String message, Throwable e) {
        android.util.Log.e(getTag(), message, e);
    }

    public static void e(Context context, String message) {
        toast(context, message);
        android.util.Log.e(getTag(), message);
    }

    public static void e(Context context, String message, Throwable e) {
        toast(context, message);
        android.util.Log.e(getTag(), message, e);
    }

    public static void w(Throwable e) {
        android.util.Log.w(getTag(), "Warning", e);
    }

    public static void w(String message) {
        android.util.Log.w(getTag(), message);
    }

    public static void w(String message, Throwable e) {
        android.util.Log.w(getTag(), message, e);
    }

    public static void w(Context context, String message) {
        toast(context, message);
        android.util.Log.w(getTag(), message);
    }

    public static void w(Context context, String message, Throwable e) {
        toast(context, message);
        android.util.Log.w(getTag(), message, e);
    }

    public static void d(String message) {
        android.util.Log.d(getTag(), message);
    }

    public static void d(String message, Throwable e) {
        android.util.Log.d(getTag(), message, e);
    }

    public static void d(Context context, String message) {
        toast(context, message);
        android.util.Log.d(getTag(), message);
    }

    public static void d(Context context, String message, Throwable e) {
        toast(context, message);
        android.util.Log.d(getTag(), message, e);
    }

    public static void i(String message) {
        android.util.Log.i(getTag(), message);
    }

    public static void i(String message, Throwable e) {
        android.util.Log.i(getTag(), message, e);
    }

    public static void i(Context context, String message) {
        toast(context, message);
        android.util.Log.i(getTag(), message);
    }

    public static void i(Context context, String message, Throwable e) {
        toast(context, message);
        android.util.Log.i(getTag(), message, e);
    }
}
