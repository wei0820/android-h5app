package com.xb.soft;

import android.app.Application;
import android.graphics.Bitmap;

import com.xb.soft.utils.PreferenceUtils;

public class MyApplication extends Application {
    public static Application application;
    public static String captureDocResult = "false";
    public static String captureFaceResult = "false";

    public static Bitmap captureDocResultBitmap;
    public static Bitmap captureFaceResultBitmap;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ConfigAPP.initConfigContext(this);
        PreferenceUtils.init(this);
    }

}
