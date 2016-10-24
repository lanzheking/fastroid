package com.honestwalker.androidutils.ViewUtils;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.equipment.DisplayUtil;

public class ListViewBorderFixed {
	public static void fix(final Context context , final ListView listview) {
		UIHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				int margin = DisplayUtil.dip2px(context , 2);
				for(int i=1; i < listview.getChildCount() ;i++) {
					View view = listview.getChildAt(i);
					view.layout(view.getLeft(), view.getTop() - margin, view.getWidth(), 
							view.getTop() + view.getHeight() - margin);
				}
			}
		}, 80);
	}
}
