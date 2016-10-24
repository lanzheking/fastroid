package com.honestwalker.android.commons.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.commons.menu.BaseWebViewFragment;
import com.honestwalker.android.commons.utils.IntentBind.IntentBind;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * 通用的web activity 父类
 */
public class BaseWebActivity extends BaseActivity {

    @IntentBind
    private String url = "";

    private BaseWebViewFragment webViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);

        webViewFragment = new BaseWebViewFragment();

        webViewFragment.setUrl(url);

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.add(R.id.fragment_web_layout , webViewFragment);

        trans.commit();

    }

    protected void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected void loadData() {

    }

    protected void loadUrl(String url) {
        webViewFragment.loadUrl(url);
    }

}
