package com.honestwalker.androidutils.os;


import com.honestwalker.androidutils.IO.LogCat;

/**
 * 获取方法的调用者，调试用
 */
public class Caller {

	private static final String TAG = "debug";

	public static void getCaller() {
		getCaller(TAG);
	}

	public static void getCaller(String tag) {
		int i;
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		for (i = 0; i < stack.length; i++) {
			StackTraceElement ste = stack[i];
			LogCat.d(tag, ste.getClassName() + "." + ste.getMethodName() + "(...)");
			LogCat.d(tag, ste.getMethodName() + "    [" + ste.getLineNumber() + "]");
		}
	}

}