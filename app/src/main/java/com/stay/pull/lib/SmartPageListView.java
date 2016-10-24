package com.stay.pull.lib;

import android.R.color;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.views.ScrollPaging;
import com.honestwalker.androidutils.views.ScrollPagingListener;

/**
 * 
 *  封装的有分页的listView,只需手动调用3个方法 <br/><br/>
 *  
 *  （1）初始化时调用{@link #setSmartParams(boolean, ListAdapter, OnLastListItemVisibleListener)} <br/>
 *  （2）每次载新一页数据时调用{@link #finishLoadingData()} <br/>
 *  （3）得到数据总条目数时调用{@link com.honestwalker.androidutils.views.ScrollPaging #setTotalResult(int)} <br/>
 *  
 */
public class SmartPageListView extends ListView {

	Context mContext;

	FootLoadingView footLoadingView;
	
	ScrollPaging scrollPaging;
	
	class FootLoadingView extends LinearLayout {

		Context context;
		private View finishLoadingTV;
		private View loadingView;
		
		public FootLoadingView(Context context) {
			this(context, null);
		}
		
		public FootLoadingView(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}
		
		public FootLoadingView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			this.context = context;
			init(context);
		}

		private void init(Context context) {
			inflate(context, R.layout.view_footer_loading_default, this);
			finishLoadingTV = findViewById(R.id.finish_loading_tv);
			loadingView = findViewById(R.id.loading_layout);
			reset();
		}
		
		public void reset(){
			loadingView.setVisibility(View.VISIBLE);
			finishLoadingTV.setVisibility(View.INVISIBLE);
		}
		
		public void notifyAllDataLoaded(){
			loadingView.setVisibility(View.INVISIBLE);
			finishLoadingTV.setVisibility(View.VISIBLE);
		}
	}
	
	public SmartPageListView(Context context) {
		super(context);
		initListView(context);
	}

	public SmartPageListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initListView(context);
	}

	public SmartPageListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initListView(context);
	}

	public ContextMenuInfo getContextMenuInfo() {
		return super.getContextMenuInfo();
	}
	
	private void initListView(Context context) {
		mContext = context;
		setCacheColorHint(color.transparent);
		setFadingEdgeLength(0);
	}

	/**
	 * @param showLoadingFooter 显示loading
	 * @param adapter listView的适配器
	 * @param onLastListItemVisibleListener 在此回调中处理加载下一页数据 <br/>
	 * 
	 * @return ScrollPaging 返回ScrollPaging对象，在数据返回时，需要调用setTotalResult
	 */
	public ScrollPaging setSmartParams(boolean showLoadingFooter,ListAdapter adapter,OnLastListItemVisibleListener onLastListItemVisibleListener){
		if (showLoadingFooter) {
			footLoadingView = new FootLoadingView(mContext); 
			addFooterView(footLoadingView, null, false);
			footLoadingView.setVisibility(View.GONE);
		}
		setAdapter(adapter);
		
		scrollPaging = new ScrollPaging(this, 0, new SmartScrollPagingListener(onLastListItemVisibleListener));

		return scrollPaging;
	}
	
	/**
	 * 在每一次返回数据后调用此方法，隐藏Loading和开滑动锁
	 */
	public void finishLoadingData(){
		loadData(false);
	}

	private void startLoadingData(){
		loadData(true);
	}
	
	private void loadData(boolean loadingVisible){
		scrollPaging.setLock(loadingVisible);		
		if (footLoadingView != null) {
			if (loadingVisible) {
				footLoadingView.setVisibility(View.VISIBLE);
			}else {
				footLoadingView.setVisibility(View.GONE);
			}
		}
	}
	
	public interface OnLastListItemVisibleListener{
		void onLastItemVisible();
	}
	
	/**
	 * 分页
	 */
	private class SmartScrollPagingListener extends ScrollPagingListener {
		
		OnLastListItemVisibleListener onLastListItemVisibleListener;
		
		public SmartScrollPagingListener(
				OnLastListItemVisibleListener onLastListItemVisibleListener) {
			this.onLastListItemVisibleListener = onLastListItemVisibleListener;
		}

		@Override
		public void scrollStateChanged(int startPosition, int endPosition) {
		}
		
		@Override
		public void reciprocalPositionVisible(int reciprocalPosition, int totalItemCount, int totalResult) {
		}
		
		@Override
		public void lastPositionVisible(int position, int totalItemCount, int totalResult) {
			startLoadingData();
			if (onLastListItemVisibleListener != null) {
				onLastListItemVisibleListener.onLastItemVisible();
			}
		}
		
		@Override
		public void allDataLoaded() {
			footLoadingView.setVisibility(View.VISIBLE);
			footLoadingView.notifyAllDataLoaded();
		}
	};
	
}
