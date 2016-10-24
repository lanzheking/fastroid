package com.honestwalker.androidutils.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class AsyncImageView extends ImageView {
	
	private String TAG = "AsyncImageViewV2";
	
	private Context context;
	
	private boolean imageLoaderInited = false;
	
	private ImageLoader imageLoader;
	
	private DisplayImageOptions options;
	
	public AsyncImageView(Context context) {
		super(context);
		this.context = context;
		init();
	}
	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	/** 建立图片和loading */
	private void init() {
		
		if(!imageLoaderInited) {
			imageLoaderInited = true;
			ImageLoaderConfiguration imageLoaderConfiguration = ImageLoaderConfiguration.createDefault(context);
			ImageLoader.getInstance().init(imageLoaderConfiguration);
			
			imageLoader = ImageLoader.getInstance();
			
			options = new DisplayImageOptions.Builder()
//		    .showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的 图片
//		    .showImageForEmptyUri(R.drawable.empty) 		// 
//		    .showImageOnFail(R.drawable.fail) // 加载失败时显示的图片
		    .resetViewBeforeLoading(true)  // 加载前是否时重置imageview
		    .cacheInMemory(true) //  是否使用内存缓存
		    .cacheOnDisk(true) // 是否使用磁盘缓存
		    .considerExifParams(false) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
		    .imageScaleType(ImageScaleType.EXACTLY) // 设置图片显示类型
		    .bitmapConfig(Bitmap.Config.ARGB_8888) // default
		    
		    .displayer(new SimpleBitmapDisplayer()) // 图片正常显示
//		    .displayer(new RoundedBitmapDisplayer(200)) // 有问题。是否设置为圆角，弧度为多少
//		    .displayer(new FadeInBitmapDisplayer(2000)) // 是否图片加载好后渐入的动画时间，可能会出现闪动
		    
//		    .delayBeforeLoading(0)			// 加载延时
//		    .decodingOptions()				 	 // 设置图片的解码配置
//		    .postProcessor(...)					 // 设置显示前的图片，显示后这个图片一直保留在缓存中
//		    .preProcessor(null)  				 // 设置图片加入缓存前，对bitmap进行设置
//		    .extraForDownloader(...)			 // 设置额外的内容给ImageDownloader
//		    .handler(new Handler()) 			 // default
		    
		    .build();
			
		}
		
	}
	
	public void loadUrl(String url , final ImageLoadingListener imageLoadingListener) {
		
		imageLoader.displayImage(url, this , options , new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				Log.d(TAG, "onLoadingStarted");
				if(imageLoadingListener != null) {
					imageLoadingListener.onLoadingStarted(arg0, arg1);
				}
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				if(imageLoadingListener != null) {
					imageLoadingListener.onLoadingFailed(arg0, arg1, arg2);
				}
				Log.d(TAG, "onLoadingFailed");
				if(arg2.getCause() == null) return;
				StackTraceElement[] stesCause = arg2.getCause().getStackTrace();
				if(stesCause != null) {
					Log.e(TAG,arg2.getCause().toString());
					for (StackTraceElement ste : stesCause) {
						Log.e(TAG,ste.toString());
					}
				}
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				Log.d(TAG, "onLoadingComplete");
				if(imageLoadingListener != null) {
					imageLoadingListener.onLoadingComplete(arg0, arg1, arg2);
				}
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				Log.d(TAG, "onLoadingCancelled");
				if(imageLoadingListener != null) {
					imageLoadingListener.onLoadingCancelled(arg0, arg1);
				}
			}
		});

	}
	
	public void loadUrl(String url) {
		loadUrl(url , null);
	}

	
}
