package com.honestwalker.androidutils.commons.adapter;

import com.honestwalker.androidutils.equipment.DisplayUtil;

import android.content.Context;
import android.view.View;

public class BaseViewHolder {
	
	private View baseView;
	protected Context mContext;
	protected int screenWidth;
	protected int screenHeight;
	
	public BaseViewHolder(View baseView) {
		this.baseView = baseView;
		this.mContext = baseView.getContext();
		this.screenWidth = DisplayUtil.getWidth(mContext);
		this.screenHeight = DisplayUtil.getHeight(mContext);
	}
	
	public View findViewById(int id) {
		return baseView.findViewById(id);
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
