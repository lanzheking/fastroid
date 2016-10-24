package com.honestwalker.androidutils.activity.fragment.menubar;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.views.BaseMyViewLinearLayout;
import com.honestwalker.androidutils.views.QtyView;

public class Menubar extends BaseMyViewLinearLayout {

	public Menubar(Context context) {
		super(context);
		init();
	}
	public Menubar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public Menubar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected void init() {
		View view = inflater.inflate(R.layout.view_menubar, null);
	}

	@Override
	protected int contentViewLayout() {
		return R.layout.view_menubar;
	}

}
