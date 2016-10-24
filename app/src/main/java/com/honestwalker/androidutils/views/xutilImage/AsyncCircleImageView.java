package com.honestwalker.androidutils.views.xutilImage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;


/**
 * 异步圆形图片工具
 */
public class AsyncCircleImageView extends RelativeLayout {

	private Context context;
	
	private CircleImageView imageView;
	private ProgressBar progressBar;
	private BitmapUtils bitmapUtils;
	public CircleImageView getImageView() {
		return imageView;
	}
	public ProgressBar getProgressBar() {
		return progressBar;
	}
	public BitmapUtils getBitmapUtils() {
		return bitmapUtils;
	}

	public AsyncCircleImageView(Context context) {
		this(context, null, 0);
	}

	public AsyncCircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AsyncCircleImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	private void init() {

		imageView = new CircleImageView(context);
		LayoutParams ivlp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		imageView.setBorderWidth(0);
		imageView.setBorderColor(getResources().getColor(android.R.color.transparent));
		this.addView(imageView, ivlp);
		//图片载出之前，默认为灰色
		imageView.setImageDrawable(new ColorDrawable(Color.argb(255,230, 230, 230)));
		
		progressBar = new ProgressBar(context);
		LayoutParams lp = new LayoutParams(20, 20);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.addView(progressBar, lp);
		
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_8888);
	}

	public void setImageBitmap(Bitmap bitmap){
		imageView.setImageBitmap(bitmap);
	}

	public void setImageBitmap(Bitmap bitmap, int width) {
		ViewSizeHelper.getInstance(context).setWidth(this, width);
		ViewSizeHelper.getInstance(context).setHeight(this, width);
		ViewSizeHelper.getInstance(context).setWidth(imageView, width);
		ViewSizeHelper.getInstance(context).setHeight(imageView, width);
		imageView.setImageBitmap(bitmap);
	}
	
	public AsyncCircleImageView configDefaultLoadingImage(int loadingImageRes){
		 bitmapUtils.configDefaultLoadingImage(loadingImageRes);
	     return this;
	}

	public AsyncCircleImageView configDefaultLoadingImage(Drawable loadingImagedDrawable){
		bitmapUtils.configDefaultLoadingImage(loadingImagedDrawable);
		return this;
	}

	public AsyncCircleImageView configDefaultLoadingImage(Bitmap loadingImagedBitmap){
		bitmapUtils.configDefaultLoadingImage(loadingImagedBitmap);
		return this;
	}
	
	public AsyncCircleImageView configDefaultLoadFailedImage(int loadFailedImageRes){
		bitmapUtils.configDefaultLoadFailedImage(loadFailedImageRes);
		return this;
	}
	
	public AsyncCircleImageView configDefaultLoadFailedImage(Drawable loadFailedImageDrawable){
		bitmapUtils.configDefaultLoadFailedImage(loadFailedImageDrawable);
		return this;
	}

	public AsyncCircleImageView configDefaultLoadFailedImage(Bitmap loadFailedImageBitmap){
		bitmapUtils.configDefaultLoadFailedImage(loadFailedImageBitmap);
		return this;
	}
	
	public AsyncCircleImageView setBorderWidth(int borderWidth){
		imageView.setBorderWidth(borderWidth);
		return this;
	}
	
	public AsyncCircleImageView setBorderColor(int borderColor){
		imageView.setBorderColor(borderColor);
		return this;
	}
	public void loadUrl(String imageUrl) {
		bitmapUtils.display(new ImageView(context), imageUrl, new CustomBitmapLoadCallBack());
	}
	
	public void loadUrl(String imageUrl, int width) {
		ViewSizeHelper.getInstance(context).setWidth(this, width);
		ViewSizeHelper.getInstance(context).setHeight(this, width);
		ViewSizeHelper.getInstance(context).setWidth(imageView, width);
		ViewSizeHelper.getInstance(context).setHeight(imageView, width);
		bitmapUtils.display(new ImageView(context), imageUrl, new CustomBitmapLoadCallBack());
	}
	
	public class CustomBitmapLoadCallBack extends DefaultBitmapLoadCallBack<ImageView> {

        @Override
        public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
        	progressBar.setVisibility(View.GONE);
        	setImageBitmap(bitmap);
        }
    }
	
	/**
	 * 圆形图片内部类
	 */
	public static class CircleImageView extends ImageView {

	    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

	    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	    private static final int COLORDRAWABLE_DIMENSION = 1;

	    private static final int DEFAULT_BORDER_WIDTH = 0;
	    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

	    private final RectF mDrawableRect = new RectF();
	    private final RectF mBorderRect = new RectF();

	    private final Matrix mShaderMatrix = new Matrix();
	    private final Paint mBitmapPaint = new Paint();
	    private final Paint mBorderPaint = new Paint();

	    private int mBorderColor = DEFAULT_BORDER_COLOR;
	    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

	    private Bitmap mBitmap;
	    private BitmapShader mBitmapShader;
	    private int mBitmapWidth;
	    private int mBitmapHeight;

	    private float mDrawableRadius;
	    private float mBorderRadius;

	    private boolean mReady;
	    private boolean mSetupPending;

	    public CircleImageView(Context context) {
	    	this(context, null, 0);
	    }

	    public CircleImageView(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	    }

	    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        super.setScaleType(SCALE_TYPE);

	        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);

	        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
	        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);

	        a.recycle();

	        mReady = true;

	        if (mSetupPending) {
	            setup();
	            mSetupPending = false;
	        }
	    }

	    @Override
	    public ScaleType getScaleType() {
	        return SCALE_TYPE;
	    }

	    @Override
	    public void setScaleType(ScaleType scaleType) {
	        if (scaleType != SCALE_TYPE) {
	            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
	        }
	    }

	    @Override
	    protected void onDraw(Canvas canvas) {
	        if (getDrawable() == null) {
	            return;
	        }

	        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
	        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
	    }

	    @Override
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	        super.onSizeChanged(w, h, oldw, oldh);
	        setup();
	    }

	    public int getBorderColor() {
	        return mBorderColor;
	    }

	    public void setBorderColor(int borderColor) {
	        if (borderColor == mBorderColor) {
	            return;
	        }

	        mBorderColor = borderColor;
	        mBorderPaint.setColor(mBorderColor);
	        invalidate();
	    }

	    public int getBorderWidth() {
	        return mBorderWidth;
	    }

	    public void setBorderWidth(int borderWidth) {
	        if (borderWidth == mBorderWidth) {
	            return;
	        }

	        mBorderWidth = borderWidth;
	        setup();
	    }

	    @Override
	    public void setImageBitmap(Bitmap bm) {
	        super.setImageBitmap(bm);
	        mBitmap = bm;
	        setup();
	    }

	    @Override
	    public void setImageDrawable(Drawable drawable) {
	        super.setImageDrawable(drawable);
	        mBitmap = getBitmapFromDrawable(drawable);
	        setup();
	    }

	    @Override
	    public void setImageResource(int resId) {
	        super.setImageResource(resId);
	        mBitmap = getBitmapFromDrawable(getDrawable());
	        setup();
	    }

	    private Bitmap getBitmapFromDrawable(Drawable drawable) {
	        if (drawable == null) {
	            return null;
	        }

	        if (drawable instanceof BitmapDrawable) {
	            return ((BitmapDrawable) drawable).getBitmap();
	        }

	        try {
	            Bitmap bitmap;
	            if (drawable instanceof ColorDrawable) {
	                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
	            } else {
            		bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
	            }

	            Canvas canvas = new Canvas(bitmap);
	            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	            drawable.draw(canvas);
	            return bitmap;
	        } catch (OutOfMemoryError e) {
	            return null;
	        }
	    }

	    private void setup() {
	        if (!mReady) {
	            mSetupPending = true;
	            return;
	        }

	        if (mBitmap == null) {
	            return;
	        }

	        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

	        mBitmapPaint.setAntiAlias(true);
	        mBitmapPaint.setShader(mBitmapShader);

	        mBorderPaint.setStyle(Paint.Style.STROKE);
	        mBorderPaint.setAntiAlias(true);
	        mBorderPaint.setColor(mBorderColor);
	        mBorderPaint.setStrokeWidth(mBorderWidth);

	        mBitmapHeight = mBitmap.getHeight();
	        mBitmapWidth = mBitmap.getWidth();

	        mBorderRect.set(0, 0, getWidth(), getHeight());
	        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);

	        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
	        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);

	        updateShaderMatrix();
	        invalidate();
	    }

	    private void updateShaderMatrix() {
	        float scale;
	        float dx = 0;
	        float dy = 0;

	        mShaderMatrix.set(null);

	        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
	            scale = mDrawableRect.height() / (float) mBitmapHeight;
	            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
	        } else {
	            scale = mDrawableRect.width() / (float) mBitmapWidth;
	            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
	        }

	        mShaderMatrix.setScale(scale, scale);
	        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);

	        mBitmapShader.setLocalMatrix(mShaderMatrix);
	    }
	}
}