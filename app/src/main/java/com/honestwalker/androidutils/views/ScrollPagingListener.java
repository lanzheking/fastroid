package com.honestwalker.androidutils.views;

public abstract class ScrollPagingListener {
	
	/** 指定显示倒数回调位置 ， 比如设置5就是列表显示到倒数第5条后调用reciprocalPositionVisiled */
	private int reciprocalPosition = 0;
	/** 列表总记录数 */
	private int totalResult;
	
	public ScrollPagingListener(){
		this.reciprocalPosition = 0;
		this.totalResult = 0;
	}
	public ScrollPagingListener(int reciprocalPosition , int totalResult) {
		this.reciprocalPosition = reciprocalPosition;
		this.totalResult = totalResult;
	}
	public int getReciprocalPosition() {
		return this.reciprocalPosition;
	}
	public int getTotalResult() {
		return this.totalResult;
	}
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	/** 一次滑动事件结束后回调，就是按下滑动到抬手调用一次 */
	public abstract void scrollStateChanged(int startPosition,int endPosition);
	
	/** 滚动到指定倒数位置调用 */
	public abstract void reciprocalPositionVisible(int reciprocalPosition , int totalItemCount , int totalResult);
	
	/** 滚到底部调用 */
	public abstract void lastPositionVisible(int position , int totalItemCount , int totalResult);
	
	/** 数据全部加载完时调用 */
	public abstract void allDataLoaded();
}
