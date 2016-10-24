package com.honestwalker.androidutils.commons.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.honestwalker.androidutils.equipment.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMounter<T> {
	
	protected Context mContext;
	
	protected List<T> data = new ArrayList<T>();
	
	private LayoutInflater inflater;
	
	protected List<View> views = new ArrayList<View>();
	
	private OnItemClickListener onItemClickListener;
	
	private int viewResId;
	
	protected int screenWidth;
	protected int screenHeight;

	public BaseMounter(Context mContext , int viewResId ,List<T> data) {
		this.data.addAll(data);
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		this.viewResId = viewResId;
		this.screenWidth  = DisplayUtil.getWidth(mContext);
		this.screenHeight = DisplayUtil.getHeight(mContext);
	}
	protected abstract void addItemData(View view , T item,int position);
	
	protected void onStartCreateView() {}
	
	protected void onCompleteCreateView() {}
	
	public List<View> getViews(LinearLayout linearLayout){
		getViews();
		for(View v : views) {
			linearLayout.addView(v);
//			LineView lineView = new LineView(mContext);
//			lineView.setBackgroundColor(mContext.getResources().getColor(R.color.border));
//			linearLayout.addView(lineView);
		}
		return views;
	}
	
	public List<View> getViews() {
		
		if(views.size() > 0) {
			return views;
		}
		
		int position = 0;
		
		onStartCreateView();
		
		for (T t : data) {
			View view = inflater.inflate(viewResId, null); 
			
			view.setClickable(true);
			
			addItemData(view, t, position);
			
			supportOnItemClick(view, position);
			
			views.add(view);

			position++;
		}
		
		onCompleteCreateView();

		return views;
	}

	protected OnItemClickListener getOnItemClickListener() {
		return this.onItemClickListener;
	}
	
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
		if(views.size() > 0) {
			int position = 0;
			for(View v : views) {
				supportOnItemClick(v, position);
				position++;
			}
		}
	}
	
	public void supportOnItemClick(View layout , int position) {
		layout.setOnClickListener(new ItemClick(position) {
			@Override
			public void onItemClick(View arg0, int position) {
				if(onItemClickListener != null) {
					onItemClickListener.onItemClick(null, arg0, position, 0);
				}
			}
		});
	}
	
	private abstract class ItemClick implements OnClickListener {

		int position;

		private ItemClick(int position) {
			this.position = position;
		}

		public abstract void onItemClick(View v, int position);

		@Override
		public void onClick(View v) {
			onItemClick(v, position);
		}

	}
	
	/** 数据长度 */
	public int getCount() {
		if(data == null) {
			return 0;
		} else {
			return data.size();
		}
	}
	
	protected String getString(int res) {
		return mContext.getString(res);
	}

}