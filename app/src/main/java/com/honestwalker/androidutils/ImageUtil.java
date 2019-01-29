package com.honestwalker.androidutils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.equipment.SDCardUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;


public class ImageUtil {

	/**
	 * 图片path转uri
	 * @param context
	 * @param imagePath  图片路径
	 * @return
	 */
	public static Uri getImageContentUri(Context context, String imagePath) {
		File imageFile = new File(imagePath);
		if(!imageFile.exists()) {
			return null;
		} else {
			return getImageContentUri(context, new File(imagePath));
		}
	}

	/**
	 * 图片file转uri
	 * @param context
	 * @param imageFile
	 * @return
	 */
	public static Uri getImageContentUri(Context context, java.io.File imageFile) {
		if(!imageFile.exists()) return null;
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID },
				MediaStore.Images.Media.DATA + "=? ",
				new String[] { filePath }, null);
		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.MediaColumns._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	public static void rotateImg(Context context,ImageView iv,int angle,int resId){
		Bitmap bitmapOrg = BitmapFactory.decodeResource(context.getResources(),resId);
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap bitmapOrg2 = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height,matrix,false);
		iv.setImageBitmap(bitmapOrg2);
	}
	
	/**
	 * 图像转file
	 * @param bitmap
	 * @param quality 清晰度  100 最清晰
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File bitmapToFile(String path,Bitmap bitmap,int quality,int width) throws FileNotFoundException {
		if(bitmap == null) {return null;}
		
		if(quality > 100) {quality=100;}
		
		try {
			// 压缩分辨率
			bitmap = bitmapZip(bitmap,width);
			
	    	CompressFormat format= CompressFormat.JPEG;
//	    	if(!path.endsWith("/")) {
//	    		path += "/";
//	    	}
//	    	String imgPath = path + System.currentTimeMillis() +".png";
	    	OutputStream stream = new FileOutputStream(path);

	    	// 保存图片
	    	bitmap.compress(format, quality, stream);
	    	File file = new File(path);
	    	return file;
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
		return null;
    	
	}
	
	/**
	 * 更改图片清晰度
	 * @param bitmap 图片对象
	 * @param quality 图片清晰度最大100
	 */
	public static Bitmap bitmapQuality(Bitmap bitmap,int quality) {
		if(bitmap == null) {return bitmap;}
		
		if(quality > 100) {quality=100;}
		if(quality < 1) {quality = 1;}
		
		try {
			
	    	CompressFormat format= CompressFormat.JPEG;
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	bitmap.compress(format, quality, baos);
	    	
	    	return bitmap;
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
		return null;
	}
	
	public String imageZip(String imagePath , int maxWidth, int quality) {
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			if(bitmap.getWidth() > maxWidth) {
				bitmapToFile(imagePath, bitmap, quality, maxWidth);
			}
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
		return imagePath;
	}
	
	/**
	 * 图片等比压缩
	 * @param bitmap
	 * @param newWidthPx 压缩后的宽度像素
	 * @return
	 */
	public static Bitmap bitmapZip(Bitmap bitmap , int newWidthPx) {
		try {
			if(bitmap.getWidth() > newWidthPx) {
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();
				int newW = newWidthPx;
				int newH = (newWidthPx * h) / w;
				bitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, false);
			}
		} catch (Exception e) {
		}
    	return bitmap;
	}
	
	/**
	 *  旋转图片
	 * @param angle
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		 //旋转图片 动作     
       Matrix matrix = new Matrix();  
       matrix.postRotate(angle);    
       System.out.println("angle2=" + angle);    
       // 创建新的图片     
       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,    
               bitmap.getWidth(), bitmap.getHeight(), matrix, true);    
       return resizedBitmap;    
	}
	
	public static int[] getBitmapSize(Context context , int resId) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resId, opts);
		int[] wh = new int[2];
		wh[0] = opts.outWidth;
		wh[1] = opts.outHeight;
		return wh;
	}
	
}
