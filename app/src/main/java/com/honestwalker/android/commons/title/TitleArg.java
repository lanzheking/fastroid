package com.honestwalker.android.commons.title;

import android.content.res.ColorStateList;
import android.view.View.OnClickListener;

public class TitleArg {
	private boolean isLeftBackVisible;

	private boolean isRightBtnVisible;

	private boolean isTitleBarVisible;

	private String titleTVStr;

	private String leftBtnStr;
	private String rightBtnStr;

	public String getLeftBtnStr() {
		return leftBtnStr;
	}

	public void setLeftBtnStr(String leftBtnStr) {
		this.leftBtnStr = leftBtnStr;
	}

	private int leftIconRes;

	private int middleIconRes;

	private int rightIconRes;

	private OnClickListener leftBackClickListener;

	private OnClickListener rightBtnClickListener;

	public int getMiddleIconRes() {
		return middleIconRes;
	}

	public void setMiddleIconRes(int middleIconRes) {
		this.middleIconRes = middleIconRes;
	}

	public int getLeftIconRes() {
		return leftIconRes;
	}

	public void setLeftIconRes(int leftIconRes) {
		this.leftIconRes = leftIconRes;
	}

	public int getRightIconRes() {
		return rightIconRes;
	}

	public void setRightIconRes(int rightIconRes) {
		this.rightIconRes = rightIconRes;
	}

	public String getRightBtnStr() {
		return rightBtnStr;
	}

	public void setRightBtnStr(String rightBtnStr) {
		this.rightBtnStr = rightBtnStr;
	}

	public OnClickListener getLeftBackClickListener() {
		return leftBackClickListener;
	}

	public void setLeftBackClickListener(OnClickListener leftBackClickListener) {
		this.leftBackClickListener = leftBackClickListener;
	}

	public OnClickListener getRightBtnClickListener() {
		return rightBtnClickListener;
	}

	public void setRightBtnClickListener(OnClickListener rightBtnClickListener) {
		this.rightBtnClickListener = rightBtnClickListener;
	}

	public String getTitleTVStr() {
		return titleTVStr;
	}

	public void setTitleTVStr(String titleTVStr) {
		this.titleTVStr = titleTVStr;
	}

	public boolean isLeftBackVisible() {
		return isLeftBackVisible;
	}

	public void setLeftBackVisible(boolean isLeftBackVisible) {
		this.isLeftBackVisible = isLeftBackVisible;
	}

	public boolean isRightBtnVisible() {
		return isRightBtnVisible;
	}

	public void setRightBtnVisible(boolean isRightBtnVisible) {
		this.isRightBtnVisible = isRightBtnVisible;
	}

	public boolean isTitleBarVisible() {
		return isTitleBarVisible;
	}

	public void setIsTitleBarVisible(boolean isTitleBarVisible) {
		this.isTitleBarVisible = isTitleBarVisible;
	}

}
