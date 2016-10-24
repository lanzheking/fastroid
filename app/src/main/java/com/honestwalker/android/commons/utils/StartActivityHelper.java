package com.honestwalker.android.commons.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.IO.SharedPreferencesLoader;

public class StartActivityHelper {
	
	public static final int ANIM_PUSHIN = 1;
	public static final int ANIM_PUSHOUT = 2;
	public static final int ANIM_ENTER = 3;
	public static final int ANIM_ENTER_FAST = 4;
	public static final int ANIM_PUSHIN_WITHOUT_OUT = 5;
	public static final int ANIM_PUSHOUT_WITHOUT_OUT = 6;
	public static final int ANIM_SHOW = 7;
	public static final int ANIM_UP_TO_DOWN = 8;
	public static final int ANIM_DOWN_TO_UP = 9;
	public static final int ANIM_DOWN_TO_UP_SIMPLE = 10;
	public static final int ANIM_UP_TO_DOWN_SIMPLE = 11;
	
	public static void toActivity(Activity context, Class descActivityClass) {
		Intent intent = new Intent(context, descActivityClass);
		onActivityChange(context, intent , 0);
		context.startActivity(intent);
		activityAnim(context, intent, ANIM_ENTER_FAST);
		gc();
	}

	public static void toActivity(Activity context, Class descActivityClass, Intent intent) {
		intent.setClass(context, descActivityClass);
		onActivityChange(context, intent , 0);
		context.startActivity(intent);
		activityAnim(context, intent, ANIM_ENTER_FAST);
		gc();
	}

	public static void toActivity(Activity context, Class descActivityClass, int animCode) {
		Intent intent = new Intent(context, descActivityClass);
		onActivityChange(context, intent , animCode);
		context.startActivity(intent);
		activityAnim(context, intent , animCode);
		gc();
	}

	public static void toActivity(Activity context, Class descActivityClass, Bundle data, int animCode) {
		Intent intent = new Intent(context, descActivityClass);
		onActivityChange(context, intent , animCode);
		intent.putExtras(data);
		context.startActivity(intent);
		activityAnim(context, intent , animCode);
		gc();
	}

	public static void toActivity(Activity context, Class descActivityClass, Intent intent, int animCode) {
		intent.setClass(context, descActivityClass);
		onActivityChange(context, intent , animCode);
		context.startActivity(intent);
		activityAnim(context, intent , animCode);
		gc();
	}
	
	public static void toActivityForResult(Activity context,Class descActivityClass,int requestCode) {
		Intent intent = new Intent(context, descActivityClass);
		onActivityChange(context, intent , 0);
		context.startActivityForResult(intent, requestCode);
		activityAnim(context, intent , ANIM_ENTER_FAST);
		gc();
	}

	public static void toActivityForResult(Activity context,Class descActivityClass, Intent intent ,  int requestCode) {
		intent.setClass(context, descActivityClass);
		onActivityChange(context, intent , 0);
		context.startActivityForResult(intent , requestCode);
		activityAnim(context, intent , ANIM_ENTER_FAST);
		gc();
	}

	public static void toActivityForResult(Activity context,Class descActivityClass , int requestCode , int animCode) {
		Intent intent = new Intent(context, descActivityClass);
		onActivityChange(context, intent , animCode);
		context.startActivityForResult(intent , requestCode);
		activityAnim(context, intent , animCode);
		gc();
	}

	public static void toActivityForResult(Activity context,Class descActivityClass, Bundle data, int requestCode,int animCode) {
		Intent intent = new Intent(context, descActivityClass);
		intent.putExtras(data);
		onActivityChange(context, intent, animCode);
		context.startActivityForResult(intent , requestCode);
		activityAnim(context, intent , animCode);
		gc();
	}

	public static void toActivityForResult(Activity context,Class descActivityClass, Intent intent, int requestCode , int animCode) {
		intent.setClass(context, descActivityClass);
		onActivityChange(context, intent , animCode);
		context.startActivityForResult(intent , requestCode);
		activityAnim(context, intent , animCode);
		gc();
	}
	
	public static void onActivityChange(Activity context , Intent intent , int animCode) {
		if(intent != null) {
			intent.putExtra("backAnimCode", getExitAnimCode(animCode));
			intent.putExtra("lastActivityClass", context.getClass());
		}
	}

