package com.honestwalker.androidutils.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.honestwalker.androidutils.ImageUtil;
import com.honestwalker.androidutils.IO.LogCat;

public class ImageCropView extends ImageView {
	
	private final String defaultNameSpace = "http://schemas.android.com/apk/res/android";

	private AttributeSet attrs;
	
	private Bitmap bitmap;
	private int[] size;
	
	public ImageCropView(Context context) {
		super(context);
		init();
	}
	public ImageCropView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.attrs = attrs;
		init();
	}
	public ImageCropView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.attrs = attrs;
		init();
	}
	
	private void init() {
		setWillNotDraw(false);
		int src = attrs.getAttributeIntValue(defaultNameSpace, "src" , 0);
		LogCat.d("img", "src=" + src);
	}
	
//	public void setImageBitmap(Bitmap bitmap) {
//		this.bitmap = bitmap;
//		super.setImageBitmap(bitmap);
//		if(bitmap == null)return;
//		crop();
//	}
	
//	public void setCropImageResource(int resId) {
//		super.setImageResource(resId);
//		size = ImageUtil.getBitmapSize(getContext(), resId);
//		crop();
//	}
	
//	private void crop() {
//		ImageUtil imageUtil = new ImageUtil();
//		Bitmap newBitmap = imageUtil.bitmapZip(bitmap, super.getWidth());
//		
//		int viewWidth  = size[0];
//		int viewHeight = size[1];
//		LogCat.d("img", "view=" + viewWidth + "x" + viewHeight);
//		
//		int imageWidth = bitmap.getWidth();
//		int imageHeight = bitmap.getHeight();
//		LogCat.d("img", "drawable=" + imageWidth + "x" + imageHeight);
//		
//		int scaleWidth = viewWidth;
//		int scaleHeight = imageHeight * scaleWidth / imageWidth;
//		if(scaleHeight > viewHeight) {
//			LogCat.d("img", "imageHeight>viewHeight");
//			scaleHeight = viewHeight;
//		} else {
//			super.setScaleType(ScaleType.FIT_START);
//			LogCat.d("img", "imageHeight<viewHeight");
//			LogCat.d("img", "scaleWidth=" + scaleWidth + "  scaleHeight=" + scaleHeight);
//			scaleHeight = imageHeight * scaleWidth / imageWidth;
//		}
//		
//		LogCat.d("img", "scaleWidth=" + scaleWidth + "  scaleHeight=" + scaleHeight);
//		
//		Bitmap cropedBitmap = Bitmap.createBitmap(newBitmap, 0, 0, scaleWidth, scaleHeight);
//		super.setImageBitmap(cropedBitmap);
//	}

	private String getAttributeValue(String attrName) {
		return getAttributeValue(defaultNameSpace , attrName);
	}
	private String getAttributeValue(String namespace , String attrName) {
		if(attrs == null) return null;
		return attrs.getAttributeValue(namespace, attrName);
	}
	
	private boolean drawed = false;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		try {
			if(drawed) return;
			drawed = true;
			BitmapDrawable drawable = (BitmapDrawable) super.getDrawable();
			Bitmap bitmap = drawable.getBitmap();
			ImageUtil imageUtil = new ImageUtil();
			Bitmap newBitmap = imageUtil.bitmapZip(bitmap, super.getWidth());
			
			int viewWidth  = super.getWidth();
			int viewHeight = super.getHeight();
			LogCat.d("img", "view=" + viewWidth + "x" + viewHeight);
			
			int imageWidth = drawable.getMinimumWidth();
			int imageHeight = drawable.getMinimumHeight();
			LogCat.d("img", "drawable=" + imageWidth + "x" + imageHeight);
			
			int scaleWidth = viewWidth;
			int scaleHeight = imageHeight * scaleWidth / imageWidth;
			if(scaleHeight > viewHeight) {
				LogCat.d("img", "imageHeight>viewHeight");
				scaleHeight = viewHeight;
			} else {
				super.setScaleType(ScaleType.FIT_START);
				LogCat.d("img", "imageHeight<viewHeight");
				LogCat.d("img", "scaleWidth=" + scaleWidth + "  scaleHeight=" + scaleHeight);
				scaleHeight = imageHeight * scaleWidth / imageWidth;
			}
			
			LogCat.d("img", "scaleWidth=" + scaleWidth + "  scaleHeight=" + scaleHeight);
			
			Bitmap cropedBitmap = Bitmap.createBitmap(newBitmap, 0, 0, scaleWidth, scaleHeight);
			super.setImageBitmap(cropedBitmap);
		} catch (Exception e) {
		}
		
		
	}
	
}
