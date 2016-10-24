package com.honestwalker.androidutils.activity.fragment.menubar;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class BaseMenuFragment extends Fragment {
	
	protected Activity context;
	
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getActivity();
	};
	
	protected void setTitle() {}
	
	/** 登录取消回调 */
	public void loginCancleCallback(){};
	
	/** 登录成功回调 */
	public void loginSuccessCallback(Object userInfoBean){
	};
}

