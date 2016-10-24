package com.honestwalker.androidutils.window;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.honestwalker.androidutils.os.BundleObject;

public class ToastHelper {
	
	public static void init(){}
	
	public static void alert(Context context,String message) {
		alert(context,message,2000);
	}
	
	public static void alert(Context context,String message,int duration) {
		Message msg = new Message();
		BundleObject data = new BundleObject();
		data.put("context", context);
		data.put("message", message);
		msg.obj = data;
		showAlertDialogHandler.sendMessage(msg);
	}
	
	private static Handler showAlertDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			final BundleObject data    = (BundleObject) msg.obj;
			String message = data.getString("message");
			Toast  toast   = Toast.makeText((Context)data.get("context"), message, 0);
			if(message == null) {
				message = "";
			}
			toast.show();
		};
	};
	
}
