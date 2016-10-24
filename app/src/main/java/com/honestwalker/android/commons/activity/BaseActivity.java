package com.honestwalker.android.commons.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.commons.BaseApplication;
import com.honestwalker.android.commons.Constants.RequestCode;
import com.honestwalker.android.commons.Constants.ResultCode;
import com.honestwalker.android.commons.config.ContextProperties;
import com.honestwalker.android.commons.utils.IntentBind.IntentBinder;
import com.honestwalker.android.commons.utils.StartActivityHelper;
import com.honestwalker.android.kc_commons.ui.utils.TranslucentStatus;
import com.honestwalker.androidutils.EventAction.ActionClick;
import com.honestwalker.androidutils.EventAction.ActionItemClick;
import com.honestwalker.androidutils.EventAction.ActionLongClick;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.honestwalker.androidutils.equipment.SDCardUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.pool.ThreadPool;
import com.honestwalker.androidutils.views.AlertDialogPage;
import com.honestwalker.androidutils.views.loading.Loading;
import com.honestwalker.androidutils.window.DialogHelper;
import com.honestwalker.androidutils.window.ToastHelper;
import com.lidroid.xutils.ViewUtils;
import com.systembartint.SystemBarTintManager;

import java.lang.reflect.Method;

/**
 * Created by honestwalker on 13-8-8.
 */
public abstract class BaseActivity extends FragmentActivity {
    
	//================================
	//
	//            公共控件
	//
	//================================
	
	//================================
	//
	//            公共参数
	//
	//================================

	protected LayoutInflater inflater;
	protected BaseActivity   context;
	
	public static ViewSizeHelper viewSizeHelper;

	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static int statusBarHeight = 0;

	protected int backAnimCode = 0;
	
	public View contentView;
	
	private long onResumeTime = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;

		// 侵入时标题支持
		TranslucentStatus.setEnable(this);

		ViewUtils.inject(this);

		init();

		// 执行字段绑定器
		new IntentBinder().doIntentBind(this);

		onMeasure();

