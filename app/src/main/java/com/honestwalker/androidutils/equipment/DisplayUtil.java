package com.honestwalker.androidutils.equipment;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/** ==================================================================
 * 												    		         *
 * 		用于获取屏幕宽、高;View宽、高;或百分比<br />	     	         	 *
 * 		例子:DisplayUtil.getWidth(context)<br />   		 *
 * 		@author lan zhe 2011-6-9<br />   	        	        	 *
 *												                     *
 *===================================================================*/
public class DisplayUtil {

	private static WindowManager windowManager;

//	private static Display display;
//	private static Activity act;
	private static Context context;
	private static DisplayUtil displayUtil;
	
	private DisplayUtil(){}
	
	/*
	 * 获取DisplayUtil实例
	 * @param DisplayUtil
	 * @return
	 */
//	public static DisplayUtil getInstance(Context context){
//		DisplayUtil.context = context;
//		display = act.getWindowManager().getDefaultDisplay();
//		if(displayUtil == null){
//			displayUtil = new DisplayUtil();
//		}
//		return displayUtil;
//	}

	private static WindowManager getWindowManager(Context context) {
		if(windowManager == null) {
			windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		return windowManager;
	}
	
	/**
	 * 获取屏幕宽度
	 * @return
	 */
	public static int getWidth(Context context){
		return getWindowManager(context).getDefaultDisplay().getWidth();
	}
	
	/**
	 * 获取屏幕宽度 单位dip
	 * @return
	 */
	public int getWidthDip(Context context) {
		return px2dip(context, getWidth(context));
	}
	
	/**
	 * 获取View控件宽度
	 * @param viewId
	 * @return
	 */
	public int getViewWidthByResource(Activity act , int viewId){
		View view = act.findViewById(viewId);
		Integer width = view.getLayoutParams().width;
		return (width != null)?width:0;
	}
	
	/**
	 * 获取屏幕高度
	 * @return
	 */
	public static int getHeight(Context context){
		return getWindowManager(context).getDefaultDisplay().getHeight();
	}
	
	/**
	 * 获取屏幕高度 单位dp
	 * @return
	 */
	public static int getHeightDip(Context context) {
		return px2dip(context, getHeight(context));
	}
	
	/**
	 * 获取View控件高度
	 * @param viewId 控件id
	 * @return
	 */
	public static int getViewHeightByResource(Activity act ,int viewId){
		View view = act.findViewById(viewId);
		Integer height = view.getLayoutParams().height;
		return (height != null)?height:0;
	}
	
	/**
	 * 获取View控件高度 单位dip
	 * @param viewId
	 * @return
	 */
	public static int getViewHeightByResourceDip(Activity act , int viewId) {
		return px2dip(act , getViewHeightByResource(act , viewId));
	}
	
	/**
	 * 获取view控件百分比宽度
	 * @param viewId 控件id
	 * @param percent 控件宽度百分比
	 * @return
	 */
	public static int getViewWidthPercent(Activity act , int viewId,int percent){
		View view = act.findViewById(viewId);
		return (int) (view.getLayoutParams().width * percent * 0.01);
	}
	
	/**
	 * 获取view控件百分比宽度
	 * @param viewId
	 * @param percent
	 * @return
	 */
	public static int getViewWidthPercentDip(Activity act , int viewId,int percent) {
		return px2dip(act , getViewWidthPercent(act , viewId, percent));
	}
	
	
	/**
	 * 获取view百分比高度
	 * @param viewId 控件id
	 * @param percent 控件高度百分比
	 * @return
	 */
	public static int getViewHeightPercent(Activity act , int viewId,int percent){
		View view = act.findViewById(viewId);
		return (int) (view.getLayoutParams().height * percent * 0.01);
	}
	
	/**
	 * 获取view百分比高度 单位dip
	 * @param viewId 控件id
	 * @param percent 控件高度百分比
	 * @return
	 */
	public static int getViewHeightPercentDip(Activity act , int viewId,int percent){
		return px2dip(act , getViewHeightPercent(act , viewId, percent));
	}
	
	/**
	 * 获取屏幕百分比宽度
	 * @param percent 百分比
	 * @return
	 */
	public static int getWidthPercent(Context context , int percent){
		return (int) (getWidth(context) * percent * 0.01);
	}
	
	/**
	 * 获取屏幕百分比高度
	 * @param percent
	 * @return
	 */
	public int getHeightPercent(Context context , int percent){
		return (int) (getHeight(context) * percent * 0.01);
	}
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 像素转sp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * sp 转 像素
 	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * px单位转换dp
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context , int pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 设置全屏与取消全屏
	 * @param isFullScreen
	 */
	public static void fullScreen(Activity act , Boolean isFullScreen) {
		if(isFullScreen) {
			act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
		} else {
			    WindowManager.LayoutParams attrs = act.getWindow().getAttributes();  
			    attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);  
			    act.getWindow().setAttributes(attrs);  
			    act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);  
		}
	}
	
	public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        return statusBarHeight;
    }
	
}
