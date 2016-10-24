package com.honestwalker.androidutils.Animation;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.honestwalker.androidutils.equipment.DisplayUtil;

public class AnimationFactory {
	
	private Context context;
	private AnimationFactory(Context context){this.context = context;}
	
	public static Animation animationHid;
	public static Animation animationHid_50percent;
	public static Animation animationShow;
	public static Animation animation3DLeft;
	public static Animation animation3DRight;
	
	public static Animation animationPushOut;
	public static Animation animationPushIn;
	
	
	public static Animation animationPushRightOut;
	public static Animation animationPushRightIn;
	
	
	public static long      animTime;
	
	public static AnimationFactory factory;
	
	public static AnimationFactory getInstance(Context context) {
		return getInstance(context, 500);
	}
	
	/**
	 * 获取动画工厂实例
	 * @param context
	 * @param animTime 动画播放时间，这个时间会一直保留到下一次调用这个方法， 使用该方法获取实例后，以后每次调用getInstance(Context context)都会使用这个时间。默认时间1500ms
	 * @return
	 */
	public static AnimationFactory getInstance(Context context,long animTime) {
		AnimationFactory.animTime = animTime;
		if(factory == null || context == null) {
			factory = new AnimationFactory(context);
			animationHid = new AlphaAnimation(1.0f,0.0f);
			animationHid.setDuration(animTime);
			animationHid_50percent = new AlphaAnimation(1.0f,0.5f);
			animationHid_50percent.setDuration(animTime);
			animationShow = new AlphaAnimation(0.0f,1.0f);
			animationShow.setDuration(animTime);
			animation3DLeft = new Rotate3dAnimation(0,90,DisplayUtil.getWidth(context)  / 2,DisplayUtil.getHeight(context)  / 2,0,true);
			animation3DLeft.setDuration(animTime);
			animation3DRight = new Rotate3dAnimation(-90,0,DisplayUtil.getWidth(context)  / 2,DisplayUtil.getHeight(context)  / 2,0,true);
			animation3DRight.setDuration(animTime);
			animationPushOut = new TranslateAnimation(0, 
					DisplayUtil.getWidth(context)  * -1, 0, 0);
			animationPushOut.setDuration(animTime);
			animationPushIn = new TranslateAnimation(DisplayUtil.getWidth(context) ,
					0, 0, 0);
			animationPushIn.setDuration(animTime);
			
			animationPushRightOut = new TranslateAnimation(0, 
					DisplayUtil.getWidth(context)  * 1, 0, 0);
			animationPushRightOut.setDuration(animTime);
			animationPushRightIn = new TranslateAnimation(DisplayUtil.getWidth(context)  * -1, 0 , 0, 0);
			animationPushRightIn.setDuration(animTime);
			
		}
		return factory;
	}
	
	public static Animation createTranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
		return new TranslateAnimation(fromXDelta,toXDelta,fromYDelta,toYDelta);
	}
	
}
