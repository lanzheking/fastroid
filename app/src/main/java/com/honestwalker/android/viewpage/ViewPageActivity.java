package com.honestwalker.android.viewpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.honestwalker.android.commons.activity.BaseActivity;
import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.spring.context.ApplicationContext;
import com.honestwalker.android.spring.context.ApplicationContextUtils;
import com.honestwalker.android.spring.core.annotation.Autowired;
import com.honestwalker.android.spring.core.annotation.IntentInject;
import com.honestwalker.android.spring.core.inject.Injection;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;

/**
 * Created by lanzhe on 17-11-13.
 */
public class ViewPageActivity extends BaseActivity {

    private static final String TAG = "VIEWPAGE";

    private LinearLayout containerView;

    private ViewPageSupport viewPage;

    @IntentInject
    private String viewPageClassName;

    @IntentInject
    private String viewPageBeanName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpage_main);
        ApplicationContextUtils.inject(this);
        Injection.inject(this);

        containerView = (LinearLayout) findViewById(R.id.viewpage_container);

        loadViewPage();

        viewPage.onCreate(savedInstanceState);

    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewPage.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        viewPage.onRestart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPage.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        viewPage.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPage.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewPage.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewPage.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPage.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        viewPage.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        viewPage.onBackPressed();
    }

    private void loadViewPage() {
        try {
            if(!StringUtil.isEmptyOrNull(viewPageClassName)) {
                Class viewPageClass = Class.forName(viewPageClassName);
                viewPage = (ViewPageSupport) viewPageClass.newInstance();
            } else if(!StringUtil.isEmptyOrNull(viewPageBeanName)) {
                viewPage = (ViewPageSupport) ApplicationContextUtils.getApplicationContext().getBean(viewPageBeanName);
            }
            viewPage.init(this);

            ViewGroup.LayoutParams lp = viewPage.getContentView().getLayoutParams();
            LogCat.d(TAG, "w=" + lp.width + "  " + lp.height);
        } catch (Exception e) {
            ExceptionUtil.showException(TAG, e);
        }
    }

    public ViewPageSupport getViewPage() {
        return viewPage;
    }

    public LinearLayout getContainerView() {
        return containerView;
    }

    public void startPage(Class<? extends ViewPageSupport> viewPageSupportClass) {
        ViewPageSupport.toPage(this, viewPageSupportClass);
    }

    public void startPage(Class<? extends ViewPageSupport> viewPageSupportClass, Intent intent) {
        ViewPageSupport.toPage(this, viewPageSupportClass, intent);
    }

}
