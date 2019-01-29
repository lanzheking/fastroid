package com.honestwalker.android.commons.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.honestwalker.android.commons.BaseApplication;
import com.honestwalker.android.commons.config.ContextProperties;
import com.honestwalker.android.commons.utils.StartActivityHelper;
import com.honestwalker.android.spring.core.inject.Injection;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.honestwalker.androidutils.equipment.SDCardUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.io.File;

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
		init();
		loadData();
	}

	@Override
	protected void onStart() {
		super.onStart();
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
        Injection.inject(this);

		if (viewSizeHelper == null) {
			viewSizeHelper = ViewSizeHelper.getInstance(this);
		}
		if (screenWidth == 0) {
			screenWidth = DisplayUtil.getWidth(context);
			screenHeight = DisplayUtil.getHeight(context);
			statusBarHeight = DisplayUtil.getStatusBarHeight(context);
		}
		if (inflater == null) {
			inflater = getLayoutInflater();
		}
	}

	protected abstract void loadData();

	private View layoutResView;
	public View getLayoutResView(){return layoutResView;}
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
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

	public String getSDImageCachePath() {
		return SDCardUtil.getSDRootPath() + ContextProperties.getConfig().sdcartCacheName + "/image/";
	}

	public void clearImageCache() {
		LogCat.d("CACHE", "删除内存卡图片缓存");
		File imageCachePath = new File(getImageCachePath());
		File[] files = imageCachePath.listFiles();
		if(files != null)  {
			for (File file : files) {
				try {
					deleteImage(file.getPath());
				} catch (Exception e) {
					ExceptionUtil.showException(e);
				}
			}
		}
	}

	public void clearSDImageCache() {
		LogCat.d("CACHE", "删除sd卡图片缓存");
        File imageCachePath = new File(getSDImageCachePath());
        File[] files = imageCachePath.listFiles();
		if(files != null)  {
			for (File file : files) {
				try {
					deleteImage(file.getPath());
				} catch (Exception e) {
					ExceptionUtil.showException(e);
				}
			}
		}
    }

	/**
	 * 删除图片方法，删除图片要同时删除uri
	 * @param imgPath
     */
	public void deleteImage(String imgPath) {
		try {
			ContentResolver resolver = this.getContentResolver();
			Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=?",
					new String[] { imgPath }, null);
			if (cursor.moveToFirst()) {
				long id = cursor.getLong(0);
				Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				Uri uri = ContentUris.withAppendedId(contentUri, id);
				this.getContentResolver().delete(uri, null, null);
			} else {
				try {
					File file = new File(imgPath);
					file.delete();
				} catch (Exception e) {}
			}
		} catch (Exception e) {}

	}

	public Intent getIntent() {
		return super.getIntent() == null ? new Intent():super.getIntent();
	}
	
	/*=================================
	 * 
	 *             公共控件操作
	 * 
	 *=================================*/

//	public void loading(final boolean show) {
//		if(show) {
//			Loading.show(context , "loading_cancelunable");
//		} else {
//			Loading.dismiss(context);
//		}
//	}
//
//	public void loadingCancelAble(final boolean show) {
//		if(show) {
//			Loading.show(context , "loading_cancelable");
//		} else {
//			Loading.dismiss(context);
//		}
//	}
	
	/*=================================
	 *
	 *         公共控件操作结束
	 *
	 *=================================*/

}
