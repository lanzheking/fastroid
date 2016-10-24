package com.honestwalker.androidutils.ViewUtils;

import android.content.Context;
import android.widget.TextView;

import com.honestwalker.androidutils.equipment.DisplayUtil;

public class TextViewRealSizeHelper {
	
	private static TextViewRealSizeHelper instance;
	
	private static Context context;
	
	private TextViewRealSizeHelper() {
		
	}
	
	public static TextViewRealSizeHelper getInstance(Context context) {
		TextViewRealSizeHelper.context = context;
		instance     = new TextViewRealSizeHelper();
		return instance;
	}
	
	/**
	 * 获取textview占用的总行，根据textview的textview和父空间宽度算出行数， 只对多行textview有效
	 * @param parentViewWidth  单位dp
	 */
	public int getTextLineCount(TextView textview,int parentViewWidth) {
		if(textview == null || textview.getText().length() == 0) {
			return 0;
		} else {
			// 内容单行总宽
			int signLineTotalWidth = getTextViewRealWidth(textview);
			if( signLineTotalWidth < parentViewWidth) {
				return 1;
			} else {
				return (signLineTotalWidth / parentViewWidth) + 1 ;
			}
		}
	}
	
	/*
	 * 获取textview实际高度,必须是多行， 单位dp
	 * @param textview
	 * @param parentWidth 父空间宽度 单位dp
	 * @return
	 */ 
	/*private static int getTextViewRealHeightDp(TextView textview,int parentWidthDip) {
		float fixHeight = 0.75f;
		if(textview == null || textview.getText().length() == 0) {
			return 0;
		} else {
			String text = textview.getText().toString();
			Float textSize = textview.getTextSize();
			int pxHeight = (int)(DisplayUtil.px2dip(context , textSize) / fixHeight) * getTextLineCount(textview,parentWidthDip);
			return pxHeight;
		}
	}*/
	
	/**
	 * 获取textview的真实高度，单位dp
	 * @param textview
	 * @param parentViewWidthDip
	 * @return
	 */
	public int getTextViewRealHeightDp(TextView textview,int parentViewWidthDip) {
		float textsize = textview.getTextSize();
		float textsizedp = DisplayUtil.px2dip(context , textsize);
		int   start    = (int)(textsizedp / 0.75f);
		int   lineCount= getTextLineCount(textview, parentViewWidthDip);
		return (int)(start + ((lineCount - 1) * (textsizedp / 0.88f)));
	}
	
	/**
	 * 获取textview的真是高度，单位px
	 * @param textview
	 * @param parentViewWidthDip
	 * @return
	 */
	public int getTextViewRealHeight(TextView textview,int parentViewWidthDip) {
		return DisplayUtil.dip2px(context , getTextLineCount(textview, parentViewWidthDip));
	}
	
	/**
	 * 获取textview的宽度 单位dp
	 * @param textview
	 * @return textview 的实际宽度，单位dp
	 */
	public int getTextViewRealWidth(TextView textview) {
		float fixWidth = 1.9f;
		if(textview == null || textview.getText().length() == 0) {
			return 0;
		} else {
			String text = textview.getText().toString();
			Float textSize = textview.getTextSize();
			int textSizeDip = DisplayUtil.px2dip(context , textSize);
			//int pxWidth = (int)((text.length() * textSizeDip) / fixWidth);
			int pxWidth = (int)((text.length() * (textSizeDip / 1.9)));
			return pxWidth;
		}
	}
	
	/**
	 * 获取textview的宽度像素 单位px
	 * @param textview
	 * @return textview 的实际宽度，单位px
	 */
	public int getTextViewRealWidthPX(TextView textview) {
		float fixWidth = 1.9f;
		if(textview == null || textview.getText().length() == 0) {
			return 0;
		} else {
			String text = textview.getText().toString();
			Float textSize = textview.getTextSize();
			int pxWidth = (int)((text.length() * textSize) / fixWidth);
			return pxWidth;
		}
	}
}
