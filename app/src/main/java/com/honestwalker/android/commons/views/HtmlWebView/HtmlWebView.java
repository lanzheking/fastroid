package com.honestwalker.android.commons.views.HtmlWebView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.commons.KancartReceiverManager;
import com.honestwalker.android.commons.jscallback.actionclass.JSCallbackObjectFilter;
import com.honestwalker.androidutils.Application;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.SharedPreferencesLoader;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.views.AlertDialogPage;

import java.net.URLDecoder;
import java.util.HashMap;

/**
 * Created by honestwalker on 15-6-2.
 */
public class HtmlWebView extends WebView {

    private Context context;

    private String cookies;

    private final String TAG = "HtmlWebView";

    private String lasterrorstrurl = "";

//    private RelativeLayout relative;
    private View networkErrorLayout;
    private Button reloadbtn;

    private HtmlWebViewCallback htmlWebViewCallback;

    public HtmlWebView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public HtmlWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public HtmlWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {

//        if (Build.VERSION.SDK_INT >= 19) {
//            getSettings().setLoadsImagesAutomatically(true);
//        } else {
//            getSettings().setLoadsImagesAutomatically(false);
//        }

        setWebViewClient(mWebViewClient);
        setWebChromeClient(webChromeClient);
//        setLayerType(LAYER_TYPE_HARDWARE, null);
//        setLayerType(LAYER_TYPE_SOFTWARE, null);

        getSettings().setSupportZoom(false);
        getSettings().setAllowFileAccess(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        getSettings().setCacheMode(getSettings().LOAD_DEFAULT);
        getSettings().setDomStorageEnabled(true);
        getSettings().setDatabaseEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        super.setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);

        //getSettings().setUserAgentString("FOSUN_TMC_" + Application.getAppVersion(context));
        getSettings().setUserAgentString(getSettings().getUserAgentString()+" SHEITC-YDZW/1.0");
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(new JSCallbackObjectFilter((Activity) context, this), "android");

    }

    @SuppressLint("JavascriptInterface")
    public void addJavascriptInterface(Object obj, String interfaceName) {
        super.addJavascriptInterface(obj , interfaceName);
    }

    private void initNetworkError() {

        View networkErrorParentLayout = getChildAt(0);
        networkErrorLayout = networkErrorParentLayout.findViewById(R.id.layout1);

//        if (getChildCount() > 0) {
//            networkErrorLayout = (LinearLayout) getChildAt(0);
//            RelativeLayout reloadBtnParentLayout = (RelativeLayout) networkErrorLayout.getChildAt(0);
////            relative.setVisibility(View.GONE);
//            reloadbtn = (Button) reloadBtnParentLayout.getChildAt(0);
//            reloadbtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    loadUrl(lasterrorstrurl);
////                    relative.setVisibility(View.GONE);
//                    networkErrorLayout.setVisibility(View.GONE);
//                }
//            });
//        }
    }

    public void loadUrl(String url) {
        lasterrorstrurl = url;
        syncCookie(context , url);
        super.loadUrl(url);
    }

    /**
     * 获取当前webview cookie
     * @param url
     * @return
     */
    private HashMap<String,String> getCurrentWebViewCookie(String url) {

        HashMap<String, String> cookieMapping = new HashMap<String , String>();

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        String cookieStr = cookieManager.getCookie(url);

        if(!StringUtil.isEmptyOrNull(cookieStr)) {
            String[] cookies = cookieStr.split(";");
            for(String cookieKV : cookies) {
                String[] kv = cookieKV.split("=");
                if(kv.length == 1) {
                    cookieMapping.put(kv[0] , "");
                } else if(kv.length == 2) {
                    cookieMapping.put(kv[0] , kv[1]);
                }
            }
        }

        return cookieMapping;

    }

    public synchronized void syncCookie(Context context, String url) {
        if(!url.startsWith("http:")){return;}
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);

