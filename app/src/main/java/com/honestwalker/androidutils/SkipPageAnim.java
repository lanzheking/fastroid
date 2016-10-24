package com.honestwalker.androidutils;


import com.honestwalker.android.fastroid.R;

public class SkipPageAnim {
	/*=================================================================*/
	/*						可选动画样式								   */
	/*=================================================================*/
	public static final int ANIM_ZOOMIN 	   = R.anim.pageanim_zoomin;
	public static final int ANIM_ZOOMOUT	   = R.anim.pageanim_zoomout;
	public static final int ANIM_FADEIN  	   = R.anim.pageanim_fadein;
	public static final int ANIM_FADEOUT 	   = R.anim.pageanim_fadeout;
	public static final int ANIM_PUSH_UP_IN    = R.anim.pageanim_push_up_in;
	public static final int ANIM_PUSH_UP_OUT   = R.anim.pageanim_push_up_out;
	public static final int ANIM_PUSH_LEFT_IN  = R.anim.pageanim_push_left_in;
	public static final int ANIM_PUSH_LEFT_OUT = R.anim.pageanim_push_left_out;
	public static final int ANIM_FADE_IN = android.R.anim.fade_in;
	public static final int ANIM_FADE_OUT = android.R.anim.fade_in;
	
	public static SkipPageAnim ZOOM = new SkipPageAnim(ANIM_ZOOMIN,ANIM_ZOOMOUT);
	public static SkipPageAnim FADE = new SkipPageAnim(ANIM_FADEIN,ANIM_FADEOUT);
	public static SkipPageAnim PUSHUP2DOWN = new SkipPageAnim(ANIM_PUSH_UP_IN,ANIM_PUSH_UP_OUT);
	public static SkipPageAnim PUSHLEFT2RIGHT = new SkipPageAnim(ANIM_PUSH_LEFT_IN,ANIM_PUSH_LEFT_OUT);
	
	private int animIn;
	private int animOut;
	
	public SkipPageAnim(int animIn,int animOut){
		this.animIn = animIn;
		this.animOut = animOut;
	}
	
	public int getAnimIn() {
		return animIn;
	}
	public void setAnimIn(int animIn) {
		this.animIn = animIn;
	}
	public int getAnimOut() {
		return animOut;
	}
	public void setAnimOut(int animOut) {
		this.animOut = animOut;
	}
	
}
