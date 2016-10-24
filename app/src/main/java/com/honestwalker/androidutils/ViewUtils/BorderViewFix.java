package com.honestwalker.androidutils.ViewUtils;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.equipment.DisplayUtil;

public class BorderViewFix {
	public static void fixBorder(final Context context , final ListView listView) {
		UIHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				for(int i=0;i<listView.getChildCount();i++) {
					if(i > 0) {
						View view = listView.getChildAt(i);
						LogCat.d("AAAAAA", "height=" + view.getHeight());
						view.layout(view.getLeft(), view.getTop() - DisplayUtil.dip2px(context , 2), view.getRight(), view.getBottom());
					}
				}
			}
		} , 130);
	}
}