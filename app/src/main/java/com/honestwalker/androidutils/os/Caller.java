package com.honestwalker.androidutils.os;


import com.honestwalker.androidutils.IO.LogCat;

public class Caller {
	public static void getCaller() {
		int i;
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		for (i = 0; i < stack.length; i++) {
			StackTraceElement ste = stack[i];
			LogCat.d(ste.getClassName() + "." + ste.getMethodName() + "(...)");
			LogCat.d("--" + ste.getMethodName() + "    [" + ste.getLineNumber() + "]");
//			LogCat.d("--" + ste.getFileName());
		}
	}
	public static void getCaller(String tag) {
		int i;
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		for (i = 0; i < stack.length; i++) {
			StackTraceElement ste = stack[i];
			LogCat.d(tag,ste.getClassName() + "." + ste.getMethodName() + "(...)");
			LogCat.d(tag,"--" + ste.getMethodName() + "    [" + ste.getLineNumber() + "]");
//			LogCat.d("--" + ste.getFileName());
		}
	}
}