package com.honestwalker.androidutils.IO;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesLoader {
	
	private static SharedPreferencesLoader instance;
	private static SharedPreferences sp;
	private static Context context;
	
	public static SharedPreferencesLoader getInstance(Context context) {
		SharedPreferencesLoader.context = context;
		instance = new SharedPreferencesLoader();
		sp = context.getSharedPreferences("KanCart", Context.MODE_MULTI_PROCESS);
		return instance;
	}
	
	public static void putString(String key , String value) {
		getInstance(context);    // 更新数据重新初始化是为了让多进程SharedPreferences更改数据后即时生效
		if(sp == null) {
			return;
		}
		sp.edit().putString(key, value).commit();
	}
	
	public static String getString(String key , String defaultValue) {
		getInstance(context);
		if(sp == null){
			return "";
		}
		return sp.getString(key, defaultValue);
	}
	
	public static String getString(String key) {
		getInstance(context);
		if(sp == null){
			return "";
		}
		return sp.getString(key, "");
	}
	
	public static void putInt(String key , int value) {
		getInstance(context);
		if(sp == null) {
			return;
		}
		sp.edit().putInt(key, value).commit();
	}

	public static void putLong(String key , long value) {
		getInstance(context);
		if(sp == null) {
			return;
		}
		sp.edit().putLong(key, value).commit();
	}

	public static int getInt(String key) {
		getInstance(context);
		if(sp == null){
			return 0;
		}
		return sp.getInt(key, 0);
	}

	public static long getLong(String key) {
		getInstance(context);
		if(sp == null){
			return 0;
		}
		return sp.getLong(key, 0);
	}

	public static long getLong(String key , long defaultValue) {
		getInstance(context);
		if(sp == null){
			return 0;
		}
		return sp.getLong(key, defaultValue);
	}

	public static int getInt(String key,int defaultValue){
		getInstance(context);
		if(sp == null){
			return 0;
		}
		return sp.getInt(key, defaultValue);
	}
	
	public static void putBoolean(String key , boolean value) {
		getInstance(context);
		if(sp == null) {
			return;
		}
		sp.edit().putBoolean(key, value).commit();
	}
	public static boolean getBoolean(String key) {
		getInstance(context);
		if(sp == null){
			return false;
		}
		return sp.getBoolean(key, false);
	}
	public static boolean getBoolean(String key , boolean defaultValue) {
		getInstance(context);
		if(sp == null){
			return defaultValue;
		}
		return sp.getBoolean(key, defaultValue);
	}
	
}
