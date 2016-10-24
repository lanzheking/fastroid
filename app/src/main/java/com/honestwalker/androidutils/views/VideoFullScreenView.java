package com.honestwalker.androidutils.views;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.VideoView;
/**
 *	Note：在使用此类时，父类外层布局不能使用相对布局，否则不能缩放
 *	推荐使用 LinearLayout 或 FrameLayout
 */
public class VideoFullScreenView extends VideoView {

	int screenWidth;
	//除去状态栏的屏幕高度
	int screenHeightAvailable;
	
	public VideoFullScreenView(Context context) {
		this(context,null);
	}

	public VideoFullScreenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		int screenHeight = getResources().getDisplayMetrics().heightPixels;
		 // 获取状态栏高度
        screenHeightAvailable = screenHeight - getStatusBarHeight();
	}
	
	/**
	 * @param videoPath 视频绝对路径
	 */
	public void startFromPath(String videoPath){
		setVideoPath(videoPath);
		requestFocus();
		start();
	}
	
	/**
	 * @param videoResInRaw raw中的视频资源 如R.raw.movie
	 */
	public void startFromRawRes(int videoResInRaw){
		String uri = "android.resource://" + getContext().getPackageName() + "/" + videoResInRaw;
		setVideoURI(Uri.parse(uri));
		requestFocus();
		start();
	}
	
	/**
	 *  是否自动重播
	 */
	public void setAutoPlayBack(boolean autoPlayBack){
		if (autoPlayBack) {
			setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					start();
				}
			});
		}
	}
	
	/**
	 * @param videoWidth  影片宽度像素值
	 * @param videoHeight 影片高度像素值
	 */
	public void resizeViewAccordingToScreenSize(int videoWidth, int videoHeight) {
	    if (videoWidth == 0 || videoHeight == 0) {
	        return;
	    }
	    
	    int resizeHeight = screenWidth * videoHeight / videoWidth;
	    if (resizeHeight >= screenHeightAvailable) {
	    	//把视频拉大到屏幕宽度，高度等比例放大，若高度大于屏高，即不会出现空白边
	    	getLayoutParams().width = screenWidth;
	    	getLayoutParams().height = resizeHeight;
	    	int heightDelta = (resizeHeight - screenHeightAvailable) / 2;
			try {
				((FrameLayout.LayoutParams)getLayoutParams()).topMargin = - heightDelta;
			} catch (Exception e) {
				((LinearLayout.LayoutParams)getLayoutParams()).topMargin = - heightDelta;
			}
		}else {
			//若出现空白边，就按照把视频拉大到屏幕高度，宽度等比例放大，即不会出现空白边
			getLayoutParams().height = screenHeightAvailable;
			int resizeWidth = screenHeightAvailable * videoWidth / videoHeight;
			getLayoutParams().width = resizeWidth;
			int widthDelta = (resizeWidth - screenWidth) / 2;
			try {
				((FrameLayout.LayoutParams)getLayoutParams()).leftMargin = - widthDelta;
			} catch (Exception e) {
				((LinearLayout.LayoutParams)getLayoutParams()).leftMargin = - widthDelta;
			}
		}
	}
	
	public int getStatusBarHeight(){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = ((Activity)getContext()).getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        return statusBarHeight;
    }
}
