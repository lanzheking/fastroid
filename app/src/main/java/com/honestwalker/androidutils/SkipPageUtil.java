package com.honestwalker.androidutils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SkipPageUtil {
	
	/**
	 * 页面跳转，默认动画
	 * @param cotx
	 * @param actClass
	 */
	public static void toActivity(Context cotx,Class actClass){
		toActivity(cotx,actClass,0x000,0x000);
	}
	
	/**
	 * 页面跳转自定义动画
	 * @param cotx
	 * @param actClass
	 * @param animInDrawableSource
	 * @param animOutDrawable
	 */
	public static void toActivity(Context cotx,Class actClass,int animInDrawableSource,int animOutDrawable){
		toActivity(cotx,new Intent(cotx,actClass),animInDrawableSource,animOutDrawable);
	}
	
	/**
	 * 页面跳转自定义动画
	 * @param cotx
	 * @param actClass
	 * @param animInDrawableSource
	 */
	public static void toActivity(Context cotx,Class actClass,SkipPageAnim skipPageAnim){
		toActivity(cotx,new Intent(cotx,actClass),skipPageAnim.getAnimIn(),skipPageAnim.getAnimOut());
	}
	
	/**
	 * 可带参数页面跳转
	 * @param cotx
	 * @param intent
	 */
	private static void toActivity(Context cotx,Intent intent){
		toActivity(cotx,intent,SkipPageAnim.ANIM_ZOOMIN,SkipPageAnim.ANIM_ZOOMOUT);
	}
	
	private static void toActivity(Context cotx,Intent intent,int animInDrawableSource,int animOutDrawable){
		Activity act = (Activity)cotx;
		act.startActivityForResult(intent, 11);
		ActivityManager am = (ActivityManager)act.getSystemService(Context.ACTIVITY_SERVICE);
		act.overridePendingTransition(animInDrawableSource,animOutDrawable);
	}

	/**
	 * 页面跳转自定义动画
	 * @param cotx
	 * @param actClass
	 * @param skipPageAnim 
	 */
	public static void toActivity(Context cotx,Intent intent,SkipPageAnim skipPageAnim){
		Activity act = (Activity)cotx;
		act.startActivityForResult(intent, 11);
		ActivityManager am = (ActivityManager)act.getSystemService(Context.ACTIVITY_SERVICE);
		act.overridePendingTransition(skipPageAnim.getAnimIn(),skipPageAnim.getAnimOut());
	}
	
	public static void openUrl(Context cotx,String url) {
		Activity act = (Activity)cotx;
		Uri uri = Uri.parse(url);   
        Intent intent=new Intent(Intent.ACTION_VIEW, uri);  
        act.startActivity(intent);
	}
	
	private static String getIntentDesClassName(Intent intent) {
		return intent.toURI().substring(intent.toURI().lastIndexOf("/") + 2,intent.toURI().length() - 4);
	}
	
}