	private static int getExitAnimCode(int animCode) {
		switch (animCode) {
			case ANIM_PUSHIN: {
				return ANIM_PUSHOUT;
			}
			case ANIM_PUSHOUT: {
				return ANIM_PUSHIN;
			}
			case ANIM_ENTER: {
				return 0;
			}
			case ANIM_ENTER_FAST: {
				return 0;
			}
			case ANIM_PUSHIN_WITHOUT_OUT: {
				return ANIM_PUSHOUT_WITHOUT_OUT;
			}
			case ANIM_PUSHOUT_WITHOUT_OUT: {
				return ANIM_PUSHIN_WITHOUT_OUT;
			}
			case ANIM_SHOW: {
				return 0;
			}
			case ANIM_DOWN_TO_UP: {
				return ANIM_UP_TO_DOWN;
			}
			case ANIM_UP_TO_DOWN: {
				return ANIM_DOWN_TO_UP;
			}
			case ANIM_DOWN_TO_UP_SIMPLE: {
				return ANIM_UP_TO_DOWN_SIMPLE;
			}
			case ANIM_UP_TO_DOWN_SIMPLE: {
				return ANIM_DOWN_TO_UP_SIMPLE;
			}
		}
		return 0;
	}
	
	/**
	 * 推拉效果动画
	 */
	public static void activityAnim( Activity context , Intent intent, int animCode) {
		if(animCode == 0) {
			return;
		}
		saveCurrentAnim(context , animCode);
		int enterAnim = 0;
		int exitAnim = 0;
		switch (animCode) {
			case ANIM_PUSHIN: {
				enterAnim = R.anim.common_in_from_right;
				exitAnim  = R.anim.common_out_to_left;
			} break;
			case ANIM_PUSHOUT: {
				enterAnim = R.anim.common_in_from_left;
				exitAnim = R.anim.common_out_to_right;
			} break;
			case ANIM_ENTER: {
				enterAnim = R.anim.common_anim_enter;
				exitAnim = R.anim.common_anim_exit;
			} break;
			case ANIM_ENTER_FAST: {
				enterAnim = R.anim.common_anim_enter_fast;
				exitAnim = R.anim.common_anim_exit_fast;
			} break;
			case ANIM_PUSHIN_WITHOUT_OUT: {
				enterAnim = R.anim.common_in_from_right;
				exitAnim = R.anim.common_anim_hidden;
			} break;
			case ANIM_PUSHOUT_WITHOUT_OUT: {
				enterAnim = R.anim.common_anim_show;
				exitAnim = R.anim.common_out_to_right;
			} break;
			case ANIM_SHOW: {
				enterAnim = R.anim.common_anim_show;
				exitAnim = R.anim.common_anim_hidden;
			} break;
			case ANIM_DOWN_TO_UP: {
				enterAnim = R.anim.common_anim_down_to_up_in;
//				exitAnim = R.anim.common_anim_down_to_up_out;
				exitAnim = R.anim.common_anim_none;
			} break;
			case ANIM_UP_TO_DOWN: {
//				enterAnim = R.anim.common_anim_up_to_down_in;
				enterAnim = R.anim.common_anim_none;
				exitAnim = R.anim.common_anim_up_to_down_out;
			} break;
			case ANIM_DOWN_TO_UP_SIMPLE: {
				enterAnim = R.anim.common_anim_show;
				exitAnim = R.anim.common_anim_down_to_up_out;
			} break;
			case ANIM_UP_TO_DOWN_SIMPLE: {
				enterAnim = R.anim.common_anim_up_to_down_in;
				exitAnim = R.anim.common_anim_hidden;
			} break;
		}
		context.overridePendingTransition(enterAnim , exitAnim);
	}
	
	public static void gc() {
		Runtime.getRuntime().gc();
	}
	
	/** 保存当前动画code ， 因为activity动画跳转可能跨线程， 不能使用全局变量， 要保存到SharedPreferences中 */
	private static void saveCurrentAnim(Context context, int animCode) {
		SharedPreferencesLoader.getInstance(context).putInt("anim_code", animCode);
	}
	
	/** 获取上次所采用的动画 */
	private static int getLastAnim(Context context) {
		int lastAnim = SharedPreferencesLoader.getInstance(context).getInt("anim_code");
		saveCurrentAnim(context, 0);  // 获取后清空保存的动画
		return lastAnim;
	}
	
	/** 根据上次动画获取相反动画 */
	public static int getReverseAnimCode(Context context) {
		int lastAnimCode = getLastAnim(context);
		switch (lastAnimCode) {
			case ANIM_PUSHIN: {
				return ANIM_PUSHOUT;
			}
			case ANIM_ENTER: {
				return ANIM_ENTER;
			}
			case ANIM_ENTER_FAST: {
				return ANIM_ENTER_FAST;
			}
			case ANIM_PUSHIN_WITHOUT_OUT: {
				return ANIM_PUSHOUT_WITHOUT_OUT;
			}
			case ANIM_SHOW: {
				return ANIM_SHOW;
			}
			case ANIM_DOWN_TO_UP: {
				return ANIM_UP_TO_DOWN;
			}
			case ANIM_UP_TO_DOWN: {
				return ANIM_DOWN_TO_UP;
			}
		}
		return 0;
	}
	
}
