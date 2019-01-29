package com.honestwalker.androidutils.ImageSelector.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.equipment.DisplayUtil;

/**
 * 自定义对话框 
 */
public class CustomerDialog extends Dialog implements OnClickListener {
	
	private Context context;
	
	private RelativeLayout titleLayout;
	private Button firstBTN;
	private Button secondBTN;
	private Button thirdBTN;
	
	private TextView titleTV;
	
	private int screenWidth;
	
	private ThreeBtnsDialogListener listener;
	
	private String firstTxt = "";
	private String secondTxt = "";
	private String thirdTxt = "";
	
	private String title;
	
	private boolean showAnim;

	public CustomerDialog(Context context, boolean showAnim) {
		super(context , R.style.input_dialog_styles);
		this.context = context;
		this.showAnim = showAnim;
	}
	
	public CustomerDialog(Context context, int theme, boolean showAnim){
        super(context, theme);
        this.context = context;
        this.showAnim = showAnim;
    }
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_three_btns_dialog);
        init();
        if (showAnim) {
        	setAnimation();
		}
    }
	
	private void setAnimation() {
		Window window = this.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setWindowAnimations(R.style.dialogWindowAnim);
	}

	private void init() {
		screenWidth  = DisplayUtil.getWidth(context);
		
		titleLayout  = (RelativeLayout) findViewById(R.id.layout1);
		ViewSizeHelper.getInstance(context).setWidth(titleLayout, (int)(screenWidth * 0.85));
		this.setCanceledOnTouchOutside(false);
		
		titleTV   = (TextView) findViewById(R.id.textview1);
		
		firstBTN  = (Button) findViewById(R.id.btn1);
		firstBTN.setOnClickListener(this);
		
		secondBTN = (Button) findViewById(R.id.btn2);
		secondBTN.setOnClickListener(this);

		thirdBTN    = (Button) findViewById(R.id.btn3);
		thirdBTN.setOnClickListener(this);
		
		firstBTN.setText(firstTxt);
		secondBTN.setText(secondTxt);
		thirdBTN.setText(thirdTxt);
		
		titleTV.setText(title);
		
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
	}
	
	public CustomerDialog setCustomerDialogListener(ThreeBtnsDialogListener listener) {
		this.listener = listener;
		return this;
	}
	
	@Override
	public void onClick(View view) {
		
		if(listener == null) {
			defaultCancelAction();
			return;
		}

		if(view.getId() == R.id.btn1) {
			listener.onFirstClick();
		} else if(view.getId() == R.id.btn2) {
			listener.onSecondClick();
		} else if(view.getId() == R.id.btn3) {
			listener.onThirdClick();
		}

		this.dismiss();
		
	}
	
	/** 
	 * 设置内容
	 */
	public CustomerDialog setContent(String title, String firstTxt , String secondTxt, String thirdTxt) {
		this.title = title;
		this.firstTxt = firstTxt;
		this.secondTxt = secondTxt;
		this.thirdTxt = thirdTxt;
		return this;
	}
	
	private void defaultCancelAction() {
		this.dismiss();
	}
	
	public interface ThreeBtnsDialogListener{
		public void onFirstClick();
		public void onSecondClick();
		public void onThirdClick();
	}
}
