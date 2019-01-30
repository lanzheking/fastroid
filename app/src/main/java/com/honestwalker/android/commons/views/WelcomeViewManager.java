package com.honestwalker.android.commons.views;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.honestwalker.android.BusEvent.EventBusUtil;
import com.honestwalker.android.commons.eventbus.event.WelcomeViewHidden;
import com.honestwalker.androidutils.UIHandler;

/**
 * Created by lanzhe on 17-5-27.
 */

public class WelcomeViewManager {

    private View contairView;
    private Bitmap[] willRecyleBitmaps;
    private ImageView[] willRecyleImageViews;

    private boolean isWelcomeViewHidden = false;

    private long startWelcomeTime = 0;

    private long minTime = 0;
    private long maxTime = 0;

    private long animTime = 1000;

    /**
     *startWelcomeTime
     * @param contairView   欢迎页面view容器
     * @param maxTime       欢迎页面显示的最大时间， <= 0 就是没有载入数据就不关闭
     * @param minTime       欢迎页面显示的最小时间， <= 0 忽略最小时间
     */
    public WelcomeViewManager(View contairView, long maxTime, long minTime) {
        this.contairView = contairView;
        this.maxTime = maxTime;
        this.minTime = minTime;
    }

    public void setWillRecyleBitmap(Bitmap... bitmaps) {
        this.willRecyleBitmaps = bitmaps;
    }

    public void setWillRecyleImageViews(ImageView... imageViews) {
        this.willRecyleImageViews = imageViews;
    }

    public boolean isWelcomeViewHidden() {
        return isWelcomeViewHidden;
    }

    public void setAnimTime(long animTime) {
        this.animTime = animTime;
    }

    public void startWelcomeAction() {
        startWelcomeTime = System.currentTimeMillis();
        if(maxTime > 0) {
            UIHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hiddenWelcome();
                }
            }, maxTime);
        }
    }

    private boolean isRequestedHiddenWelcome = false;
    /**
     * 请求关闭welcome, 但不一定立即关闭, 有最大时间和数据请求完毕的纬度
     */
    public void requestHiddenWelcome() {
        if(isRequestedHiddenWelcome) return;
        isRequestedHiddenWelcome = true;

        long timelimit = System.currentTimeMillis() - startWelcomeTime;
        if(minTime > 0 && minTime - timelimit > 0) {
            UIHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hiddenWelcome();
                }
            }, minTime - timelimit);
        } else {
            hiddenWelcome();
        }

    }

    /**
     * 直接关闭welcome
     */
    public void hiddenWelcome() {
        if(isWelcomeViewHidden) return;
        isWelcomeViewHidden = true;
        Animation hiddenAction = new AlphaAnimation(1.0f, 0.0f);
        hiddenAction.setDuration(animTime);
        hiddenAction.setFillAfter(true);

        hiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {

                contairView.setVisibility(View.GONE);

                if(willRecyleImageViews != null) {
                    for (ImageView imageView : willRecyleImageViews) {
                        imageView.setImageBitmap(null);
                    }
                }

                if(willRecyleBitmaps != null) {
                    for (Bitmap bitmap : willRecyleBitmaps) {
                        try {
                            if(!bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        } catch (Exception e){}
                    }
                }

                EventBusUtil.getInstance().post(new WelcomeViewHidden());

            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        contairView.startAnimation(hiddenAction);
    }

}