        LogCat.d(TAG, " ");
        LogCat.d(TAG, " ");
        LogCat.d(TAG, url + " 开始同步cookie ");
        String cookie = SharedPreferencesLoader.getInstance(context).getString("cookie", "");

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        HashMap<String, String> newCookieMap = new HashMap<String , String>();
        {
//            {   // 读取全局cookie
//                HashMap<String , String> staticCookieMap = StaticCookieUtil.getStaticCookie(context);
//                LogCat.d(TAG, "");
//                LogCat.d(TAG, "读取全局cookie");
//                for(String staticCookie : staticCookieMap.keySet()) {
//                    LogCat.d(TAG, staticCookie + "=" + staticCookieMap.get(staticCookie));
//                    newCookieMap.put(staticCookie.trim() , staticCookieMap.get(staticCookie));
//                }
//                LogCat.d(TAG, "");
//            }

           // 整理cookie
            String webCookie = cookieManager.getCookie(url);
            if(!StringUtil.isEmptyOrNull(webCookie)) { // 先保存webcookie
                String[] webCookieStrs = webCookie.split(";");
                if(webCookieStrs != null && webCookieStrs.length > 0) {
                    for(String webCookieKV : webCookieStrs) {
                        String[] kv = webCookieKV.split("=");
                        if(kv.length == 1) {
                            newCookieMap.put(kv[0].trim() , "");
                        } else if(kv.length == 2) {
                            newCookieMap.put(kv[0].trim() , kv[1].trim());
                        }
                    }
                }
            }

            if(!StringUtil.isEmptyOrNull(cookie)) { // 添加本地cookie(如登录cookie)
                String[] cookieStrs = cookie.split(";");
                if(cookieStrs != null && cookieStrs.length > 0) {
                    for(String cookieKV : cookieStrs) {
                        String[] kv = cookieKV.split("=");
                        if(kv.length == 1) {
                            newCookieMap.put(kv[0].trim() , "");
                        } else if(kv.length == 2) {
                            newCookieMap.put(kv[0].trim() , kv[1].trim());
                        }
                    }
                }
            }

        }

        // 决定发行版本
//        String publish_version = ContextProperties.getConfig().publish_version;
//        LogCat.d("publish", "publish_version=" + publish_version);
//
//        if("release".equals(publish_version)) {
//            url = ContextProperties.getConfig().release_webHost;
//        } else if("test".equals(publish_version)) {
//            url = ContextProperties.getConfig().test_webHost;
//        } else if("debug".equals(publish_version)) {
//            url = ContextProperties.getConfig().debug_webHost;
//        } else if("localdev".equals(publish_version)) {
//            url = ContextProperties.getConfig().localdev_webHost;
//        }

        cookieManager.removeAllCookie();
        CookieManager cm = CookieManager.getInstance();
        for(String key : newCookieMap.keySet()) {
            cm.setCookie(url, key + "=" + newCookieMap.get(key));
            LogCat.d(TAG, "同步cookie添加 " + key + "=" + URLDecoder.decode(newCookieMap.get(key)));
        }

        cookieSyncManager.sync();

//        {   // 输出同步后的cookie
//            String newCookie = cookieManager.getCookie(url);
//            if(newCookie != null) {
//                LogCat.d(TAG, "同步后的cookie " + URLDecoder.decode(newCookie));
//            }
//        }

