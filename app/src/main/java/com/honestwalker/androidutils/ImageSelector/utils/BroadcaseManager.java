package com.honestwalker.androidutils.ImageSelector.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BroadcaseManager {
	
	/** 注册广播 */
	public static void registerReceiver(Context context, String action,
			BroadcastReceiver receiver) {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(action);
		context.registerReceiver(receiver, myIntentFilter);
	}

	/** 反注册广播 */
	public static void unregisterReceiver(Context context , BroadcastReceiver receiver) {
		context.unregisterReceiver(receiver);
	}
	
	/** 发送广播 */
	public static void sendBroadcast(Context context , String action , Intent intent) {
		if(intent == null) {
			intent = new Intent(action);
		} else {
			intent.setAction(action);
		}
        context.sendBroadcast(intent); 
	}
	
}