		loadData();

	}

	@Override
	protected void onStart() {
		super.onStart();
		
		BaseApplication.lastPage = this.getClass().getSimpleName();
		
		if(getIntent() != null) {
			backAnimCode = getIntent().getIntExtra("backAnimCode", 0);
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		onResumeTime = System.currentTimeMillis();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		
		if(System.currentTimeMillis() - onResumeTime < 400) { // 避免连续按后退动画瑕疵
			return;
		}
		
		super.onBackPressed();
		finish();

		LogCat.d("backAnimCode" , "backAnimCode=" + backAnimCode);

		if(backAnimCode != 0) {
			StartActivityHelper.activityAnim(context, getIntent(), backAnimCode);
			backAnimCode = 0;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * 初始化公共参数
	 */
	public void init() {
		if (viewSizeHelper == null) {
			viewSizeHelper = ViewSizeHelper.getInstance(this);
		}
		if (screenWidth == 0) {
			screenWidth = DisplayUtil.getWidth(context);
			screenHeight = DisplayUtil.getHeight(context);
			statusBarHeight = DisplayUtil.getStatusBarHeight(context);
//			titleHeight = (int) (screenHeight * titleHeightScale);
		}
		if (inflater == null) {
			inflater = getLayoutInflater();
		}
	}

	/**
	 * 待重写方法，用于对控件进行显示设置，如：距离，位置，大小等等
	 */
	protected void onMeasure() {}

	protected abstract void loadData();

	private View layoutResView;
	public View getLayoutResView(){return layoutResView;}
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		this.layoutResView = inflater.inflate(layoutResID, null);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		this.layoutResView = view;
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		this.layoutResView = view;
	}
	
	protected Activity getContext() {
		context = this;
		return context;
	}

	/**
	 * 获取缓存路径 ， 末尾已经包含 /
	 */
	public String getCachePath() {
		return ContextProperties.getConfig().cachePath;
	}

	public String getImageCachePath() {
		return ContextProperties.getConfig().cachePath + "image/";
	}

	public String getSDCachePath() {
		return SDCardUtil.getSDRootPath() + ContextProperties.getConfig().sdcartCacheName + "/";
	}

	public void recyleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}
	
	/** 获取当前登录的账户 */
//	public User getLoginUser() {
//		try {
//			User user = (User) ObjectStreamIO.input(getCachePath(), ObjectStreamType.LOGIN_USER);
//			return user;
//		} catch (Exception e) {
//		}
//		return null;
//	}
//	/** 判断是否需要登录 */
//	protected boolean needLogin() {
//		return (getLoginUser() == null || StringUtil.isEmptyOrNull(getLoginUser().getSessionkey()));
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode == RequestCode.ACTION_LOGIN) {
			if(resultCode == ResultCode.RESULT_CANCELED) {
//				loginCancleCallback();
			} else if(resultCode == ResultCode.RESULT_OK) {
				// 登录成功回调
//				loginSuccessCallback(getLoginSuccessUser());
			}
		}
	}
	
	/** 启动线程 */
	public void threadPool(Runnable runnable) {
		ThreadPool.threadPool(runnable);
	}
	
	/*=================================
	 * 
	 *             Intent 相关操作开始
	 * 
	 *=================================*/
	
	public Intent getIntent() {
		return super.getIntent() == null ? new Intent():super.getIntent();
	}
	
	/** 从intent取得一个Serializable对象 */
	public Object getIntentSerializableExtra(String key){
		Intent intent = getIntent();
		Object value  = intent.getSerializableExtra(key);
		return value;
	}
	
	public Object getIntentSerializableExtra(Intent intent , String key){
		Object value = intent.getSerializableExtra(key);
		return value;
	}
	
	/*=================================
	 * 
	 *      Intent 相关操作结束
	 * 
	 *=================================*/
	
	/*=================================
	 * 
	 *             公共控件操作
	 * 
	 *=================================*/

	public void setSize(View view , float width , float height) {
		setSize(view , (int)width , (int)height);
	}

	public void setSize(View view , int width , int height) {
		viewSizeHelper.setSize(view, width, height);
	}

	public void setSize(int viewResId , float width , float height) {
		setSize(viewResId , (int)width , (int)height);
	}

	public void setSize(int viewResId , int width , int height) {
		View view = findViewById(viewResId);
		setSize(view, width, height);
	}

	public void setWidth(View view,int width) {
		viewSizeHelper.setWidth(view, width);
	}

	public void setWidth(int viewResId , int width) {
		View view = findViewById(viewResId);
		setWidth(view, width);
	}

	public void setWidth(int viewResId , int width , int scaleWidth, int scaleHeight) {
		View view = findViewById(viewResId);
		setWidth(view, width, scaleWidth, scaleHeight);
	}

	public void setWidth(View view , int width , int scaleWidth, int scaleHeight) {
		int height = width * scaleHeight / scaleWidth;
		setWidth(view, width);
		setHeight(view, height);
	}

	public void setHeight(View view,int height) {
		try{
			LayoutParams lp = view.getLayoutParams();
			lp.height = height;
		} catch (Exception e) {
		}
	}

	public void setHeight(int viewResId,int height) {
		try{
			LayoutParams lp = findViewById(viewResId).getLayoutParams();
			lp.height = height;
		} catch (Exception e) {
		}
	}

	public void loading(final boolean show) {
		if(show) {
			Loading.show(context , "loading_cancelunable");
		} else {
			Loading.dismiss(context);
		}
	}

	public void loadingCancelAble(final boolean show) {
		if(show) {
			Loading.show(context , "loading_cancelable");
		} else {
			Loading.dismiss(context);
		}
	}
	
	public OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};

	/*=================================
	 * 
	 *         公共控件操作结束
	 * 
	 *=================================*/

	/*=================================
	 * 
	 *        公共控件点击事件开始
	 * 
	 *=================================*/

	/*=================================
	 * 
	 *       公共控件点击事件结束
	 * 
	 *=================================*/
	
	
	/*================================
	 * 
	 *            控件事件重写开始 
	 * 
	 *================================*/
	
	/** 点击事件code */
	private final int ACTION_CLICK = 1; 
	/** 长按事件code */
	private final int ACTION_LONGCLICK = 2;
	/** 列表点击事件code */
	private final int ACTION_ITEMCLICK = 3; 
	
	/** 设置按钮点击事件 */
	protected void setClick(final View view , final String clickMethod ,final Object... args) {
		setActionListener(view, ACTION_CLICK, clickMethod, args);
	}
	/** 设置按钮点击事件 */
	protected void setClick(final View view , final String clickMethod) {
		setClick(view, clickMethod, new Object[0]);
	}
	
	/** 设置按钮长按事件 */
	protected void setLongClick(final View view , final String clickMethod ,final Object... args) {
		setActionListener(view, ACTION_LONGCLICK, clickMethod, args);
	}
	/** 设置按钮长按事件 */
	protected void setLongClick(final View view , final String clickMethod) {
		setLongClick(view, clickMethod, new Object[0]);
	}
	
	/** 设置按钮长按事件 */
	protected void setItemClick(final View view , final String clickMethod , final Object... args) {
		Object[] newArgs;
		if(args != null && args.length > 0) {
			newArgs = new Object[1 + args.length];
			newArgs[0] = (int)0;
			for(int i=1 ; i < newArgs.length; i++) {
				newArgs[i] = args;
			}
		} else {
			newArgs = new Object[1];
			newArgs[0] = (int)0;
		}
		setActionListener(view, ACTION_ITEMCLICK ,clickMethod, newArgs);
	}
	protected void setItemClick(final View view , final String clickMethod) {
		setItemClick(view, clickMethod, new Object[0]);
	}
	
	/** 设置按钮事件 */
	private void setActionListener(final View view , final int actionCode , final String clickMethod ,final Object... args) {
		if(view != null) {
			view.setClickable(true);
			try {
				// 获取参数列表类型
				Class[] cargs = new Class[args == null?0:args.length];
				for(int i=0;i<cargs.length;i++) {
					cargs[i] = args[i].getClass();
				}
				// 获得方法
			    final Method method = this.getClass().getMethod(clickMethod, cargs);
			    if(method != null) {
			    	switch (actionCode) {
						case ACTION_CLICK: {
							view.setOnClickListener(new ActionClick(this,method,args));
						} break;
						case ACTION_LONGCLICK: {
							view.setOnLongClickListener(new ActionLongClick(this, method, args));
						} break;
						case ACTION_ITEMCLICK: {
							if(view instanceof ListView) {
								((ListView)view).setOnItemClickListener(new ActionItemClick(this, method, args));
							} else if(view instanceof GridView) {
								((GridView)view).setOnItemClickListener(new ActionItemClick(this, method, args));
							}
						} break;
					}
			    }
			} catch (Exception e) {
				ExceptionUtil.showException(e);
			}
		}
	}
	/*================================
	 * 
	 *            控件事件重写结束
	 * 
	 *================================*/
	
	
	/*================================
	 * 
	 *          业务逻辑开始
	 *
	 *================================*/
	
	
