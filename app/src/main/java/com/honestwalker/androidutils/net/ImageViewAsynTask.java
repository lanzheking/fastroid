package com.honestwalker.androidutils.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.honestwalker.androidutils.DES;
import com.honestwalker.androidutils.IO.ImageCacheUtil;

/**
 * 默认的图片异步加载器
 * @author langel
 *
 */
public class ImageViewAsynTask  extends AsyncTask<String,Integer,Bitmap> {
	
	private Context   context;
	private ImageView imageView;
	private Integer   inSampleSize = 1;
	private Integer   loadingDrawableResource;
	private Boolean   loadingShowLogo = true;
	
	public ImageViewAsynTask(Context context,ImageView imageView) {
		this(context,imageView,1);
	}
	
	public ImageViewAsynTask(Context context,ImageView imageView,Boolean loadingShowLogo) {
		this(context,imageView,1,loadingShowLogo);
	}
	
	public ImageViewAsynTask(Context context,ImageView imageView, int inSampleSize) {
		this(context,imageView,inSampleSize,true);
	}
	
	public ImageViewAsynTask(Context context,ImageView imageView, int inSampleSize,Boolean loadingShowLogo) {
		this.context   	  = context;
		this.imageView 	  = imageView;
		this.inSampleSize = inSampleSize;
		this.loadingShowLogo = loadingShowLogo;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		String url = params[0];
		HttpClient hc = new DefaultHttpClient();
		HttpGet hg = new HttpGet(url);
		Bitmap bm;
		if(ImageCacheUtil.existsImageCache(context, url)) {
			bm = ImageCacheUtil.getImageBitmapInCacheByImgUrl(context, url, false);
			return bm;
		}
		try {
			HttpResponse hr = hc.execute(hg);
			BitmapFactory.Options opt = new BitmapFactory.Options();   
		    opt.inPreferredConfig = Bitmap.Config.RGB_565;    
		    opt.inPurgeable = true;   
		    opt.inInputShareable = true;  
		    opt.inSampleSize = inSampleSize;
			bm = BitmapFactory.decodeStream(hr.getEntity().getContent(),null,opt);
			ImageCacheUtil.saveBitmap(context.getCacheDir().toString() + "/image/" + DES.encrypt(url), bm);
		} catch (Exception e) {
			return null;
		}
		return bm;
	}
	
	 protected void onPostExecute(Bitmap result) {//后台任务执行完之后被调用，在ui线程执行
    	 if(result != null) {
    		 imageView.setImageBitmap(result);
    	 }else {
    		 if(loadingDrawableResource != null) {
        		 imageView.setImageResource(loadingDrawableResource);
        	 }
    	 }
     }
     
     protected void onPreExecute () {//在 doInBackground(Params...)之前被调用，在ui线程执行
    	 if(loadingDrawableResource != null) {
    		 imageView.setImageResource(loadingDrawableResource);
    	 }/* else if(loadingShowLogo){
    		 imageView.setImageResource(R.drawable.logo);
    	 }*/
     }
     
     public void setLoadingDrawableResource(int res) {
    	 this.loadingDrawableResource = res;
     }

}
