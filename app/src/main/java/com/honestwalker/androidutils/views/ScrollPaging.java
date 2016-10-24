package com.honestwalker.androidutils.views;

import com.honestwalker.androidutils.IO.LogCat;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class ScrollPaging implements OnScrollListener {

	private int startPosition;
	private int endPosition;
	
	private ScrollPagingListener scrollPagingListener;
	
	private int pageSize = 20;
	
	private boolean lastVisibleActionLock = false;
	
	private boolean allDataLoaded;//确保allDataLoaded()只运行一次
	
	public ScrollPaging(ListView scrollView , int pageSize , ScrollPagingListener scrollPagingListener) {
		this(scrollView, pageSize, 0, scrollPagingListener);
	}
	
	public ScrollPaging(ListView scrollView , int pageSize , int totalResult , ScrollPagingListener scrollPagingListener) {
		this.pageSize = pageSize;
		if(scrollPagingListener != null) {
			scrollPagingListener.setTotalResult(totalResult);
		}
		this.scrollPagingListener = scrollPagingListener;
		scrollView.setOnScrollListener(this);
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		startPosition = firstVisibleItem;
		endPosition = firstVisibleItem + visibleItemCount;
		
		if( scrollPagingListener != null) {
			LogCat.d("PAGING", "totalItemCount=" + totalItemCount + "  visibleItemCount=" + visibleItemCount + 
					"  scrollPagingListener.getTotalResult()=" + scrollPagingListener.getTotalResult());
			if (!allDataLoaded && scrollPagingListener.getTotalResult() > 0 && totalItemCount >= scrollPagingListener.getTotalResult() -1) {
				LogCat.d("PAGING", "allDataLoaded");
				scrollPagingListener.allDataLoaded();
				allDataLoaded = true;
			}
			if (totalItemCount != 0									// 有数据时
					&& visibleItemCount <= totalItemCount 			// 页面大于1页时
					&& scrollPagingListener.getTotalResult() > totalItemCount) // 页面大于1页时 
					{
//				LogCat.d("PAGING", "firstVisibleItem=" + firstVisibleItem + "  visibleItemCount=" + visibleItemCount + 
//						"  totalItemCount=" + totalItemCount);
				if(!lastVisibleActionLock && firstVisibleItem + visibleItemCount >= totalItemCount) {		// 显示倒数第getReciprocalPosition()条时
					scrollPagingListener.lastPositionVisible(scrollPagingListener.getReciprocalPosition() , totalItemCount , scrollPagingListener.getTotalResult());
				} else if(firstVisibleItem + visibleItemCount >= totalItemCount - scrollPagingListener.getReciprocalPosition()){                                                       // 显示到最后一条时
					scrollPagingListener.reciprocalPositionVisible(scrollPagingListener.getReciprocalPosition() , totalItemCount, scrollPagingListener.getTotalResult());
				}
			}
		}
		
	}
	
	public void setLock(boolean lock) {
		this.lastVisibleActionLock = lock;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollPagingListener != null) {
			if(scrollState == 0) {  // 松手时
				scrollPagingListener.scrollStateChanged(startPosition, endPosition);
			}
		}
	}
	
	public void setTotalResult(int totalResult) {
		if(scrollPagingListener != null) {
			scrollPagingListener.setTotalResult(totalResult);
		}
	}

}
