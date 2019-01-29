package com.honestwalker.android.commons.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebViewExt;

/**
 *
 * Created by lan zhe on 15-10-9.
 */
public class BaseWebViewFragment extends BaseTabIconFragment {

    private LinearLayout contentView;

    private HtmlWebViewExt webView;

    private String url = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = (LinearLayout) inflater.inflate(R.layout.fragment_web , null);

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
