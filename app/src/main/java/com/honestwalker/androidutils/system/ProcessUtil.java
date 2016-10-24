package com.honestwalker.androidutils.system;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ProcessUtil {

	/**
	 * 进程自杀
	 */
	public static void killMyProcess() {
		Process.killProcess(Process.myPid());
	}

	/**
	 * 获取当前进程id
	 * @return
	 */
	public static int getMyPid() {
		return Process.myPid();
	}

	/**
	 * 根据id杀进程
	 * @param pid
	 */
	public static void killProcessById(int pid) {
		Process.killProcess(pid);
	}

	/**
	 * 根据进层名杀死进城
	 * @param name
	 */
	public static void killProcessByName(String name) {
		killProcessById(Process.getGidForName(name));
	}

	/** 获取当前进程名 */
	public static String getCurProcessName(Context context) {
		int pid = Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	/**
	 * 判断指定进程是否存在
	 * @param context
	 * @param processName  包名:进程名
	 * @return
	 */
	public static boolean isProccessExist(Context context , String processName) {
		ArrayList<String> pNames = getProccessNameList(context);
		for (String pName : pNames) {
			if(pName.equalsIgnoreCase(processName)) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<String> getProccessNameList(Context context) {
		ArrayList<String> pNames = new ArrayList<>();

		ActivityManager mActivityManager;
		mActivityManager=(ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> apps =
				mActivityManager.getRunningAppProcesses();
		for(ActivityManager.RunningAppProcessInfo app:apps) {
//			Log.d("sys" , " 进程 " + app.processName);
			pNames.add(app.processName);
		}
		return pNames;
	}

}
