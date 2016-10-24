package com.stay.pull.lib.internal;

import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.IO.LogCat;
import com.stay.pull.lib.PullToRefreshBase;

public class LoadingLayout extends FrameLayout {

	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

	private final ImageView headerImage;
	private final ProgressBar headerProgress;
	private final TextView headerText;

	private String pullLabel;
	private String refreshingLabel;
	private String releaseLabel;
	private String lastUpdateTimeLabel;
	
	private long lastUpdateTime = 0;

	private final Animation rotateAnimation, resetRotateAnimation;
	
	private boolean showLastUpdateTime = false;

	public LoadingLayout(Context context, final int mode, String releaseLabel, String pullLabel, String refreshingLabel , String lastUpdateTimeLabel) {
		super(context);
		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.view_pull_to_refresh_header, this);
		headerText = (TextView) header.findViewById(R.id.pull_to_refresh_text);
		headerImage = (ImageView) header.findViewById(R.id.pull_to_refresh_image);
		headerProgress = (ProgressBar) header.findViewById(R.id.pull_to_refresh_progress);

		final Interpolator interpolator = new LinearInterpolator();
		rotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		        0.5f);
		rotateAnimation.setInterpolator(interpolator);
		rotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		rotateAnimation.setFillAfter(true);

		resetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
		        Animation.RELATIVE_TO_SELF, 0.5f);
		resetRotateAnimation.setInterpolator(interpolator);
		resetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		resetRotateAnimation.setFillAfter(true);

		this.releaseLabel = releaseLabel;
		this.pullLabel = pullLabel;
		this.refreshingLabel = refreshingLabel;
		this.lastUpdateTimeLabel = lastUpdateTimeLabel;
		
		switch (mode) {
			case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
				headerImage.setImageResource(R.drawable.pulltorefresh_up_arrow);
				break;
			case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
			default:
				headerImage.setImageResource(R.drawable.pulltorefresh_down_arrow);
				break;
		}
	}

	public synchronized void reset() {
		LogCat.d("test","1 reset  " + lastUpdateTime);
		if(lastUpdateTime == 0) {
			LogCat.d("test","1 ");
			headerText.setText(pullLabel);
		} if(System.currentTimeMillis() - lastUpdateTime < 5000) {
			LogCat.d("test","2 lastUpdateTime=" + lastUpdateTime);
			Date d = new Date(lastUpdateTime);
			if(showLastUpdateTime) {
				headerText.setText(pullLabel + "\r\n" + lastUpdateTimeLabel + "：" + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
			} else {
				headerText.setText(pullLabel);
			}
		} else {
//			if(lastUpdateTime == 0) {
//				headerText.setText(pullLabel);
//			} else {
			if(showLastUpdateTime) {
				Date d = new Date(lastUpdateTime);
				headerText.setText(pullLabel + "\r\n" + lastUpdateTimeLabel + "：" + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds());
			} else {
				headerText.setText(pullLabel);
			}
//			}
		}
		headerImage.setVisibility(View.VISIBLE);
		headerProgress.setVisibility(View.GONE);
		lastUpdateTime = System.currentTimeMillis();
	}

	public void releaseToRefresh() {
		LogCat.d("test","releaseToRefresh");
		headerText.setText(releaseLabel);
		headerImage.clearAnimation();
		headerImage.startAnimation(rotateAnimation);
	}

	public void setPullLabel(String pullLabel) {
		this.pullLabel = pullLabel;
	}

	public void refreshing() {
		LogCat.d("test","refreshing");
		headerText.setText(refreshingLabel);
		headerImage.clearAnimation();
		headerImage.setVisibility(View.INVISIBLE);
		headerProgress.setVisibility(View.VISIBLE);
		lastUpdateTime = System.currentTimeMillis();
	}

	public void setRefreshingLabel(String refreshingLabel) {
		this.refreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(String releaseLabel) {
		this.releaseLabel = releaseLabel;
	}

	public void pullToRefresh() {
		LogCat.d("test","pullToRefresh");
		headerText.setText(pullLabel);
		headerImage.clearAnimation();
		headerImage.startAnimation(resetRotateAnimation);
	}

	public void setTextColor(int color) {
		headerText.setTextColor(color);
	}

}
