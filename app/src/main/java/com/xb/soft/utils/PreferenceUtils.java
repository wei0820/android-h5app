package com.xb.soft.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class PreferenceUtils {

	public static final String PREFENCE_FILE_NAME = "jy_data";
	public static final String WEB_URL = "web.url";
	public static final String FIRST_LANUCH = "first";

	private static Application APPLICATION  = null;
	
	public static void init(Application app){
		APPLICATION = app;
	}
	
	public static void setValue(String key,long lValue){
		if(key == null || APPLICATION == null)
			return;
		 SharedPreferences preferences = APPLICATION.getSharedPreferences(PREFENCE_FILE_NAME, Context.MODE_PRIVATE);
		 Editor editor = preferences.edit();
		 editor.putLong(key, lValue);
		 editor.commit();
	}

	public static void setValue(String key,int iValue){
		if(key == null || APPLICATION == null)
			return;
		SharedPreferences preferences = APPLICATION.getSharedPreferences(PREFENCE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(key, iValue);
		editor.commit();
	}

	public static void setValue(String key,String sValue){
		if(key == null || APPLICATION == null)
			return;
		SharedPreferences preferences = APPLICATION.getSharedPreferences(PREFENCE_FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(key, sValue);
		editor.commit();
	}

	public static long getLong(String key){
		if(key == null || APPLICATION == null)
			return 0;
		 SharedPreferences preferences = APPLICATION.getSharedPreferences(PREFENCE_FILE_NAME, Context.MODE_PRIVATE);
		return  preferences.getLong(key, 0);
	}

	public static int getInt(String key){
		if(key == null || APPLICATION == null)
			return 0;
		SharedPreferences preferences = APPLICATION.getSharedPreferences(PREFENCE_FILE_NAME, Context.MODE_PRIVATE);
		return  preferences.getInt(key, 0);
	}

	public static String getString(String key){
		if(key == null || APPLICATION == null)
			return null;
		SharedPreferences preferences = APPLICATION.getSharedPreferences(PREFENCE_FILE_NAME, Context.MODE_PRIVATE);
		return  preferences.getString(key,null);
	}

	public static String getString(String key,String defaultValue){
		if(key == null || APPLICATION == null)
			return null;
		SharedPreferences preferences = APPLICATION.getSharedPreferences(PREFENCE_FILE_NAME, Context.MODE_PRIVATE);
		return  preferences.getString(key,defaultValue);
	}


	public static boolean getBoolean(String key){
		return getBoolean(key,true);
	}

	public static boolean getBoolean(String key,boolean defaultValue){
		if(key == null || APPLICATION == null)
			return false;
		SharedPreferences preferences = APPLICATION.getSharedPreferences(PREFENCE_FILE_NAME, Context.MODE_PRIVATE);
		return  preferences.getBoolean(key, defaultValue);
	}
	
	public static void setBoolean(String key,boolean bValue){
		if(key == null || APPLICATION == null)
			return;
		 SharedPreferences preferences = APPLICATION.getSharedPreferences(PREFENCE_FILE_NAME, Context.MODE_PRIVATE);
		 Editor editor = preferences.edit();
		 editor.putBoolean(key, bValue);
		 editor.commit();
	}

	/**
	 * 查询某个key是否已经存在
	 * @param key
	 * @return
	 */
	public static boolean contains(String key) {
		SharedPreferences sp = APPLICATION.getSharedPreferences(PREFENCE_FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.contains(key);
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 *
	 * @author zhy
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 *
		 * @return
		 */
		@SuppressWarnings({"unchecked", "rawtypes"})
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 *
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			editor.commit();
		}
	}
}
