package com.honestwalker.androidutils.activity.menubar;

import com.honestwalker.androidutils.IO.LogCat;

import android.app.Activity;

public abstract class MenubarTabpageActivity extends Activity {
	
	protected MenubarTabpageActivity context;
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		context = this;
		initView();
	}
	
	/** 页面控件初始化 */
	protected abstract void initView();
	
}
