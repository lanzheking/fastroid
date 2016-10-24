package com.honestwalker.androidutils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.honestwalker.androidutils.exception.ExceptionUtil;

public class ImageRecyle {
	public static void recycle(Bitmap bitmap) {
		try {
			if(bitmap != null) {
				if(!bitmap.isRecycled())
					bitmap.recycle();
			}
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
	}
	public static void recycle(Drawable drawable) {
		if(drawable != null) {
			recycle(ImageConvert.drawable2Bitmap(drawable));
		}
	}
	public static void recycle(ImageView iv) {
		recycle(iv.getDrawable());
	}
}
