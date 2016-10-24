package com.honestwalker.androidutils.ImageSelector.ImageScan;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.honestwalker.androidutils.ImageSelector.ImageScan.MyImageView.OnMeasureListener;
import com.honestwalker.android.fastroid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChildAdapter extends BaseAdapter {
	
	private Context mContext;
	private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
	/**
	 * 用来存储图片的选中情况
	 */
	private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
	private HashMap<Integer, CheckBox> mCheckBoxtMap = new HashMap<Integer, CheckBox>();
	private GridView mGridView;
	private List<String> list;
	protected LayoutInflater mInflater;
	
	private boolean signleSelect = false;
	private int maxSelect = 9;

	public ChildAdapter(Context context, List<String> list, GridView mGridView) {
		this.mContext = context;
		this.list = list;
		this.mGridView = mGridView;
		mInflater = LayoutInflater.from(context);
	}
	
	/** 设置为单选 */
	public void setSignleSelect() {
		signleSelect = true;
	}
	
	public void setMaxSelect(int maxSelect) {
		this.maxSelect = maxSelect;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		String path = list.get(position);
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.grid_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
			
			if(signleSelect) {  // 单选模式下不显示多选按钮
				viewHolder.mCheckBox.setVisibility(View.GONE);
				viewHolder.mImageView.setOnClickListener(new ImageViewOnClick(position));
			}
			
			//用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
				
				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		viewHolder.mImageView.setTag(path);
		viewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked && mCheckBoxtMap.size() >= maxSelect) {
					viewHolder.mCheckBox.setChecked(false);
					return;
				}
				//如果是未选中的CheckBox,则添加动画
				if(!mSelectMap.containsKey(position) || !mSelectMap.get(position)){
					addAnimation(viewHolder.mCheckBox);
				}
				mSelectMap.put(position, isChecked);
				if(isChecked) {
					if(signleSelect) {  // 单选时 去掉别的选择
						Iterator<Map.Entry<Integer, CheckBox>> iter = mCheckBoxtMap.entrySet().iterator();
						while(iter.hasNext()) {
							Map.Entry<Integer, CheckBox> ent = iter.next();
							mCheckBoxtMap.remove(ent.getKey());
							ent.getValue().setChecked(false);
							
						}
					}
					mCheckBoxtMap.put(position, viewHolder.mCheckBox);
				} else {
					mCheckBoxtMap.remove(position);
				}
			}
		});
		
		viewHolder.mCheckBox.setChecked(mSelectMap.containsKey(position) ? mSelectMap.get(position) : false);
		
		//利用NativeImageLoader类加载本地图片
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageLoader.NativeImageCallBack() {
			
			@Override
			public void onImageLoader(Bitmap bitmap, String path) {
				ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
				if(bitmap != null && mImageView != null){
					mImageView.setImageBitmap(bitmap);
				}
			}
		});
		
		if(bitmap != null){
			viewHolder.mImageView.setImageBitmap(bitmap);
		}else{
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		
		return convertView;
	}
	
	/**
	 * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画 
	 * @param view
	 */
	private void addAnimation(View view){
		float [] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
				set.setDuration(150);
		set.start();
	}
	
	
	/**
	 * 获取选中的Item的position
	 * @return
	 */
	public List<Integer> getSelectItems(){
		List<Integer> list = new ArrayList<Integer>();
		for(Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext();){
			Map.Entry<Integer, Boolean> entry = it.next();
			if(entry.getValue()){
				list.add(entry.getKey());
			}
		}
		
		return list;
	}
	
	
	public static class ViewHolder{
		public MyImageView mImageView;
		public CheckBox mCheckBox;
	}

	private class ImageViewOnClick implements OnClickListener {

		private int position;
				
		public ImageViewOnClick(int position) {
			this.position = position;
		}
		
		@Override
		public void onClick(View arg0) {
			
			Intent intent = new Intent();
			ArrayList<String> selectPaths = new ArrayList<String>();
			selectPaths.add(list.get(position));
			intent.putStringArrayListExtra("imgPaths", selectPaths);
			((Activity)mContext).setResult( ((Activity)mContext).RESULT_OK , intent);
			((Activity)mContext).finish();
			
		}
		
	}


}
