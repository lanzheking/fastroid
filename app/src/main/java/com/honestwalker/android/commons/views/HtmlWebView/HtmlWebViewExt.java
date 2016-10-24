package com.honestwalker.android.commons.views.HtmlWebView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.views.BaseMyViewRelativeLayout;

/**
 * Created by honestwalker on 15-7-22.
 */
public class HtmlWebViewExt extends BaseMyViewRelativeLayout {

    private View contentView;
    private View networkErrorView;
    private HtmlWebView webView;

    private ImageView networkErrorIV;

    private String TAG = "WEBVIEW-EXT";

    private String url = "";

//    private OnWebloadListener onWebloadListener;

    /** 引用控件的页面可以通过回调做一些事情 */
    private HtmlWebViewCallback mHtmlWebViewCallback;

    public HtmlWebViewExt(Context context) {
        super(context);
        initView();

    }

    public HtmlWebViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HtmlWebViewExt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public WebSettings getSettings() {
        return webView.getSettings();
    }

    private void initView() {
        contentView = inflate(context , R.layout.view_htmlwebviewext , this);
        networkErrorIV = (ImageView) contentView.findViewById(R.id.network_err_iv);
        int networkErrorIVWidth = (int) (screenWidth * 0.35);
        ViewSizeHelper.getInstance(context).setSize(networkErrorIV , networkErrorIVWidth , networkErrorIVWidth);
        networkErrorView = contentView.findViewById(R.id.network_err_layout);
        webView = (HtmlWebView) contentView.findViewById(R.id.htmlwebview);
        webView.setHtmlWebViewCallback(htmlWebViewCallback);
        networkErrorView.setClickable(true);
        networkErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(url);
                hasError = false;
                LogCat.d(TAG, "重试");
                if(mHtmlWebViewCallback != null) {
                    mHtmlWebViewCallback.onNetworkErrorRetry(webView , url);
                }
            }
        });
//        networkErrorView.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                LogCat.d(TAG , "重试");
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL: {
//                        webView.reload();
//                        hasError = false;
//                        networkErrorView.setVisibility(View.VISIBLE);
//                        LogCat.d(TAG , "重试");
//                    } break;
//                }
//                return false;
//            }
//        });
    }

    public void loadUrl(String url) {
        this.url = url;
        networkErrorView.setVisibility(View.GONE);
        webView.loadUrl(url);
    }

    public void reload() {
        webView.reload();
    }

    private boolean hasError = false;
    private HtmlWebViewCallback htmlWebViewCallback = new HtmlWebViewCallback() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(mHtmlWebViewCallback != null) {
                mHtmlWebViewCallback.shouldOverrideUrlLoading(view, url);
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogCat.d(TAG, "onPageStarted");
            if(mHtmlWebViewCallback != null) {
                mHtmlWebViewCallback.onPageStarted(view, url , favicon);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogCat.d(TAG, "onPageFinished");
            if(mHtmlWebViewCallback != null) {
                mHtmlWebViewCallback.onPageFinished(view, url);
            }
            if(!hasError) {
                networkErrorView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            LogCat.d(TAG, "onReceivedError");
            if(mHtmlWebViewCallback != null) {
                mHtmlWebViewCallback.onReceivedError(view, errorCode, description, failingUrl);
            }
            hasError = true;
//            networkErrorView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNetworkErrorRetry(WebView view, String url) {}
    };

    public void setHtmlWebViewCallback(HtmlWebViewCallback htmlWebViewCallback) {
        this.mHtmlWebViewCallback = htmlWebViewCallback;
    }

    public HtmlWebView getHtmlWebView() {
        return webView;
    }

    public String getUrl() {
        return webView.getUrl();
    }

//    public void setOnWebloadListener(OnWebloadListener onWebloadListener) {
//        this.onWebloadListener = onWebloadListener;
//    }

    public interface OnWebloadListener {
        public boolean shouldOverrideUrlLoading(WebView view, String url) ;
        public void onPageStarted(WebView view, String url, Bitmap favicon) ;
        public void onPageFinished(WebView view, String url) ;
    }

    public void addJavascriptInterface(Object obj, String interfaceName) {
        webView.addJavascriptInterface(obj , interfaceName);
    }

}
