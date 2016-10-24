package com.honestwalker.androidutils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class SysyemPageOpen {
	
	/**
	 * 打开电话
	 * @param context
	 * @param phone
	 */
	public static void call (Activity context , String phone) {
		 Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:"+ phone)); 
		 context.startActivity(phoneIntent);
	}
	
}
