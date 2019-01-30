package com.honestwalker.androidutils.window;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.honestwalker.androidutils.os.BundleObject;

public class ToastHelper {
	
	public static void init(){}
	
	public static void alert(Context context,String message) {
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

			fixStyle(toast);

			toast.show();

			if(message == null) {
				message = "";
			}
			toast.show();
		}
	};

	private static void fixStyle(Toast  toast) {
		try {
			LinearLayout layout = (LinearLayout)toast.getView();
			int childrenCount = layout.getChildCount();
			for (int i = 0; i < childrenCount; i++) {
				if(layout.getChildAt(i) instanceof TextView) {
					TextView tv = (TextView) layout.getChildAt(i);
					tv.setBackgroundColor(0x00000000);
				}
			}
		} catch (Exception e) {

		}
	}
	
}
