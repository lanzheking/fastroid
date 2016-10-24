package com.honestwalker.androidutils.window;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;

public class MyDialog extends Dialog{
	
	public MyDialog(Context context,int styleId) {
		super(context, styleId);
	}
	
	public void onBackPressed(Handler onBackPressedHandler) {
		onBackPressedHandler.sendEmptyMessage(0);
	}
	
}
