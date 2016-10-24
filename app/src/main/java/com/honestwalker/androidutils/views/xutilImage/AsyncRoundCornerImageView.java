package com.honestwalker.androidutils.views.xutilImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;


/**
 * 带圆角的异步图片工具
 */
public class AsyncRoundCornerImageView extends RelativeLayout {

	private Context context;

	private ImageView imageView;
	private ProgressBar progressBar;
	private BitmapUtils bitmapUtils;
	public ImageView getImageView() {
		return imageView;
	}
	public ProgressBar getProgressBar() {
		return progressBar;
	}
	public BitmapUtils getBitmapUtils() {
		return bitmapUtils;
	}

	private final float defaultRadiusRatio = 15;
	
	public AsyncRoundCornerImageView(Context context) {
		this(context, null, 0);
	}

	public AsyncRoundCornerImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AsyncRoundCornerImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	private void init() {

		imageView = new ImageView(context);
		LayoutParams ivlp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		this.addView(imageView, ivlp);
		//图片载出之前，默认为灰色
		imageView.setBackgroundColor(Color.argb(255,230, 230, 230));
		
		progressBar = new ProgressBar(context);
		LayoutParams lp = new LayoutParams(20, 20);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.addView(progressBar, lp);
		
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Config.ARGB_8888);
	}

	public void setImageBitmap(final Bitmap bitmap){
		if (getWidth() == 0 || getHeight() == 0) {
			//若onLayout方法未执行，就等待
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					setImageBitmap(bitmap);
				}
			}, 20);
		}else {
			Bitmap cropBitmap = resizeBitmapByCenterCrop(bitmap, getWidth(), getHeight());
			imageView.setImageBitmap(getRoundedBitmap(cropBitmap, defaultRadiusRatio));
		}
	}

	public void setImageBitmap(Bitmap bitmap, int width, int height) {
		setImageBitmap(bitmap, width, height, defaultRadiusRatio);
	}
	
	public void setImageBitmap(Bitmap bitmap, int width, int height,
			float radius) {
		Bitmap cropBitmap = resizeBitmapByCenterCrop(bitmap, width, height);
		imageView.setImageBitmap(getRoundedBitmap(cropBitmap, defaultRadiusRatio));
	}
	
	public AsyncRoundCornerImageView configDefaultLoadingImage(int loadingImageRes){
		 bitmapUtils.configDefaultLoadingImage(loadingImageRes);
	     return this;
	}

	public AsyncRoundCornerImageView configDefaultLoadingImage(Drawable loadingImagedDrawable){
		bitmapUtils.configDefaultLoadingImage(loadingImagedDrawable);
		return this;
	}

	public AsyncRoundCornerImageView configDefaultLoadingImage(Bitmap loadingImagedBitmap){
		bitmapUtils.configDefaultLoadingImage(loadingImagedBitmap);
		return this;
	}
	
	public AsyncRoundCornerImageView configDefaultLoadFailedImage(int loadFailedImageRes){
		bitmapUtils.configDefaultLoadFailedImage(loadFailedImageRes);
		return this;
	}
	
	public AsyncRoundCornerImageView configDefaultLoadFailedImage(Drawable loadFailedImageDrawable){
		bitmapUtils.configDefaultLoadFailedImage(loadFailedImageDrawable);
		return this;
	}

	public AsyncRoundCornerImageView configDefaultLoadFailedImage(Bitmap loadFailedImageBitmap){
		bitmapUtils.configDefaultLoadFailedImage(loadFailedImageBitmap);
		return this;
	}
	
	public void loadUrl(String imageUrl) {
		bitmapUtils.display(imageView, imageUrl, new CustomBitmapLoadCallBack());
	}
	
	public void loadUrl(String imageUrl, int width, int height) {
		ViewSizeHelper.getInstance(context).setWidth(this, width);
		ViewSizeHelper.getInstance(context).setHeight(this, height);
		ViewSizeHelper.getInstance(context).setWidth(imageView, width);
		ViewSizeHelper.getInstance(context).setHeight(imageView, height);
		bitmapUtils.display(imageView, imageUrl, new CustomBitmapLoadCallBack());
	}
	
	private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(android.R.color.transparent);

	private Bitmap cropBitmap;
	public void fadeInDisplay(final ImageView imageView, final Bitmap bitmap) {
		if (getWidth() == 0 || getHeight() == 0) {
			//若onLayout方法未执行，就等待
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					fadeInDisplay(imageView, bitmap);
				}
			}, 20);
		}else {
			
			cropBitmap = resizeBitmapByCenterCrop(bitmap, getWidth(), getHeight());
			Bitmap roundCornerBitmap = getRoundedBitmap(cropBitmap, defaultRadiusRatio);
			final TransitionDrawable transitionDrawable =
					new TransitionDrawable(new Drawable[]{
							TRANSPARENT_DRAWABLE,
							new BitmapDrawable(imageView.getResources(), roundCornerBitmap)
					});
			imageView.setImageDrawable(transitionDrawable);
			transitionDrawable.startTransition(500);
		}
    }
	
	public class CustomBitmapLoadCallBack extends DefaultBitmapLoadCallBack<ImageView> {

        @Override
        public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
        	progressBar.setVisibility(View.GONE);
        	fadeInDisplay(container, bitmap);
        	imageView.setBackgroundColor(Color.argb( 0, 0, 0, 0));
//        	setImageBitmap(bitmap);
        }
    }
	
	/**
	 * 按照ScaleType的centerCrop来剪切位图  <br />
	 * 
	 * (1)当图片大于ImageView的宽高：以图片的中心点和ImageView的中心点为基准，按比例缩小图片，直到图片的宽高有一边等于ImageView的宽高，
	 * 		则对于另一边，图片的长度大于或等于ImageView的长度，最后用ImageView的大小居中截取该图片。<br />
	 * (2)当图片小于ImageView的宽高：以图片的中心店和ImageView的中心点为基准，按比例扩大图片，直到图片的宽高大于或等于ImageView的宽高，
	 * 		并按ImageView的大小居中截取该图片。<br />
	 * 
	 * @param src 原位图
	 * @param containerWidth 容器的宽度（目标宽度）
	 * @param containerHeight 容器高度（目标高度）
	 * @return 目标宽高的位图
	 */
	public Bitmap resizeBitmapByCenterCrop(Bitmap src, int containerWidth, int containerHeight) {
	    if (src == null || containerWidth == 0 || containerHeight == 0) {
	        return null;
	    }
	    
	    int bitmapWidth = src.getWidth();
	    int bitmapHeight = src.getHeight();
		
	    if (bitmapWidth < containerWidth || bitmapHeight < containerHeight) {
	    	//位图宽或高小于容器宽或高时
	    	
	    	Bitmap scaleUpBitmap = null;
	    	//位图和容器的宽度比值
	    	float widthRatio = (float)containerWidth / (float)bitmapWidth;
	    	//位图和容器的高度比值
	    	float heightRatio = (float)containerHeight / (float)bitmapHeight;
	    	if (widthRatio > heightRatio) {
				scaleUpBitmap = getScaleBitmap(src, (float)containerWidth / (float)bitmapWidth);
			}else {
				scaleUpBitmap = getScaleBitmap(src, (float)containerHeight / (float)bitmapHeight);
			}
	    	int firstPixelX = (scaleUpBitmap.getWidth() - containerWidth) / 2;
	    	int firstPixelY = (scaleUpBitmap.getHeight() - containerHeight) / 2;
	    	return Bitmap.createBitmap(scaleUpBitmap, firstPixelX, firstPixelY, containerWidth, containerHeight);
	    	
		}else {
			//位图宽和高都大于等于容器宽和高时
			int firstPixelX = (bitmapWidth - containerWidth) / 2;
			int firstPixelY = (bitmapHeight - containerHeight) / 2;
			return Bitmap.createBitmap(src, firstPixelX, firstPixelY, containerWidth, containerHeight);
		}
	}
	
	 /**
     * 图片圆角处理
     * @return
     */
    public static Bitmap getRoundedBitmap(Bitmap mBitmap,float roundPx) {
    	Bitmap bgBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Config.ARGB_8888);
    	//把创建的位图作为画板
    	Canvas mCanvas = new Canvas(bgBitmap);
    	
    	Paint mPaint = new Paint();
    	Rect mRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
    	RectF mRectF = new RectF(mRect);
    	mPaint.setAntiAlias(true);
    	//先绘制圆角矩形
    	mCanvas.drawRoundRect(mRectF, roundPx, roundPx, mPaint);
    	//设置图像的叠加模式
    	mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    	//绘制图像
    	mCanvas.drawBitmap(mBitmap, mRect, mRect, mPaint);
    	
    	return bgBitmap;
    }
    
    /**
	 * 按比例缩放位图
	 * @param mBitmap
	 * @param scaleRitio
	 * @return
	 */
	public static Bitmap getScaleBitmap(Bitmap mBitmap,float scaleRitio) {
    	int width = mBitmap.getWidth();
    	int height = mBitmap.getHeight();
    	
    	Matrix matrix = new Matrix();
    	matrix.postScale(scaleRitio, scaleRitio);
    	Bitmap mScaleBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, true);
    	
    	return mScaleBitmap;
    }
}