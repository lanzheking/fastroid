package com.honestwalker.androidutils;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageConvert {

	public static Bitmap drawable2Bitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
				.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	public static Bitmap getBitmapFromResource(Context context,int resId) {
		Resources res= context.getResources();  
		Bitmap bmp=BitmapFactory.decodeResource(res, resId); 
		return bmp;
	}

	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
	
	public static Drawable bimap2Drawble(Context context,Bitmap bitmap){
		Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
		return drawable;
	}
	
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		 //旋转图片 动作     
       Matrix matrix = new Matrix();;    
       matrix.postRotate(angle);    
       System.out.println("angle2=" + angle);    
       // 创建新的图片     
       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,    
               bitmap.getWidth(), bitmap.getHeight(), matrix, true);    
       return resizedBitmap;    
	}

}
