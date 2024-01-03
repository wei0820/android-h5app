package com.xb.soft.utils;


import com.xb.soft.BuildConfig;

public class Log {
    private final static boolean isDEBUG = BuildConfig.LOG_DEBUG;
    public static void i(String TAG, String msg) {
        if (msg == null) {
            return;
        }
        if (isDEBUG)
            android.util.Log.i(TAG, msg);
    }

    public static void v(String TAG, String msg) {
        if (msg == null) {
            return;
        }
        if (isDEBUG)
            android.util.Log.v(TAG, msg);
    }

    public static void d(String TAG, String msg) {
        if (msg == null) {
            return;
        }
        if (isDEBUG)
            android.util.Log.d(TAG, msg);
    }

    public static void e(String TAG, String msg) {
        if (msg == null) {
            return;
        }
        if (isDEBUG)
            android.util.Log.e(TAG, msg);
    }

    public static void e(String TAG, String msg, Throwable e) {
        if (msg == null) {
            return;
        }
        if (isDEBUG)
            android.util.Log.e(TAG, msg, e);
    }

    public static void w(String TAG, String msg) {
        if (msg == null) {
            return;
        }
        if (isDEBUG)
            android.util.Log.w(TAG, msg);
    }

}
