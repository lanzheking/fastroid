package com.honestwalker.androidutils.exception;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.equipment.SDCardUtil;

/**
 * LogCat中输出错误信息
 * @author Administrator
 *
 */
public class ExceptionUtil {
	public static Boolean show = false;
	public final static String TAG = "AndroidRuntime";
	public static void showException(String tag,Exception e) {
		if (LogCat.showLog()) {
			if (e != null && SDCardUtil.existsSDCard()) {
				StackTraceElement[] stes = e.getStackTrace();
				LogCat.e(tag,"<=======================KancartException====================>");
				LogCat.e(tag,e.getMessage() + " " + e.toString());
				for (StackTraceElement ste : stes) {
					LogCat.e(tag,ste.toString());
				}
				
				if(e.getCause() != null) {
					LogCat.e(tag,"<================KancartException Cause=====================>");
					LogCat.e(tag,e.getCause().toString());
					StackTraceElement[] stesCause = e.getCause().getStackTrace();
					if(stesCause != null) {
						for (StackTraceElement ste : stesCause) {
							LogCat.e(tag,ste.toString());
						}
					}
				}
				LogCat.e(tag,"<============================================================>");
			}
		}
	}
	
	public static void showException(Exception e) {
		if (LogCat.showLog()) {
			showException(TAG, e);
		}
	}
}
