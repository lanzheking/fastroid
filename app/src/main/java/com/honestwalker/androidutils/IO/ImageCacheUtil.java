package com.honestwalker.androidutils.IO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.honestwalker.androidutils.DES;
import com.honestwalker.androidutils.ImageConvert;
import com.honestwalker.androidutils.exception.ExceptionUtil;

public class ImageCacheUtil {
	
	 /**
     * 获取路径中的图片扩展名，可以是url，如果不包含常见图片扩展名，则返回.png
     * @param path
     * @return
     */
    public static String getImageExtension(String path) {
    	if(path == null) {
    		return null;
    	} else {
    		String ext = path.substring(path.lastIndexOf(".")).toLowerCase();
    		if(ext.equals(".gif") || ext.equals(".jpg") || ext.equals(".png") || ext.equals(".bmp") || ext.equals(".jpeg")) {
    			return ext;
    		} else {
    			return ".png";
    		}
    	}
    }
   
    /**
     * 缓存中是否存在指定url的图片
     * @return
     */
    public static Boolean existsImageCache(Context context,String imgUrl) {
    	String fileName = "";
		try {
			fileName = DES.encrypt(imgUrl);
			//fileName += getImageExtension(imgUrl);
		} catch (Exception e1) {
			fileName  = imgUrl.substring(imgUrl.lastIndexOf("/")+1);  
		}
		File file = new File(context.getCacheDir() + "/image/",fileName);
		return file.exists();
    }
    
    /**
     * 从缓存中获取图片bitmap ， 根据imgUrl获取
     * @param context
     * @param imgUrl 图片url
     * @param notExistsDownload 如果图片不存在缓存中，是否自动下载再返回，否则返回null
     * @return
     */
    public static Bitmap getImageBitmapInCacheByImgUrl(Context context,String imgUrl,Boolean notExistsDownload) {
    	Bitmap bitmap = ImageConvert.drawable2Bitmap(getImageDrawableInCacheByImgUrl(context,imgUrl,notExistsDownload));
    	return bitmap;
    }
    
    /**
     * 从缓存中获取图片Drawable ， 根据imgUrl获取
     * @param context
     * @param imgUrl 图片url
     * @param notExistsDownload 如果图片不存在缓存中，是否自动下载再返回，否则返回null
     * @return
     */
    public static Drawable getImageDrawableInCacheByImgUrl(Context context,String imgUrl,Boolean notExistsDownload) {
    	String fileName = "";
		try {
			fileName = DES.encrypt(imgUrl);
			//fileName += getImageExtension(imgUrl);
		} catch (Exception e1) {
			fileName  = imgUrl.substring(imgUrl.lastIndexOf("/")+1);  
		}
		File file = new File(context.getCacheDir() + "/image/",fileName);
		if(!file.exists()) {
			if(notExistsDownload) {
				String realPath = saveImage2CachePath(context,imgUrl);
				Drawable drawable = Drawable.createFromPath(realPath);
				return drawable;
			} else {
				return null;
			}
		} else {
			Drawable drawable = null;
			try {
				drawable = Drawable.createFromPath(file.toString());
			} catch (Exception e) {
				return null;
			}
			return drawable;
		}
    }
    
    /**
     * 保存图片到缓存， 无线程 </br >
     * 保存方式是讲imgUrl进行DES加密（这样便没有特俗符号）作为文件名，如果url没有后最自动使用.png作为后缀,然后保存</br>
     * 这样取得图片就可以根据imgUrl取得或根据图片真实路径取得</br>
     * 注意：实现必须设置好DES的key。
     * @return String 保存后的图片真实路径
     */
    public static String saveImage2CachePath(Context context,String imgUrl) {
    	String fileName = "";
		try {
			fileName = DES.encrypt(imgUrl);
			fileName += getImageExtension(imgUrl);
		} catch (Exception e1) {
			fileName  = imgUrl.substring(imgUrl.lastIndexOf("/")+1);  
		}
    	File file = new File(context.getCacheDir(),fileName);
    	if (file.exists()) {
    		file.delete();
		}
    	try {
			FileOutputStream fos = new FileOutputStream(file);
			InputStream is = new URL(imgUrl).openStream();
			int data = is.read();
			while (data != -1) {
				fos.write(data);
				data = is.read();
			}
			fos.close();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return file.toString();
    }
    
    /**
     * 保存bitmap
     * @param file
     * @param bitmap
     */
    public static void saveBitmap(File file,Bitmap bitmap) {
    	ByteArrayOutputStream steam = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, steam);
        byte[] buffer = steam.toByteArray();
        RandomAccessFile accessFile = null;
        try {
            accessFile = new RandomAccessFile(file, "rw");
            accessFile.write(buffer);
        } catch (Exception e) {
        	ExceptionUtil.showException(e);
        }
        
    }
    
    /**
     * 保存bitmap
     * @param path 包括完整路径和文件名
     * @param bitmap
     */
    public static void saveBitmap(String path,Bitmap bitmap) {
    	saveBitmap(new File(path), bitmap);
    }
    
    /**
     * 保存drawable图像到本地缓存
     * @param file
     * @param drawable
     */
    public static void saveDrawable(File file,Drawable drawable) {
    	if(file != null && drawable != null) {
    		Bitmap bitmap = ImageConvert.drawable2Bitmap(drawable);
    		saveBitmap(file, bitmap);
    	}
    }
    
    /**
     * 保存drawable图像到本地缓存
     * @param path 图片路径
     * @param drawable
     */
    public static void saveDrawable(String path,Drawable drawable) {
    	saveDrawable(new File(path), drawable);
    }
    
}
