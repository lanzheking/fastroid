package com.honestwalker.androidutils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {
	
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) { 
        int width = bitmap.getWidth(); 
        int height = bitmap.getHeight(); 
        Matrix matrix = new Matrix(); 
        float scaleWidht = ((float) w / width); 
        float scaleHeight = ((float) h / height); 
        matrix.postScale(scaleWidht, scaleHeight); 
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, 
                matrix, true); 
        return newbmp; 
    }
	
	public static Bitmap zoomBitmap(Bitmap bitmap,double zoomLevel) {
		int width = bitmap.getWidth(); 
        int height = bitmap.getHeight(); 
        Matrix matrix = new Matrix(); 
        float scaleWidht = ((float) (width / zoomLevel) / width); 
        float scaleHeight = ((float) (height / zoomLevel) / height); 
        matrix.postScale(scaleWidht, scaleHeight); 
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, 
                matrix, true); 
        return newbmp; 
	}

    /**
     * 按照比例剪切图片，保持横宽
     */
    public static Bitmap imageCropHeightProportion(Bitmap bitmap , int wProportion , int hProportion) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

//        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
//        int retY = w > h ? 0 : (h - w) / 2;

        int retX = 0;
        int retY = 0;

        int cropH = w * hProportion / wProportion;
        if(cropH > h) cropH = h;

        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, w, cropH, null, false);
    }

}