//	/** 登录成功回调 */
//	public void loginSuccessCallback(Object userInfoBean){};
//	/** 登录取消回调 */
//	public void loginCancleCallback(){};
	
	/*================================
	 * 
	 *          业务逻辑结束
	 *
	 *================================*/
	
	/*================================
	 * 
	 *            页面跳转相关 开始
	 * 
	 *================================ */

	public void toActivity(Class<? extends Activity> descActivityClass) {
		StartActivityHelper.toActivity(this, descActivityClass);
	}

	public void toActivity(Class<? extends Activity> descActivityClass, Intent intent) {
		StartActivityHelper.toActivity(this, descActivityClass, intent);
	}

	public void toActivity(Class<? extends Activity> descActivityClass, int animCode) {
		StartActivityHelper.toActivity(this, descActivityClass, animCode);
	}

	public void toActivity(Class<? extends Activity> descActivityClass, Bundle data, int animCode) {
		StartActivityHelper.toActivity(this, descActivityClass, data , animCode);
	}

	public void toActivity(Class<? extends Activity> descActivityClass, Intent intent, int animCode) {
		StartActivityHelper.toActivity(this, descActivityClass, intent , animCode);
	}
	
	public void toActivityForResult(Class<? extends Activity> descActivityClass,int requestCode) {
		StartActivityHelper.toActivityForResult(this, descActivityClass, requestCode);
	}

	public void toActivityForResult(Class<? extends Activity> descActivityClass, Intent intent ,  int requestCode) {
		StartActivityHelper.toActivityForResult(this, descActivityClass, intent , requestCode);
	}

	public void toActivityForResult(Class<? extends Activity> descActivityClass , int requestCode , int animCode) {
		StartActivityHelper.toActivityForResult(this, descActivityClass, requestCode , animCode);
	}

	public void toActivityForResult(Class<? extends Activity> descActivityClass, Intent intent, int requestCode , int animCode) {
		StartActivityHelper.toActivityForResult(this, descActivityClass, intent , requestCode , animCode);
	}
	
	/*================================
	 * 
	 *            页面跳转相关 结束
	 * 
	 *===============================*/
	
	

	/*================================
	 * 
	 *             对话框相关
	 * 
	 *===============================*/
	public void alertDialog(final String title, final String msg) {
		UIHandler.post(new Runnable() {
			@Override
			public void run() {
				DialogHelper.alert(BaseActivity.this, title, msg);
				AlertDialogPage dialog = new AlertDialogPage(context , AlertDialogPage.AlertDialogStyle.SingleBTN);
				dialog.setContent(msg);
				dialog.setTitle(title);
				dialog.setTitleVisible(true);
				dialog.show();
			}
		});
	}

	public void alertDialog(final String msg) {
		UIHandler.post(new Runnable() {
			@Override
			public void run() {
//				DialogHelper.alert(BaseActivity.this, msg);
				AlertDialogPage dialog = new AlertDialogPage(context , AlertDialogPage.AlertDialogStyle.SingleBTN);
				dialog.setContent(msg);
				dialog.setTitleVisible(false);
				dialog.show();
			}
		});
	}
	
	public void alertToast(final String msg) {
		UIHandler.post(new Runnable() {
			@Override
			public void run() {
				ToastHelper.alert(BaseActivity.this, msg);
			}
		});
	}

	public void alertToast(final String msg, final int time) {
		UIHandler.post(new Runnable() {
			@Override
			public void run() {
				ToastHelper.alert(BaseActivity.this, msg, time);
			}
		});
	}

	/*================================
	 * 
	 *          对话框相关结束
	 *
	 *===============================*/

}
