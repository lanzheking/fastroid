package com.honestwalker.androidutils.ImageSelector.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.honestwalker.androidutils.ImageUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.io.IOException;

/**
 * Depiction:
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-3-3 上午10:39. <br />
 * Rewrite Date :  16-3-3 上午10:39. <br />
 */
public class PictureUtil {

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            ExceptionUtil.showException(e);
        }
        return degree;
    }

    /**
     * 旋转照片文件
     * @param path     图片路径
     * @param degrees  旋转方向 90 为正向
     * @value 是否成功
     */
    public static boolean rotate(String path , int degrees) {
        try {
            Bitmap srcBitmap = BitmapFactory.decodeFile(path);  // 读取照片
            Bitmap newBitmap = ImageUtil.rotaingImageView(degrees, srcBitmap); // 旋转照片
            ImageUtil.bitmapToFile(path , newBitmap , 100 , newBitmap.getWidth()); // 输出照片
            return true;
        } catch (Exception e) {
            ExceptionUtil.showException(e);
        }
        return false;
    }

    public static Bitmap rotate(Bitmap b, int degrees) {
        if(degrees==0){
            return b;
        }
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() ,
                    (float) b.getHeight() );
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
            }
        }
        return b;
    }



}
