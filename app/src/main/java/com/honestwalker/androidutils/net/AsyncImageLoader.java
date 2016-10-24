package com.honestwalker.androidutils.net;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import com.honestwalker.androidutils.DES;
import com.honestwalker.androidutils.ImageConvert;
import com.honestwalker.androidutils.IO.ImageCacheUtil;
import com.honestwalker.androidutils.IO.LogCat;

public class AsyncImageLoader {
	private HashMap<String, SoftReference<Drawable>> imageCache;
	private Context context;
	private Boolean useCache;
	/**
	 * 异步加载图片，默认不使用图片缓存功能
	 * @param context
	 */
	public AsyncImageLoader(Context context) {
		this(context,false);
	}
	/**
	 * 异步加载图片，并决定是否使用缓存
	 * @param context
	 * @param useCache 是否使用图片缓存功能
	 */
	public AsyncImageLoader(Context context , Boolean useCache) {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		this.context = context;
		this.useCache = useCache;
	}
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}
	
	public Drawable loadDrawable(final String imageUrl,final int inSampleSize ,
			final ImageCallback imageCallback) {
		LogCat.d("AAAAAA", "111111");
		if(useCache) {
			if(ImageCacheUtil.existsImageCache(context, imageUrl)) {
				try{
					Drawable drawable =  ImageCacheUtil.getImageDrawableInCacheByImgUrl(context, imageUrl, false);
					return drawable;
				} catch (Exception e) {
					return loadDrawableFromUrl(imageUrl,inSampleSize,imageCallback);
				}
			} else {
				return loadDrawableFromUrl(imageUrl,inSampleSize,imageCallback);
			}
		} else {
			LogCat.d("AAAAAA", "2222222222");
			return loadDrawableFromUrl(imageUrl,inSampleSize,imageCallback);
		}
	}
	
	private Drawable loadDrawableFromUrl(final String imageUrl,final int inSampleSize ,
			final ImageCallback imageCallback) {
		LogCat.d("AAAAAA", "3333333333333");
		if (imageCache.containsKey(imageUrl)) {
			LogCat.d("AAAAAA", "4444444444444");
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		LogCat.d("AAAAAA", "55555555555555");
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		new Thread() {
			@Override
			public void run() {
				LogCat.d("AAAAAA", "6666666666666666");
				Drawable drawable = loadImageFromUrl(imageUrl,inSampleSize);
				
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}
	
	public Drawable loadDrawable(final String imageUrl,final int maxWidth, final int maxHeight,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public void distoryBitmap(Bitmap bmb) {
		if (null != bmb && !bmb.isRecycled()) {
			bmb.recycle();
		}
	}

	public Drawable loadImageFromUrl(String url) {
		return loadImageFromUrl(url,1);
	}
	
	/**
	 * 无异步加载
	 * @param url
	 * @param inSampleSize
	 * @return
	 */
	public Drawable loadImageFromUrl(String url,int inSampleSize) {
		try {
			
			org.apache.commons.httpclient.URI uri = new org.apache.commons.httpclient.URI(url, false, "UTF-8");
			URL aURL = new URL(uri.toString());
			URLConnection con = aURL.openConnection();
			con.connect();
			InputStream is = con.getInputStream();
			/* 建立缓冲区是一个良好的编程习惯. */
			BufferedInputStream bis = new BufferedInputStream(is);
			/* 解析网络上的图片 */
			try{
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = inSampleSize;
				Bitmap bm = BitmapFactory.decodeStream(bis,null,opts);
				bis.close();
				is.close();
//				Drawable drawable = ImageConvert.bimap2Drawble(AsyncImageLoader.this.context,bm);
				if(useCache) {
					ImageCacheUtil.saveBitmap(context.getCacheDir() + "/image/" + DES.encrypt(url), bm);
				}
				return ImageConvert.bimap2Drawble(AsyncImageLoader.this.context,bm);
			}catch (OutOfMemoryError e) {
				return null;
			}
			
		} catch (Exception e) {
			LogCat.d("AAAAAA", e.toString());
			return null;
		}
	}
	
	/*public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
				.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}*/

	/*
	 * public static Drawable loadImageFromUrl(String url) { URL m; InputStream
	 * i = null; try { m = new URL(url); i = (InputStream) m.getContent(); }
	 * catch (MalformedURLException e1) { e1.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); } try { Drawable d =
	 * Drawable.createFromStream(i, "src"); return d; } catch (Exception e) {
	 * LogCat.getLog().e(e.toString()); return null; } }
	 */

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
}
