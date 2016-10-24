package com.honestwalker.androidutils;

import android.os.Handler;

public class UIHandler {

	private static Handler uiHandler;
	
	public static void init(){
		if(uiHandler == null) {
			uiHandler = new Handler();
		}
	}
	
	public static void post(Runnable runnable) {
		init();
		uiHandler.post(runnable);
	}
	public static void postDelayed(Runnable runnable,long delayMillis) {
		init();
		uiHandler.postDelayed(runnable, delayMillis);
	}

}
