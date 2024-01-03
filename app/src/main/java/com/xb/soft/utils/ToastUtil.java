package com.xb.soft.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtil {
    private static Toast mToast = null;

    public static void showToast(Context context, String text, int duration) {
        cancelToast();
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mToast = Toast.makeText(context, text, duration);
                mToast.show();
                Looper.loop();
            }
        }.start();

    }

    public static void showMainToast(Context context, String text, int duration) {
        cancelToast();
        mToast = Toast.makeText(context, text, duration);
        mToast.show();
    }

    public static void showToastShort(Context context, String text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToastShort(Context context, int resId) {
        showToastShort(context, context.getString(resId));
    }

    public static void showToastLong(Context context, String text) {
        showToast(context, text, Toast.LENGTH_LONG);
    }

    public static void showToastLong(Context context, int resId) {
        showToastLong(context, context.getString(resId));
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
