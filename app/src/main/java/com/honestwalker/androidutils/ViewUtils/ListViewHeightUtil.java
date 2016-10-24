package com.honestwalker.androidutils.ViewUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/**
 * Depiction:
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-3-2 下午1:52. <br />
 * Rewrite Date :  16-3-2 下午1:52. <br />
 */
public class ListViewHeightUtil {

    public void setListViewHeightBasedOnChildren(ViewGroup view , ListAdapter adapter, int attHeight) {
        ListAdapter listAdapter = adapter;
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, view);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = totalHeight + (1 * (listAdapter.getCount() - 1)) + attHeight;
        view.setLayoutParams(params);
    }

}
