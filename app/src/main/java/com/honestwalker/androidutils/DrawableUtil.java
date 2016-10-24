package com.honestwalker.androidutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class DrawableUtil {
	
	public static Drawable zoomDrawable(Context context,Drawable drawable, int w, int h) { 
		Bitmap bitmap = ImageConvert.drawable2Bitmap(drawable);
		Drawable result = ImageConvert.bimap2Drawble(context,BitmapUtil.zoomBitmap(bitmap, w , h));
		return result;
	}
	
	public static Drawable zoomDrawable(Context context,Drawable drawable,double zoomLevel) {
		Bitmap bitmap = ImageConvert.drawable2Bitmap(drawable);
		Drawable result = ImageConvert.bimap2Drawble(context,BitmapUtil.zoomBitmap(bitmap, zoomLevel));
		return result;
	}

}