        LogCat.d(TAG, " ");
        LogCat.d(TAG, " ");

    }

    private WebViewClient mWebViewClient = new WebViewClient() {

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            LogCat.d("TEST", "shouldInterceptRequest2 " + request.getUrl());
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
//            LogCat.d("TEST", "onLoadResource  " + url);
//            if(url.indexOf("/discount/") > -1 ||
//                url.indexOf("/frontend/") > -1) {
//                syncCookie(context, url);
//                LogCat.d("TEST", "同步   " + url + " cookie");
//            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.startsWith("tel:")) {
                // 不需要权限，跳转到"拔号"中。
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                context.startActivity(callIntent);
                return true;
            }
            if(url.startsWith("mailto:")) {
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                context.startActivity(mailIntent);
                return true;
            }

            LogCat.d(TAG, "重定向:" + url);
            if(htmlWebViewCallback != null) {
                htmlWebViewCallback.shouldOverrideUrlLoading(view , url);
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if(htmlWebViewCallback != null) {
                htmlWebViewCallback.onPageStarted(view , url , favicon);
            }
            syncCookie(context, url);
            LogCat.d(TAG, ((Activity) context).getLocalClassName() + ",onPageStarted " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if(htmlWebViewCallback != null) {
                htmlWebViewCallback.onPageFinished(view , url);
            }

//            if (!getSettings().getLoadsImagesAutomatically()) {
//                getSettings().setLoadsImagesAutomatically(true);
//            }
//            if(Build.VERSION.SDK_INT >= 19) {
//                webView.getSettings().setLoadsImagesAutomatically(true);
//            } else {
//                webView.getSettings().setLoadsImagesAutomatically(false);
//            }

            KancartReceiverManager.sendBroadcast(context, KancartReceiverManager.Action.ACTION_WEB_ERROR);   //加载完成，发送广播关闭loading

            LogCat.d(TAG, ((Activity) context).getLocalClassName() + ",onPageFinished");
            /*if(relative!=null){
                relative.setVisibility(View.GONE);
            }*/
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            if(htmlWebViewCallback != null) {
                htmlWebViewCallback.onReceivedError(view , errorCode , description , failingUrl);
            }

//            LogCat.d(TAG , "onReceivedError " + errorCode);
//
//            loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            /**
//             * 加载页面出错时显示的界面
//             */
//            //loadUrl("file:///android_asset/errorSite.html");
//            KancartReceiverManager.sendBroadcast(context, KancartReceiverManager.Action.ACTION_WEB_ERROR);   //加载出错，发送广播关闭loading
//            if (networkErrorLayout != null) {
//                networkErrorLayout.setVisibility(View.VISIBLE);
//            }



        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient() {

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            ((Activity)context).startActivityForResult(Intent.createChooser(i, "File Chooser"), 2000);

        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            ((Activity)context).startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    3000);
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            ((Activity)context).startActivityForResult(Intent.createChooser(i, "File Chooser"), 10000);

        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            final AlertDialogPage dialog = new AlertDialogPage((Activity)context);
            dialog.setContent(message);
            dialog.setLeftBTNText("知道了");
            dialog.setTitleVisible(false);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    result.confirm();
                }
            });
            dialog.setSingleBTNOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    result.confirm();
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setHtmlWebViewCallback(HtmlWebViewCallback htmlWebViewCallback) {
        this.htmlWebViewCallback = htmlWebViewCallback;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        LogCat.d("TESTT", "scrollY=" + scrollY + "  clampedY=" + clampedY + "   ");
        if(clampedY) { // 滚动到底部通知页面
            callJSScrollEvent();
        }
    }

//    // 目的时为了让webview滑动时调用js ， 告诉h5 页面滑动了
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(onTouchListener != null) {
//            return onTouchListener.onTouch(this , event);
//        } else {
//            if(event.getAction() == MotionEvent.ACTION_MOVE) {
//                callJSScrollEvent();
//            } else if(event.getAction() == MotionEvent.ACTION_UP) {
//                // 因为自动滑动时无法监听，所以滑动放手后，要自动调用滑动js事件
//                ThreadPool.threadPool(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (int i = 0; i < 3; i++) {
//                            UIHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    callJSScrollEvent();
//                                }
//                            });
//                            ThreadPool.sleep(350);
//                        }
//                    }
//                });
//            }
//            return super.onTouchEvent(event);
//        }
//    }

    private void callJSScrollEvent() {
        LogCat.d("TEST", "aaa");
//        super.loadUrl("javascript:dispatchEvent(new Event('click'))");
        super.loadUrl("javascript:viewModels.currentModel.loadMore()");
    }

    private OnTouchListener onTouchListener;
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

}
