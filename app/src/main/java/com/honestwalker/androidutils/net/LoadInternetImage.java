package com.honestwalker.androidutils.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class LoadInternetImage {
	
	public static BitmapDrawable getImage(String url) {
		
		URL imageUrl = null;
		Bitmap bitmap = null;
		try {
		
			imageUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis=new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inSampleSize = 2;
			bis.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BitmapDrawable bmpDrawable=new BitmapDrawable(bitmap);
		return bmpDrawable;
	}
	
	public static Bitmap getBitmap(String url,int inSampleSize) {
		URL imageUrl = null;
		Bitmap bitmap = null;
		try {
			imageUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis=new BufferedInputStream(is);
			if(bis.available() > 0) {
				bitmap = BitmapFactory.decodeStream(bis);
				BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inSampleSize = inSampleSize;
			}
			bis.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*if(bitmap == null) {
			bitmap = BitmapFactory.decodeResource(GlobalContext.getGlobalContext().getResources(), R.drawable.whitetag);
		}*/
		
		return bitmap;
	}
	
}
