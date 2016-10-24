package com.honestwalker.androidutils.window;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.os.BundleObject;

public class DialogHelper {
	
	public static void init(){}
	
	public static void alert(Context context,String message) {
		alert(context,null,message);
	}
	
	/**
	 * 线程中可以直接调用， 因为他是用Handler实现的。
	 * @param context
	 * @param title
	 * @param message
	 */
	public static void alert(Context context,Object title,Object message) {
		alert(context,title,message + "","OK",null,false,"Cacle");
	}
	
	public static void alert(Context context, Object title,Object message,String okButtonStr,Handler okButtonHandler) {
		alert(context,title,message + "",okButtonStr,okButtonHandler,false,null);
	}
	
	public static void alert(Context context, Object title,Object message,String okButtonStr,Handler okButtonHandler,Handler cancelEventHandler) {
		alert(context,title,message + "",okButtonStr,okButtonHandler,false,null,cancelEventHandler);
	}
	
	public static void alert(Context context,Object title,Object message,String okButtonStr,Handler okButtonHandler,Boolean showCancelButton,String cancelButtonStr) {
		BundleObject data = new BundleObject();
		if(title != null && !title.equals("")) {
			data.put("title",title);
		}
		if(message != null && !message.equals("")) {
			data.put("message",message);
		}
		if(okButtonStr != null && !okButtonStr.equals("")) {
			data.put("okButtonStr", okButtonStr);
			if(okButtonHandler != null) {
				data.put("okButtonHandler", okButtonHandler);
			}
		}
		data.put("showCancelButton", showCancelButton);
		data.put("cancelButtonStr", cancelButtonStr);
		data.put("context", context);
		Message msg = new Message();
		msg.obj = data;
		showAlertDialogHandler.sendMessage(msg);
	}
	public static void alert(Context context,Object title,Object message,String okButtonStr,Handler okButtonHandler,Boolean showCancelButton,String cancelButtonStr,Handler cancelEventHandler) {
		BundleObject data = new BundleObject();
		if(title != null && !title.equals("")) {
			data.put("title",title);
		}
		if(message != null && !message.equals("")) {
			data.put("message",message);
		}
		if(okButtonStr != null && !okButtonStr.equals("")) {
			data.put("okButtonStr", okButtonStr);
			if(okButtonHandler != null) {
				data.put("okButtonHandler", okButtonHandler);
			}
		}
		if(cancelEventHandler != null) {
			data.put("cancelEventHandler", cancelEventHandler);
		}
		data.put("showCancelButton", showCancelButton);
		data.put("cancelButtonStr", cancelButtonStr);
		data.put("context", context);
		Message msg = new Message();
		msg.obj = data;
		showAlertDialogHandler.sendMessage(msg);
	}
	
	public static void alert(Context context, Object title,Object message,String okButtonStr,Handler okButtonHandler,String cancelButtonStr,Handler cacleButtonHandler,Handler cancelEventHandler) {
		BundleObject data = new BundleObject();
		if(title != null && !title.equals("")) {
			data.put("title",title);
		}
		if(message != null && !message.equals("")) {
			data.put("message",message);
		}
		if(okButtonStr != null && !okButtonStr.equals("")) {
			data.put("okButtonStr", okButtonStr);
			if(okButtonHandler != null) {
				data.put("okButtonHandler", okButtonHandler);
			}
		}
		if(cancelButtonStr != null && !cancelButtonStr.equals("")) {
			data.put("cancelButtonStr", cancelButtonStr);
			if(cacleButtonHandler != null) {
				data.put("cancelButtonHandler", cacleButtonHandler);
			}
		}
		if(cancelEventHandler != null) {
			data.put("cancelEventHandler", cancelEventHandler);
		}
		data.put("context", context);
		Message msg = new Message();
		msg.obj = data;
		showAlertDialogHandler.sendMessage(msg);
	}

	private static Handler showAlertDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			final BundleObject data    = (BundleObject) msg.obj;
			String title   = data.getString("title");
			String message = data.getString("message");
			Context context = (Context) data.get("context");
			Builder dialog;
			if((Context)data.get("context") != null) {
				dialog = new Builder((Context)data.get("context") , R.style.CustomDialog);
			} else if(context != null) {
				dialog = new Builder(context , R.style.CustomDialog);
			} else {
				return;
			}
			if(title == null) {
				title = "";
			}
			if(message == null) {
				message = "";
			}
			dialog.setTitle(title);
			dialog.setMessage(message);

			String okButtonStr = data.getString("okButtonStr","OK");
			if(okButtonStr == null) {okButtonStr = "ok";}
			// 确定按钮相应事件
			dialog.setPositiveButton(okButtonStr, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(data.get("okButtonHandler") != null) {
						((Handler)data.get("okButtonHandler")).sendEmptyMessage(0);
					}
				}
			});

			// dialog关闭相应事件
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if(data.get("cancelEventHandler") != null) {
						((Handler)data.get("cancelEventHandler")).sendEmptyMessage(0);
					}
				}
			});

			if(data.getBoolean("showCancelButton", false)) {
				String cancelButtonStr = data.getString("cancelButtonStr","Cancel");
				if(cancelButtonStr == null) {cancelButtonStr = "cancel";}
				dialog.setNegativeButton(cancelButtonStr, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
			} else if(data.get("cancelButtonHandler") != null) {
				String cancelButtonStr = data.getString("cancelButtonStr","Cancel");
				dialog.setNegativeButton(cancelButtonStr, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((Handler)data.get("cancelButtonHandler")).sendEmptyMessage(0);
					}
				});
			}
			try {
				dialog.show();
			} catch (Exception e) {
				ExceptionUtil.showException(e);
			}
		};
	};
}
