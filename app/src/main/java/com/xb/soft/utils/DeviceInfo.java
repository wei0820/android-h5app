package com.xb.soft.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DeviceInfo {

	private static int windowsWidth;
	private static int windowsHeight;
	private static float density = 3;

	private static Context mContext;

	public static void initDeviceContext(Context context){
		mContext = context;
		initDeviceInfo();
	}
	private static void initDeviceInfo(){
		if(mContext == null)
			return;
		DisplayMetrics display = mContext.getResources().getDisplayMetrics();
		DeviceInfo.density = display.density;
		DeviceInfo.windowsWidth = display.widthPixels;
		DeviceInfo.windowsHeight = display.heightPixels;
	}

	public static int getWindowsHeight() {
		if(windowsHeight == 0)
			initDeviceInfo();
		return windowsHeight;
	}

	public static float getDensity() {
		if(density == 0)
			initDeviceInfo();
		return density;
	}

	public static int getWindowsWidth() {
		if(windowsWidth == 0)
			initDeviceInfo();
		return windowsWidth;
	}

	public static int getStatueBarHeight(Activity activity) {
		return Utils.getStatusBarHeight(activity);
	}

	public static int getNaviBarHeight(Activity activity) {
		return Utils.getNavigationBarHeight(activity);
	}

}
