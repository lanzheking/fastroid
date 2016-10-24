package com.honestwalker.androidutils.ImageSelector.ImageScan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.honestwalker.androidutils.ImageSelector.ImageSelector;
import com.honestwalker.android.fastroid.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @blog http://blog.csdn.net/xiaanming
 * 
 * @author xiaanming
 * 
 *
 */
public class MultiImageSelectorActivity extends Activity {
	
	private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
	private List<ImageBean> list = new ArrayList<ImageBean>();
	private final static int SCAN_OK = 1;
	private ProgressDialog mProgressDialog;
	private GroupAdapter adapter;
	private GridView mGroupGridView;
	
	private boolean signleSelect = false;
	private int maxSelect = 9;
	
	private View titleLayout;
	private Button titleLeftBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_scan);
		
		mGroupGridView = (GridView) findViewById(R.id.main_grid);
		
		getImages();
		
		signleSelect = getIntent().getBooleanExtra("signleSelect", false);
		maxSelect = getIntent().getIntExtra("maxSelect", 9);
		
		mGroupGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				List<String> childList = mGruopMap.get(list.get(position).getFolderName());
				
				Intent mIntent = new Intent(MultiImageSelectorActivity.this, ShowImageActivity.class);
				mIntent.putStringArrayListExtra("data", (ArrayList<String>)childList);
				mIntent.putExtra("signleSelect", signleSelect);
				mIntent.putExtra("maxSelect", maxSelect);
				startActivityForResult(mIntent , ImageSelector.REQUEST_MULTI_IMAGE_SELECT);
				
			}
		});
		
		titleLayout = findViewById(R.id.activity_title);
		Display display = this.getWindowManager().getDefaultDisplay();
		try{
			LayoutParams lp = titleLayout.getLayoutParams();
			lp.height = (int) (display.getHeight() * 0.07);
		} catch (Exception e) {}
		titleLeftBtn = (Button) titleLayout.findViewById(R.id.title_left_btn);
		titleLeftBtn.setText("取消");
		titleLeftBtn.setVisibility(View.VISIBLE);
		titleLeftBtn.setOnClickListener(cancleBtnOnClick);
		
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SCAN_OK:
				//关闭进度条
				mProgressDialog.dismiss();
				
				adapter = new GroupAdapter(MultiImageSelectorActivity.this, list = subGroupOfImage(mGruopMap), mGroupGridView);
				mGroupGridView.setAdapter(adapter);
				break;
			}
		}
		
	};

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	private void getImages() {
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		
		//显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = MultiImageSelectorActivity.this.getContentResolver();

				//只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);
				
				while (mCursor.moveToNext()) {
					try {
						//获取图片的路径
						String path = mCursor.getString(mCursor
								.getColumnIndex(MediaStore.Images.Media.DATA));
						
						//获取该图片的父路径名
						String parentName = new File(path).getParentFile().getName();
						
						
						//根据父路径名将图片放入到mGruopMap中
						if (!mGruopMap.containsKey(parentName)) {
							List<String> chileList = new ArrayList<String>();
							chileList.add(path);
							mGruopMap.put(parentName, chileList);
						} else {
							mGruopMap.get(parentName).add(path);
						}
					} catch (Exception e) {
					}
				}
				
				mCursor.close();
				
				//通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);
				
			}
		}).start();
		
	}
	
	
	/**
	 * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
	 * 所以需要遍历HashMap将数据组装成List
	 * 
	 * @param mGruopMap
	 * @return
	 */
	private List<ImageBean> subGroupOfImage(HashMap<String, List<String>> mGruopMap){
		if(mGruopMap.size() == 0){
			return null;
		}
		List<ImageBean> list = new ArrayList<ImageBean>();
		
		Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = it.next();
			ImageBean mImageBean = new ImageBean();
			String key = entry.getKey();
			List<String> value = entry.getValue();
			
			mImageBean.setFolderName(key);
			mImageBean.setImageCounts(value.size());
			mImageBean.setTopImagePath(value.get(0));//获取该组的第一张图片
			
			list.add(mImageBean);
		}
		
		return list;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(RESULT_OK == resultCode) {
			setResult(resultCode , data);
			finish();
		}
	}
	
	private OnClickListener cancleBtnOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	};

}
