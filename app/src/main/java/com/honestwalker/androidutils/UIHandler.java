package com.honestwalker.androidutils;

import android.os.Handler;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

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

	public static <T> T post(final Callable<T> callable) {
		init();
		FutureTask<T> future = new FutureTask<>(callable);
		uiHandler.post(future);
		try {
			return future.get();
		} catch (Exception e) {
			ExceptionUtil.showException(e);
			return null;
		}
	}

	public static void postDelayed(Runnable runnable,long delayMillis) {
		init();
		uiHandler.postDelayed(runnable, delayMillis);
	}

	public static <T> T postDelayed(Callable<T> callable,long delayMillis) {
		init();
		FutureTask<T> future = new FutureTask<>(callable);
		uiHandler.postDelayed(future, delayMillis);
		try {
			LogCat.d("AOP", "postDelayed id=" + Thread.currentThread().getId());
			T result = future.get();
			return result;
		} catch (Exception e) {
			ExceptionUtil.showException(e);
			return null;
		}
	}

}
