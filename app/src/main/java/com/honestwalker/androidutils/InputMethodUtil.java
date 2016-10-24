package com.honestwalker.androidutils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputMethodUtil {
	
	/**
	 * 弹出键盘
	 * @param context
	 * @param editText
	 */
	public static void showInputMethod(Context context,View editText) {
		editText.requestFocus();
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 隐藏键盘
	 * @param context
	 * @param editText
	 */
	public static void hiddenInputMethod(Context context,View editText) {
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	
}
