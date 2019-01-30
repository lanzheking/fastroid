package com.honestwalker.android.commons.views.HtmlWebView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.honestwalker.android.commons.bean.JSParam;
import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.UIHandler;
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

    private boolean networkRetryLock = false;
    private void initView() {
        contentView = inflate(context , R.layout.view_htmlwebviewext , this);
        networkErrorIV = (ImageView) contentView.findViewById(R.id.network_err_iv);
        int networkErrorIVWidth = (int) (screenWidth * 0.35);
        ViewSizeHelper.getInstance(context).setSize(networkErrorIV , networkErrorIVWidth , networkErrorIVWidth);
        networkErrorView = contentView.findViewById(R.id.network_err_layout);
        webView = (HtmlWebView) contentView.findViewById(R.id.htmlwebview);
//        webView.setHtmlWebViewCallback(htmlWebViewCallback);
        networkErrorView.setClickable(true);
        networkErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(networkRetryLock) return;

                webView.loadUrl(url);

                networkRetryLock = true;
                UIHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        networkRetryLock = false;
                    }
                }, 2000);

                LogCat.d(TAG, "重试");
                if(mHtmlWebViewCallback != null) {
                    mHtmlWebViewCallback.onNetworkErrorRetry(webView , url);
                }
            }
        });

        webView.setDownloadListener(downloadListener);

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

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        this.url = baseUrl;
        networkErrorView.setVisibility(View.GONE);
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
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
            hasError = false;
//            HermesEventBus.getDefault().post(new WebPageFinishedEvent(webView, url));
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            LogCat.d(TAG, "onReceivedError");
            if(mHtmlWebViewCallback != null) {
                mHtmlWebViewCallback.onReceivedError(view, errorCode, description, failingUrl);
            }
            hasError = true;
            networkErrorView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNetworkErrorRetry(WebView view, String url) {}
    };

    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri=Uri.parse(url);
            Intent intent=new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        }
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

//    /**
//     * 开启js callback
//     * @param applicationId
//     */
//    public void enableJsCallback(String applicationId) {
//        webView.enableJsCallback(applicationId);
//    }

    public void setUserAgent(String userAgent) {
        webView.setUserAgent(userAgent);
    }

    public void setHost(String webHost) {
        webView.setHost(webHost);
    }

    public void goBack() {
        webView.goBack();
    }

    public void resetFileChoose() {
        webView.resetFileChoose();
    }

    public ValueCallback getmUploadMessage() {
        return webView.getmUploadMessage();
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

//    public void addInterceptRequest(InterceptRequest interceptRequest) {
//        webView.addInterceptRequest(interceptRequest);
//    }

    public void execJS(String method) {
        execJS(method, null);
    }

    public void execJS(String method, JSParam paramKVMap) {
        webView.execJS(method, paramKVMap);
    }

}
