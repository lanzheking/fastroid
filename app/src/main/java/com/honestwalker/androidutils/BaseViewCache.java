package com.honestwalker.androidutils;

import android.view.View;

public class BaseViewCache {

	private View baseView;
	
	public BaseViewCache(View baseView) {
		this.baseView = baseView;
	}
	
	public View findViewById(View view,int id) {
		if(this.baseView != null) {
			if(view == null) {
				view = baseView.findViewById(id);
			}
			return view;
		}
		return null;
	}
	

}
