package com.honestwalker.androidutils.views.xutilImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;

import org.xutils.common.Callback;
import org.xutils.x;

/**
 * 异步图片工具
 */
public class AsyncImageView extends RelativeLayout{

	private Context context;

	private ImageView imageView;
	private ProgressBar progressBar;
	
	private View maskView;
	private boolean allowMask = false;
	public void setAllowMask(boolean allowMask) {
		this.allowMask = allowMask;
	}


	public ImageView getImageView() {
		return imageView;
	}
	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public AsyncImageView(Context context) {
		this(context, null, 0);
	}

	public AsyncImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AsyncImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	private void init() {
		
		imageView = new ImageView(context);
		LayoutParams ivlp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		this.addView(imageView, ivlp);
		//图片载出之前，默认为灰色
		imageView.setBackgroundColor(Color.argb(255,230, 230, 230));
		
		progressBar = new ProgressBar(context);
		LayoutParams lp = new LayoutParams(20, 20);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.addView(progressBar, lp);
		
		if (allowMask) {
			maskView = new View(context);
			LayoutParams vlp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			this.addView(maskView, vlp);
			maskView.setBackgroundColor(Color.argb(100, 0, 0, 0));
			maskView.setVisibility(View.GONE);
		}
		
	}

	public void setScaleType(ScaleType scaleType) {
		imageView.setScaleType(scaleType);
	}

	public void setImageBitmap(Bitmap bitmap){
		
		imageView.setImageBitmap(bitmap);
	}

	public void setImageBitmap(Bitmap bitmap, int width, int height) {
		ViewSizeHelper.getInstance(context).setWidth(this, width);
		ViewSizeHelper.getInstance(context).setHeight(this, height);
		ViewSizeHelper.getInstance(context).setWidth(imageView, width);
		ViewSizeHelper.getInstance(context).setHeight(imageView, height);
		imageView.setImageBitmap(bitmap);
	}

	public void setImageDrawable(Drawable drawable) {
		imageView.setImageDrawable(drawable);
	}
	
	public void loadUrl(String imageUrl, int width, int height) {
		ViewSizeHelper.getInstance(context).setWidth(this, width);
		ViewSizeHelper.getInstance(context).setHeight(this, height);
		ViewSizeHelper.getInstance(context).setWidth(imageView, width);
		ViewSizeHelper.getInstance(context).setHeight(imageView, height);
		x.image().bind(new ImageView(context), imageUrl, commonCallback);
	}

	private Callback.CommonCallback<Drawable> commonCallback = new Callback.CommonCallback<Drawable>() {
		@Override
		public void onSuccess(Drawable drawable) {
			progressBar.setVisibility(View.GONE);
			setImageDrawable(drawable);
		}

		@Override
		public void onError(Throwable ex, boolean isOnCallback) {

		}

		@Override
		public void onCancelled(CancelledException cex) {

		}

		@Override
		public void onFinished() {

		}
	};
	
	private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(android.R.color.transparent);

    private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
        final TransitionDrawable transitionDrawable =
                new TransitionDrawable(new Drawable[]{
                        TRANSPARENT_DRAWABLE,
                        new BitmapDrawable(imageView.getResources(), bitmap)
                });
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(500);
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (allowMask) {
				maskView.setVisibility(View.VISIBLE);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (allowMask) {
				maskView.setVisibility(View.GONE);
			}
			break;
		}
		
		return super.onTouchEvent(event);
	}
}