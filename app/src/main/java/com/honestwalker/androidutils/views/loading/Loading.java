package com.honestwalker.androidutils.views.loading;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.RClassUtil;
import com.honestwalker.androidutils.os.BundleObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loading窗口   ，  注意registerDialog不能在线程中直接调用，必须使用Handler<br />
 * 在Handler中调用LoadingHelper.registerDialog(...)<br />
 * @author Administrator
 *
 */
public class Loading {

	private static Map<String,LoadingStyle> dialogStyleMap = new HashMap<String, LoadingStyle>();
	private static Map<Context,Dialog> dialogMap = new HashMap<Context, Dialog>();
	
	private static boolean configLoaded = false;
	public static void init(Context context) {
		if(configLoaded) return;
		configLoaded = true;
		try {
			Class rClass = RClassUtil.getRClass(context);
			List<LoadingStyle> loadingStyleList = LoadingLoader.load(context , RClassUtil.getResId(rClass , "raw.loading_config"));
			for(LoadingStyle loadingStyle : loadingStyleList) {
				dialogStyleMap.put(loadingStyle.getId() , loadingStyle);
			}
		} catch (Exception e) {}
	}

	/**
	 * 指定loading是否已经注册
	 * @param dialogName
	 * @return
	 */
	public static boolean containsDialog(String dialogName) {
		return dialogStyleMap.containsKey(dialogName);
	}
	
	public static void show(Context context,String dialogName) {
		show(context,dialogName,null);
	}
	
	public static void show(Context context,String dialogName , final Handler onBackPressHandler) {
		LoadingStyle style = dialogStyleMap.get(dialogName);
		BundleObject data = new BundleObject();
		data.put("style", style);
		data.put("context", context);
		data.put("onBackPressHandler", onBackPressHandler);
		Message msg = new Message();
		msg.what=1;
		msg.obj = data;
		showDialogHandler.sendMessage(msg);
	}
	
	public static void dismiss(Context context){
		Message msg = new Message();
		msg.what = 0;
		msg.obj = context;
		showDialogHandler.sendMessage(msg);
	}
	
	private static Handler showDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			Dialog loadingDialog;
			if(msg.what == 1) {
				BundleObject data = (BundleObject) msg.obj;
				LoadingStyle style = (LoadingStyle) data.get("style");
				Context context = (Context) data.get("context");
				if(context==null || style==null){
					return;
				}
				loadingDialog = new Dialog(context,style.getStyleResId());
				final Handler onBackPressHandler = (Handler) data.get("onBackPressHandler");
				if(style != null) {
					LayoutInflater factory = LayoutInflater.from(context);
					View view = null;
					if (style.getContentView() != null) {
						view = style.getContentView();
					}else {
						view = factory.inflate(style.getLayoutResId(), null);
					}
					int width = style.getWidth() > 0 ? style.getWidth() : LayoutParams.WRAP_CONTENT;
					int height = style.getHeight() > 0 ? style.getHeight() : LayoutParams.WRAP_CONTENT;
					LayoutParams lp = new LayoutParams(width , height);
					TextView messageTV = (TextView) view.findViewById(style.getTextResourceId());
					if(messageTV != null) {
						messageTV.setText(style.getText());
					}
					loadingDialog.setCancelable(false);
					if (style.isCancelable()) {
						loadingDialog.setCancelable(true);
					}
					loadingDialog.getWindow().setContentView(view,lp);
					loadingDialog.setOnKeyListener(new OnKeyListener() {
						private Boolean clicked = false;
						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							LogCat.d("LoadingHelper", "loading key = " + keyCode);
							if(keyCode == KeyEvent.KEYCODE_BACK) {
								LogCat.d("LoadingHelper", "loading key = " + keyCode + " click =" + clicked);
								if(!clicked) {
									clicked = true;
									if(onBackPressHandler != null) {
										onBackPressHandler.sendEmptyMessage(0);
									}
								}
							}
							return false;
						}
					});
					try {
						loadingDialog.show();
						dialogMap.put(context, loadingDialog);
					} catch (Exception e) {
					}
				}
			} else if(msg.what == 0) {
				if(msg.obj != null) {
					LogCat.d("LoadingHelper",(Context)msg.obj);
					Dialog dialog = dialogMap.get((Context)msg.obj);
					if(dialog != null) {
						try {
							dialog.dismiss();
						} catch (Exception e) {
						}
					}
				}
			}
		}
	};
	
}
