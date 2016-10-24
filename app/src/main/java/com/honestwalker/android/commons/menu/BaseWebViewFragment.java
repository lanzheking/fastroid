package com.honestwalker.android.commons.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebViewExt;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 *
 * Created by lan zhe on 15-10-9.
 */
public class BaseWebViewFragment extends BaseTabIconFragment {

    private LinearLayout contentView;

//    @ViewInject(R.id.fragment_webview)
    private HtmlWebViewExt webView;

    private String url = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = (LinearLayout) inflater.inflate(R.layout.fragment_web , null);
//        ViewUtils.inject(this, contentView);

        webView = (HtmlWebViewExt) contentView.findViewById(R.id.fragment_webview);

        if(getMenubarItemBean() != null) {
            setUrl(getMenubarItemBean().getMenubarPageBean().getTargetUrl());
        }

        webView.loadUrl(url);

        return contentView;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void loadUrl(String url) {
        setUrl(url);
        webView.loadUrl(url);
    }

}
