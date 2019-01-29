package com.honestwalker.androidutils.IO;

import android.util.Log;

import com.honestwalker.androidutils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 日志工具
 *
 */
public class LogCat {

	protected static String TAG = "";
	private static int segmentationLength = 3000;

	public static void setTag(String tag) {
		LogCat.TAG = tag;
	}

	public static void d(Object msg) {
		if (msg == null) {
			Log.i(TAG, "");
		} else {
			logd(TAG, msg + "");
		}
	}

	public static void d(String tag, Object msg) {
		if (msg == null) {
			Log.i(tag, "");
		} else {
			logd(tag, msg + "");
		}
	}

	public static void d(Object tag, Object msg) {
		if (msg == null) {
			Log.i(tag.toString(), "");
		} else {
			logd(tag + "", msg + "");
		}
	}

	public static void v(Object msg) {
		if (msg == null) {
			Log.v(TAG, "");
		} else {
			Log.v(TAG, msg.toString());
		}
	}

	public static void v(String tag, Object msg) {
		if (msg == null) {
			Log.v(tag, "");
		} else {
			Log.v(tag, msg.toString());
		}
	}

	public static void e(Object msg) {
		if (msg == null) {
			Log.e(TAG, "");
		} else {
			Log.e(TAG, msg.toString());
		}
	}

	public static void e(String tag, Object msg) {
		if (msg == null) {
			Log.e(tag, "");
		} else {
			Log.e(tag, msg.toString());
		}
	}

	public static void e(Object tag, Object msg) {
		if (msg == null) {
			Log.e(tag.toString(), "");
		} else {
			Log.e(tag.toString(), msg.toString());
		}
	}

	public static void w(Object msg) {
		if (msg == null) {
			Log.w(TAG, "");
		} else {
			Log.w(TAG, msg.toString());
		}
	}

	public static void w(String tag, Object msg) {
		if (msg == null) {
			Log.w(tag, "");
		} else {
			Log.w(tag, msg.toString());
		}
	}

	public static void i(Object msg) {
		if (msg == null) {
			Log.i(TAG, "");
		} else {
			Log.i(TAG, msg.toString());
		}
	}

	public static void i(String tag, Object msg) {
		if (msg == null) {
			Log.i(tag, "");
		} else {
			Log.i(tag, msg.toString());
		}
	}

	private synchronized static void logd(String tag , Object msg) {
		String msgStr = msg + "";

		if(msgStr.length() <= segmentationLength) {
			Log.i(tag, getCaller() + msgStr + "\r");
			return;
		}

		List<String> segmentationMsg = new ArrayList<>();
		segmentationMsg.add(getCaller());
		int msgStrLen = msgStr.length();
		int segmentationLen = msgStrLen % segmentationLength == 0 ? msgStrLen / segmentationLength : msgStrLen / segmentationLength + 1;
		for(int i=0; i < segmentationLen ; i++ ){
			if(msgStr.length() > (i+1)*segmentationLength) {
				segmentationMsg.add(msgStr.substring(i * segmentationLength, (i + 1) * segmentationLength));
			} else {
				segmentationMsg.add(msgStr.substring(i * segmentationLength, msgStr.length() - 1));
			}
		}
		for(String s : segmentationMsg) {
			Log.d(tag, s);
		}
		Log.d(tag, "\r");
	}

	private static String getCaller() {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();  //获取线程运行栈信息
		StringBuffer sb = new StringBuffer();
		for(int i=3;i<stack.length;i++) {
			StackTraceElement s = stack[i];
			sb.append("\r").append("[")
					.append(TimeUtil.getNow()).append(" ")
					.append(s.getClassName()).append(":")
					.append(s.getLineNumber())
					.append("]: ");
//			System.out.format(" ClassName:%d\t%s\n", i, s.getClassName());
//			System.out.format("MethodName:%d\t%s\n", i, s.getMethodName());
//			System.out.format("  FileName:%d\t%s\n", i, s.getFileName());
//			System.out.format("LineNumber:%d\t%s\n\n", i, s.getLineNumber());
			break;
		}
		return sb.toString();
	}

	private static long startTime;
	private static long endTime;
	public static void startTimeLog(Object msg) {
		startTime = System.currentTimeMillis();
		endTime   = 0;
		LogCat.d(msg);
	}
	public static void endTimeLog(String msg) {
		endTime = System.currentTimeMillis();
		LogCat.d(msg + "   >  " + (endTime - startTime));
		startTime = 0;
		endTime   = 0;
	}

}
