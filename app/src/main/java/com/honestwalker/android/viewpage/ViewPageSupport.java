package com.honestwalker.android.viewpage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.honestwalker.android.BusEvent.EmptyEvent;
import com.honestwalker.android.commons.utils.StartActivityHelper;
import com.honestwalker.android.spring.context.ApplicationContext;
import com.honestwalker.android.spring.context.ApplicationContextUtils;
import com.honestwalker.android.spring.core.inject.Injection;
import com.honestwalker.android.spring.exception.BeanNotFoundException;
import com.honestwalker.android.viewpage.annotation.ViewPage;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import org.greenrobot.eventbus.Subscribe;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * Created by lanzhe on 17-11-13.
 */
public abstract class ViewPageSupport {

    public static final String TAG = "VIEWPAGE";

    protected View contentView;

    protected ViewPageActivity context;

    public ViewPageSupport() {
        ApplicationContextUtils.inject(this);
    }

    public void init(ViewPageActivity containerActivity) {
        if(this.context != null) return;
        HermesEventBus.getDefault().register(this);
        this.context = containerActivity;
        ViewPage viewPage = this.getClass().getAnnotation(ViewPage.class);
        if(viewPage == null) { // 处理ViewPage注解在父类的情况
            Class currentClass = this.getClass();
            while( currentClass != Object.class ) {
                currentClass = currentClass.getSuperclass();
                viewPage = (ViewPage) currentClass.getAnnotation(ViewPage.class);
                if(viewPage != null) break;
            }
        }
        if(viewPage != null && viewPage.value() > 0) {
            contentView = this.context.getLayoutInflater().inflate(viewPage.value(), containerActivity.getContainerView());

            Injection.inject(this, containerActivity, contentView);

        } else {
            LogCat.d(TAG, "layout=" + null);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {}

    protected void onStart() {}

    protected void onRestart() {}

    protected void onRestoreInstanceState(Bundle savedInstanceState) {}

    protected void onNewIntent(Intent intent) {}

    protected void onResume() {}

    protected void onPause() {}

    protected void onStop() {}

    protected void onDestroy() {}

    protected void onBackPressed(){
        finish();
    }

    public void finish() {
        context.finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){}

    public View getContentView() {
        return contentView;
    }

    public Intent getIntent() {
        return context.getIntent();
    }

    public static void toPage(Context context, Class<? extends ViewPageSupport> viewPageSupportClass) {
        toPage(context, viewPageSupportClass, null);
    }

    public static void toPage(Context context, Class<? extends ViewPageSupport> viewPageSupportClass, Intent intent) {
        if(intent == null) intent = new Intent(context, ViewPageActivity.class);
        intent.setClass(context, ViewPageActivity.class);
        intent.putExtra("viewPageClassName", viewPageSupportClass.getName());
        StartActivityHelper.toActivity(context, ViewPageActivity.class, intent, StartActivityHelper.ANIM_ENTER_FAST);
    }

    public static void toPage(Context context, String viewPageBeanName) {
        toPage(context, viewPageBeanName, null);
    }

    public static void toPage(Context context, String viewPageBeanName, Intent intent) {
        if(intent == null) intent = new Intent(context, ViewPageActivity.class);
        try {
            if(ApplicationContextUtils.getApplicationContext().getBean(viewPageBeanName) == null) {
                throw new BeanNotFoundException(viewPageBeanName);
            }
        } catch (Exception e) {
            ExceptionUtil.showException("Spring", e);
            ExceptionUtil.showException(e);
            return ;
        }
        intent.setClass(context, ViewPageActivity.class);
        intent.putExtra("viewPageBeanName", viewPageBeanName);
        StartActivityHelper.toActivity(context, ViewPageActivity.class, intent, StartActivityHelper.ANIM_ENTER_FAST);
    }

    public static void toPageForResult(Activity context, Class<? extends ViewPageSupport> viewPageSupportClass, Intent intent, int requestCode) {
        if(intent == null) intent = new Intent(context, ViewPageActivity.class);
        intent.setClass(context, ViewPageActivity.class);
        intent.putExtra("viewPageClassName", viewPageSupportClass.getName());
        StartActivityHelper.toActivityForResult(context, ViewPageActivity.class, intent, requestCode);
    }

    public static void toPageForResult(Activity context, String viewPageBeanName, Intent intent, int requestCode) {
        if(intent == null) intent = new Intent(context, ViewPageActivity.class);
        try {
            if(ApplicationContextUtils.getApplicationContext().getBean(viewPageBeanName) == null) {
                throw new BeanNotFoundException(viewPageBeanName);
            }
        } catch (Exception e) {
            ExceptionUtil.showException("Spring", e);
            ExceptionUtil.showException(e);
            return ;
        }
        intent.setClass(context, ViewPageActivity.class);
        intent.putExtra("viewPageBeanName", viewPageBeanName);
        StartActivityHelper.toActivityForResult(context, ViewPageActivity.class, intent, requestCode);
    }

    public void removeFromContainer() {
        context.getContainerView().removeView(this.getContentView());
    }

    @Subscribe
    public void emptyEvent(EmptyEvent event){/* do noting */}

}
