package com.honestwalker.android.commons.title;


import android.content.res.ColorStateList;
import android.view.View.OnClickListener;

import com.honestwalker.android.commons.activity.BaseActivity;

public class TitleArgBuilder {

	/** 获取文本和右按钮的title */
	public static TitleArg getRightBtnTitle(String titleStr ,
											String titleRightBtnStr , OnClickListener titleRightBtnClick) {
		TitleArg titleArg = new TitleArg();
		titleArg.setIsTitleBarVisible(true);
		titleArg.setTitleTVStr(titleStr);
		titleArg.setRightBtnVisible(true);
		titleArg.setRightBtnClickListener(titleRightBtnClick);
		titleArg.setRightBtnStr(titleRightBtnStr);

		return titleArg;
	}


	/** 获取只有文本的title */
	public static TitleArg getTitle(String titleStr) {
		TitleArg titleArg = new TitleArg();
		titleArg.setIsTitleBarVisible(true);
		titleArg.setTitleTVStr(titleStr);
		titleArg.setLeftBackVisible(false);
		titleArg.setRightBtnVisible(false);

		return titleArg;
	}

	/** 获取带后退按钮的title */
	public static TitleArg getBackBtnTitle(BaseActivity context ,
										   String titleStr) {

		TitleArg titleArg = new TitleArg();
		titleArg.setIsTitleBarVisible(true);
		titleArg.setTitleTVStr(titleStr);
		titleArg.setLeftBackVisible(true);
		titleArg.setLeftBackClickListener(context.backBtnClick);

		return titleArg;
	}

	/** 获取带后退按钮和右按钮的title */
	public static TitleArg getBackAndRightTVTitle(BaseActivity context ,
												  String titleStr, String titleRightBtnStr ,
												  OnClickListener titleRightBtnClick) {

		TitleArg titleArg = new TitleArg();
		titleArg.setIsTitleBarVisible(true);
		titleArg.setTitleTVStr(titleStr);
		titleArg.setLeftBackVisible(true);
		titleArg.setLeftBackClickListener(context.backBtnClick);
		titleArg.setRightBtnVisible(true);
		titleArg.setRightBtnClickListener(titleRightBtnClick);
		titleArg.setRightBtnStr(titleRightBtnStr);
		return titleArg;
	}

	/** 获取带后退按钮和右图标的title */
	public static TitleArg getBackAndRightIconTitle(BaseActivity context ,
													String titleStr, int rightIconRes , OnClickListener titleRightBtnClick) {

		TitleArg titleArg = new TitleArg();
		titleArg.setIsTitleBarVisible(true);
		titleArg.setTitleTVStr(titleStr);
		titleArg.setLeftBackVisible(true);
		titleArg.setLeftBackClickListener(context.backBtnClick);
		titleArg.setRightBtnVisible(true);
		titleArg.setRightBtnClickListener(titleRightBtnClick);
		titleArg.setRightIconRes(rightIconRes);
		return titleArg;
	}

	public static TitleArg getLeftAndRightTextTitle(String titleStr,
													String leftText , OnClickListener titleLeftBtnClick,
													String rightText , OnClickListener titleRightBtnClick) {
		TitleArg titleArg = new TitleArg();
		titleArg.setIsTitleBarVisible(true);
		titleArg.setTitleTVStr(titleStr);
		titleArg.setLeftBackVisible(true);
		titleArg.setLeftBtnStr(leftText);
		titleArg.setLeftBackClickListener(titleLeftBtnClick);
		titleArg.setRightBtnVisible(true);
		titleArg.setRightBtnClickListener(titleRightBtnClick);
		titleArg.setRightBtnStr(rightText);
		return titleArg;
	}

	/** 获取左右自定义图标的title */
	public static TitleArg getLeftAndRightIconTitle(
			String titleStr,
			int leftIconRes , OnClickListener titleLeftBtnClick,
			int rightIconRes , OnClickListener titleRightBtnClick) {

		TitleArg titleArg = new TitleArg();
		titleArg.setIsTitleBarVisible(true);
		titleArg.setTitleTVStr(titleStr);
		titleArg.setLeftBackVisible(true);
		titleArg.setLeftIconRes(leftIconRes);
		titleArg.setLeftBackClickListener(titleLeftBtnClick);
		titleArg.setRightBtnVisible(true);
		titleArg.setRightBtnClickListener(titleRightBtnClick);
		titleArg.setRightIconRes(rightIconRes);
		return titleArg;
	}

	/** 获取右图标和标题*/
	public static TitleArg getRightIconTitle(String titleStr,
											 int rightIconRes, OnClickListener onClickListener){
		TitleArg titleArg = new TitleArg();
		titleArg.setIsTitleBarVisible(true);
		titleArg.setRightBtnVisible(true);
		titleArg.setLeftBackVisible(false);
		titleArg.setRightBtnClickListener(onClickListener);
		titleArg.setTitleTVStr(titleStr);
		titleArg.setRightIconRes(rightIconRes);

		return titleArg;
	}

	/** 获取左右和中间自定义图标的title */
	public static TitleArg getLeftRightMiddleIconTitle(
			int middleIconRes,
			int leftIconRes , OnClickListener titleLeftBtnClick,
			int rightIconRes , OnClickListener titleRightBtnClick) {

		TitleArg titleArg = new TitleArg();
		titleArg.setIsTitleBarVisible(true);
		titleArg.setMiddleIconRes(middleIconRes);
		titleArg.setLeftBackVisible(true);
		titleArg.setLeftIconRes(leftIconRes);
		titleArg.setLeftBackClickListener(titleLeftBtnClick);
		titleArg.setRightBtnVisible(true);
		titleArg.setRightBtnClickListener(titleRightBtnClick);
		titleArg.setRightIconRes(rightIconRes);
		return titleArg;
	}

	/** 获取中间自定义图标的title */
	public static TitleArg getMiddleIconTitle(
			int middleIconRes) {

		TitleArg titleArg = new TitleArg();
		titleArg.setIsTitleBarVisible(true);
		titleArg.setMiddleIconRes(middleIconRes);
		titleArg.setLeftBackVisible(false);
		titleArg.setRightBtnVisible(false);
		return titleArg;
	}


	public static TitleArg getInvisibleTitleBar(){
		TitleArg titleArg = new TitleArg();
		titleArg.setIsTitleBarVisible(false);
		return titleArg;
	}
}