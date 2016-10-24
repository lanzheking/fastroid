package com.stay.pull.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;

import com.stay.pull.lib.PullToRefreshAdapterViewBase;

/**
 * 
 * 有分页的下拉刷新的listView(用例参看TestListActivity)
 *
 */
public class PullToRefreshPageListView extends PullToRefreshAdapterViewBase<SmartPageListView>{

	public PullToRefreshPageListView(Context context) {
		super(context);
		this.setDisableScrollingWhileRefreshing(false);
	}

	public PullToRefreshPageListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDisableScrollingWhileRefreshing(false);
	}

	public PullToRefreshPageListView(Context context, int mode) {
		super(context, MODE_PULL_DOWN_TO_REFRESH);
		this.setDisableScrollingWhileRefreshing(false);
	}

	@Override
	protected SmartPageListView createRefreshableView(Context context,
			AttributeSet attrs) {
		SmartPageListView lv = new SmartPageListView(context, attrs);
		lv.setId(android.R.id.list);
		return lv;
	}

	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return ((SmartPageListView)getRefreshableView()).getContextMenuInfo();
	}

}
