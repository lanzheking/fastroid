package com.honestwalker.androidutils.IO;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Administrator
 * 
 */
public class LogCat {

	protected static String TAG = "";
	public static Boolean showLog = true;
	private static int segmentationLength = 3000;

	public static void showLog(Boolean show) {
		showLog = show;
	}
	
	public static Boolean showLog( ) {
		return showLog;
	}
	
	public static void setTag(String tag) {
		LogCat.TAG = tag;
	}

	public static void d(Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.i(TAG, "");
			} else {
				logd(TAG, msg + "");
			}
		}
	}

	public static void d(String tag, Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.i(tag, "");
			} else {
				logd(tag, msg + "");
			}
		}
	}
	
	public static void d(Object tag, Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.i(tag.toString(), "");
			} else {
				logd(tag + "", msg + "");
			}
		}
	}

	public static void v(Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.v(TAG, "");
			} else {
				Log.v(TAG, msg.toString());
			}
		}
	}

	public static void v(String tag, Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.v(tag, "");
			} else {
				Log.v(tag, msg.toString());
			}
		}
	}
	
	public static void e(Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.e(TAG, "");
			} else {
				Log.e(TAG, msg.toString());
			}
		}
	}

	public static void e(String tag, Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.e(tag, "");
			} else {
				Log.e(tag, msg.toString());
			}
		}
	}
	
	public static void e(Object tag, Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.e(tag.toString(), "");
			} else {
				Log.e(tag.toString(), msg.toString());
			}
		}
	}

	public static void w(Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.w(TAG, "");
			} else {
				Log.w(TAG, msg.toString());
			}
		}
	}

	public static void w(String tag, Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.w(tag, "");
			} else {
				Log.w(tag, msg.toString());
			}
		}
	}
	
	public static void i(Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.i(TAG, "");
			} else {
				Log.i(TAG, msg.toString());
			}
		}
	}

	public static void i(String tag, Object msg) {
		if (showLog) {
			if (msg == null) {
				Log.i(tag, "");
			} else {
				Log.i(tag, msg.toString());
			}
		}
	}
	
	private synchronized static void logd(String tag , Object msg) {
		String msgStr = msg + "";
		
		if(msgStr.length() <= segmentationLength) {
			Log.i(tag, msgStr);
			return;
		}
		
		List<String> segmentationMsg = new ArrayList<String>();
		int msgStrLen = msgStr.length();
		int segmentationLen = msgStrLen % segmentationLength == 0 ? msgStrLen / segmentationLength : msgStrLen / segmentationLength + 1;
		for(int i=0; i < segmentationLen ; i++ ){
			if(msgStr.length() > (i+1)*segmentationLength) {
				segmentationMsg.add(msgStr.substring(i * segmentationLength, (i + 1) * segmentationLength));
//				Log.i(tag, msgStr.substring(i * segmentationLength, (i + 1) * segmentationLength));
			} else {
				segmentationMsg.add(msgStr.substring(i * segmentationLength, msgStr.length() - 1));
//				Log.i(tag, msgStr.substring(i * segmentationLength, msgStr.length() - 1));
			}
		}
		for(String s : segmentationMsg) {
			Log.i(tag, s);
		}
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
