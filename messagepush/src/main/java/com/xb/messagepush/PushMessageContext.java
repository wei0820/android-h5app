package com.xb.messagepush;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;

public class PushMessageContext {
	private static PushMessageContext ourInstance = new PushMessageContext();

	public static PushMessageContext getInstance() {
		return ourInstance;
	}

	private PushMessageContext() {
	}

	public void init(Context context){
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(context);     		// 初始化 JPush
	}

	public void startPush(Context context,boolean start){
		if(start)
			JPushInterface.resumePush(context);
		else
			JPushInterface.stopPush(context);
	}
}
