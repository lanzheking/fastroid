package com.honestwalker.androidutils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Debug;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;

public class MemoryUtil {

	/**
	 * 获取heapSize 他大于系统给的heapSize 会OOM错误
	 * 
	 * @return 当前app使用的heapSize (MB)
	 */
	public static double getHeapSizeDouble() {
		return DoubleUtil.div(Double.valueOf(Debug.getNativeHeapSize() + ""),
				1024 * 1024, 4);
	}
	public static int getHeapSizeInt() {
		try {
			return (int)Debug.getNativeHeapSize() / 1024 / 1024;
		} catch (Exception e) {
			return 0;
		}
	}

	public static long getNativeHeapFreeSize() {
		return Debug.getNativeHeapFreeSize();
	}

	public static long getGlobalAllocSize() {
		return Debug.getGlobalAllocSize();
	}

	public static long getGlobalFreedSize() {

		return Debug.getGlobalFreedSize();
	}

	private static int vmHeapSize = -1;
	public static int getVMHeapSize() {
		if(vmHeapSize != -1) {
			return vmHeapSize;
		}
		int vmHeapSize = 0;
		String configPath = "/system/build.prop";
		try {
			FileReader fr = new FileReader(configPath);
			BufferedReader br = new BufferedReader(fr,1024);
			String strRead = "";
			String key = "dalvik.vm.heapsize=";
			while( (strRead = br.readLine()) != null ) {
				if(strRead.indexOf(key) == 0) {
					if(strRead.trim().toLowerCase().endsWith("m")) {
						strRead = strRead.substring(key.length(),strRead.length() - 1);
						break;
					}
				}
			}
			br.close();
			if(!StringUtil.isEmptyOrNull(strRead)) {
				return Integer.parseInt(strRead);
			}
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
		return vmHeapSize;
	}
	
	public static int getFreeVMHeapSize() {
		if(getVMHeapSize() > -1) {
			return getVMHeapSize() - getHeapSizeInt();
		} else {
			return 0;
		}
	}
	
	public static StringBuffer memoryInfoSB = new StringBuffer();
	public static double heapsize = 0;
	public static double total = 0;
	private static Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
	public static void showMemoryInfo(Context context) {
		Debug.getMemoryInfo(memoryInfo);
		memoryInfoSB.delete(0, memoryInfoSB.length());
		memoryInfoSB.append("memory userd/free : ");
		heapsize = DoubleUtil.div((memoryInfo.getTotalPss()) , 1024d , 1);
		memoryInfoSB.append(heapsize + " MB");
		if(getAppLevel() > 15) {
			total = getFreeVMHeapSize();
			memoryInfoSB.append(" / " + total + " MB");
		}
		LogCat.d("heapsize", memoryInfoSB.toString());
	}
	
	private static int getAppLevel() {
		int sysVersion = Integer.parseInt(VERSION.SDK); 
		return sysVersion;
	}
	
	private static boolean running = false;
	public static synchronized void onShowMemoryInfo(final Context context , final ShowMemoryListener showMemoryListener) {
		
		if(!running) {
			// 内存监控
			Timer timer = new Timer();
			TimerTask tt = new TimerTask() {
				@Override
				public void run() {
					running = true;
					MemoryUtil.showMemoryInfo(context);
					if(showMemoryListener != null) {
						showMemoryListener.onShow(heapsize, total);
					}
				}
			};
			timer.schedule(tt, 0, 1000);
		}
	}
	
}
