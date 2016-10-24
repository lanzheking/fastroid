package com.honestwalker.androidutils.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;

public abstract class DialogPage {

	private Dialog   dialog;
	protected Activity context;
	private View 	  parentView;
	private View 	  contentView;
	private TextView titleTV;
	private View 	  dismissView;
	private LinearLayout  contentLayout;
	private View      titleLayout;

	private int theme = -1;

	public DialogPage(Activity context, int resLayoutId) {
		this(context, context.getLayoutInflater().inflate(resLayoutId, null) , R.style.custom_dialog_style);
	}

	public DialogPage(Activity context, int resLayoutId , int theme) {
		this(context, context.getLayoutInflater().inflate(resLayoutId, null) , theme);
	}
	
	public DialogPage(Activity context, View contentView , int theme) {
		this.theme = theme;
		this.context = context;
		this.contentView = contentView;
		init();
	}
	
	private void init() {
		
		dialog = new Dialog(context, theme);

		parentView = context.getLayoutInflater().inflate(R.layout.dialogpage_base, null);

		titleLayout = parentView.findViewById(R.id.dialogpage_title_layout);
		titleTV = (TextView) parentView.findViewById(R.id.dialogpage_title);
		
		dismissView = parentView.findViewById(R.id.dialogpage_dismiss);
		dismissView.setOnClickListener(dismissOnClick);

		contentLayout = (LinearLayout) parentView.findViewById(R.id.dialogpage_content);
		
		dialog.setContentView(parentView);
		dialog.setCanceledOnTouchOutside(false);
		contentLayout.addView(contentView);
		
		initView();
		
	}
	
	/**
	 * 设置列表框标题
	 * @param title
	 */
	public void setTitle(String title) {
		titleTV.setText(title == null ? "" : title);
	}
	
	/**
	 * 显示对话框
	 */
	public void show() {
		displayContent();
		if(dialog == null) return;
		if(!dialog.isShowing()) {
			try {
				dialog.show();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 隐藏对话框
	 */
	public void dismiss() {
		if(dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	
	private OnClickListener dismissOnClick = new OnClickListener() {
		public void onClick(View v) {
			dismiss();
		};
	};
	
	/**
	 * contentView控件初始化
	 */
	protected abstract void initView();

	protected abstract void displayContent();

	/**
	 * 从contentView 获取 view
	 * @param id
	 * @return
	 */
	protected View findViewById(int id) {
		return contentView.findViewById(id);
	}

	public void showDismissView(boolean show) {
		if(show) {
			dismissView.setVisibility(View.VISIBLE);
		} else {
			dismissView.setVisibility(View.GONE);
		}
	}

//	public void setTitleBgColor(int bgColorRes) {
//
//	}

	public void setWidth(int width) {
		ViewSizeHelper.getInstance(context).setWidth(contentView , width);
	}

	protected Dialog getDialog() {
		return dialog;
	}

	public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
		dialog.setOnDismissListener(listener);
	}

	public void setTitleVisible(boolean show) {
		if(show) {
			titleLayout.setVisibility(View.VISIBLE);
		} else {
			titleLayout.setVisibility(View.GONE);
		}
	}

	public View getContentView() {
		return contentView;
	}
}
