package com.honestwalker.androidutils.ImageSelector.ImageScan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;


import com.honestwalker.android.fastroid.R;

import java.util.ArrayList;
import java.util.List;

public class ShowImageActivity extends Activity {

	
	private String TAG = "ImageSelector";
	
	private GridView mGridView;
	private List<String> list;
	private ChildAdapter adapter;
	
	private View titleLayout;
  
	private boolean signleSelect = false;
	private int maxSelect = 9;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image_activity);

		signleSelect = getIntent().getBooleanExtra("signleSelect", false);
		maxSelect = getIntent().getIntExtra("maxSelect", 9);

		mGridView = (GridView) findViewById(R.id.child_grid);
		list = getIntent().getStringArrayListExtra("data");

		adapter = new ChildAdapter(this, list, mGridView);
		if(signleSelect) {
			adapter.setSignleSelect();
		}
		adapter.setMaxSelect(maxSelect);
		mGridView.setAdapter(adapter);

		titleLayout = findViewById(R.id.activity_title);
		Display display = this.getWindowManager().getDefaultDisplay();
		try{
			LayoutParams lp = titleLayout.getLayoutParams();
			lp.height = (int) (display.getHeight() * 0.07);
		} catch (Exception e) {}

		Button leftBtn  = (Button) titleLayout.findViewById(R.id.title_left_btn);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(cancleBtnOnClick);
		leftBtn.setText("取消");

		Button rightBtn = (Button) titleLayout.findViewById(R.id.title_right_btn);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setOnClickListener(doneBtnOnClick);
		rightBtn.setText("确定");

	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	private OnClickListener cancleBtnOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};
	
	private OnClickListener doneBtnOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			Log.d(TAG, "adapter.getSelectItems().size()=" + adapter.getSelectItems().size());
			if(adapter.getSelectItems().size() > 0) {
				
				Intent intent = new Intent();
				ArrayList<String> selectPaths = new ArrayList<String>();
				for(Integer i : adapter.getSelectItems()) {
					selectPaths.add(adapter.getItem(i) + "");
				}
				
				intent.putStringArrayListExtra("imgPaths", selectPaths);
				setResult(RESULT_OK , intent);
			} else {
				setResult(RESULT_CANCELED);
			}
			finish();
		
		}
	};
	

}
