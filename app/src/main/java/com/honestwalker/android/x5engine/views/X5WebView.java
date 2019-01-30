package com.honestwalker.android.x5engine.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.honestwalker.android.commons.bean.JSParam;
import com.honestwalker.android.commons.jscallback.actions.JSCallbackObject;
import com.honestwalker.android.x5engine.FileChooser;
import com.honestwalker.android.x5engine.InterceptRequest;
import com.honestwalker.android.x5engine.OnOpenFileChooser;
import com.honestwalker.android.x5engine.X5WebResourceLoadCallback;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.UIHandler;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class X5WebView extends WebView {

	private Context context;

	public static final int REQUEST_CODE_Choose_FILE = 101;

	private ValueCallback<Uri> valueCallback;

	private String sourceFilePath;

	private static boolean DEBUG = false;

	private WebChromeClient mWebViewClient;

	private FileChooser fileChooser;

	private OnOpenFileChooser onOpenFileChooser;

	private List<InterceptRequest> interceptRequests = new ArrayList<>();

	private X5WebResourceLoadCallback x5WebResourceLoadCallback;

	public X5WebView(Context context) {
		super(context);
		this.context = context;
		setBackgroundColor(85621);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(Context context, AttributeSet arg1) {
		super(context, arg1);
		this.context = context;
		initWebViewSettings();
		this.getView().setClickable(true);
		String x5UserAgent = "";
		if(isX5LoadSuccess) {
			x5UserAgent = " X5/" + X5WebView.getTbsCoreVersion(context);
		}
		this.getSettings().setUserAgent(this.getSettings().getUserAgentString() + x5UserAgent);
		super.setWebChromeClient(webChromeClient);
		super.setWebViewClient(webViewClient);
		fileChooser = new FileChooser((Activity) context, this);
	}

	private WebViewClient webViewClient = new WebViewClient() {

		@Override
		public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
			super.onReceivedError(webView, webResourceRequest, webResourceError);
			LogCat.d("X5E", "Load error " + webResourceRequest.getUrl() +
					" code=" + webResourceError.getErrorCode() + " " + webResourceError.getDescription());
		}

		@Override
		public void onLoadResource(WebView webView, String url) {
			super.onLoadResource(webView, url);
			if(x5WebResourceLoadCallback != null) {
				if (x5WebResourceLoadCallback.onLoadResource(webView, url)) {
					return;
				}
			}
		}

		@Override
		public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
			super.onPageStarted(webView, url, bitmap);
			LogCat.d("X5", "start " + url);
//			HermesEventBus.getDefault().post(new WebPageStartEvent((X5WebView) webView, url));
			if(x5WebResourceLoadCallback != null) {
				if (x5WebResourceLoadCallback.onPageStarted(webView, url, bitmap)) {
					return;
				}
			}
		}

		@Override
		public void onPageFinished(WebView webView, String url) {

			LogCat.d("X5", "complate " + url);
//			HermesEventBus.getDefault().post(new WebPageFinishedEvent((X5WebView) webView, url));
			if (x5WebResourceLoadCallback != null) {
				if (x5WebResourceLoadCallback.onPageFinished(webView, url)) {
					return;
				}
			}

			super.onPageFinished(webView, url);

		}

		@Override
		public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
			super.onReceivedSslError(webView, sslErrorHandler, sslError);
			if(x5WebResourceLoadCallback != null) {
				if(x5WebResourceLoadCallback.onReceivedSslError(webView, sslErrorHandler, sslError)) {
					return;
				}
			}
			sslErrorHandler.proceed();
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
			LogCat.d("URL", "收到请求 " + webResourceRequest.getUrl().toString());
			try {
				String url = webResourceRequest.getUrl().toString();
				if(interceptRequests != null && url != null) {
					for (InterceptRequest interceptRequest : interceptRequests) {
						if(interceptRequest.interceptRule(X5WebView.this, this, url)) {
							LogCat.d("URL", "拦截URL " + url);
							WebResourceResponse webResourceResponse = interceptRequest.getResponse(X5WebView.this, this, url);
							if(webResourceResponse != null) {
								return webResourceResponse;
							} else {
								break;
							}
						}
					}
				}
			} catch (Exception e) {}

			return super.shouldInterceptRequest(webView, webResourceRequest);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView webView, String url) {
			if(x5WebResourceLoadCallback != null) {
				if(x5WebResourceLoadCallback.shouldOverrideUrlLoading(webView, url)) {
					return true;
				}
			}
			if (url.startsWith("tel:")) {
				Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
				context.startActivity(tel);
				return true;
			}else{
				webView.loadUrl(url);
				return true;
			}
		}
	};

	private WebChromeClient webChromeClient = new WebChromeClient() {

        @Override

        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        	LogCat.d("FILE", "监听到文件选择");
            if(onOpenFileChooser != null) {
                boolean openFileChooserResult = onOpenFileChooser.onShowFileChooser(webView, valueCallback, fileChooserParams);
                if(openFileChooserResult) {
                    return true;
                }
            }
            fileChooser.choose();
            return true;
        }

//        // For Android 4.1
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//            if(onOpenFileChooser != null) {
//                boolean openFileChooserResult = onOpenFileChooser.onShowFileChooser(webView, valueCallback, fileChooserParams);
//                if(openFileChooserResult) {
//                    return true;
//                }
//            }
//            fileChooser.choose();
//        }

//      @Override
//		public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> valueCallback, String acceptType, String capture) {
//		  LogCat.d("FILE", "openFileChooser called");
//			LogCat.d("FILE", "拦截文件上传 acceptType=" + acceptType);
////			if(onOpenFileChooser != null) {
////				boolean openFileChooserResult = onOpenFileChooser.openFileChooser(valueCallback, acceptType, capture);
////					if(openFileChooserResult) {
////					return;
////				}
////			}
////			fileChooser.choose();
//		}

		@Override
		public void onProgressChanged(WebView webView, int progressInPercent) {
			super.onProgressChanged(webView, progressInPercent);
			Log.d("webview", "progressInPercent=" + progressInPercent);
			if(x5WebResourceLoadCallback != null) {
				if(x5WebResourceLoadCallback.onProgressChanged(webView, progressInPercent)) {
					return;
				}
			}
		}

		@Override
		public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
			if(mWebViewClient != null) {
				return mWebViewClient.onJsConfirm(webView, s, s1, jsResult);
			}
			return super.onJsConfirm(webView, s, s1, jsResult);
		}

		@Override
		public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
			if(mWebViewClient != null) {
				return mWebViewClient.onJsAlert(webView, s, s1, jsResult);
			}
			return super.onJsAlert(webView, s, s1, jsResult);
		}

		@Override
		public boolean onJsPrompt(WebView webView, String s, String s1, String s2, JsPromptResult jsPromptResult) {
			if(mWebViewClient != null) {
				return mWebViewClient.onJsPrompt(webView, s, s1, s2, jsPromptResult);
			}
			return super.onJsPrompt(webView, s, s1, s2, jsPromptResult);
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			if(mWebViewClient != null) {
				return mWebViewClient.onConsoleMessage(consoleMessage);
			}
			return super.onConsoleMessage(consoleMessage);
		}
	};

	/**
	 * 开启JS CALLBACK (JS API)
	 * @param activity
     */
	public void setJSCallback(Activity activity) {
		addJavascriptInterface(new JSCallbackObject(activity, this), "jsApiBridge");
	}

	/**
	 * 开启JS CALLBACK (JS API)
	 * @param activity
	 * @param jsObjName js 对象名
	 */
	public void setJSCallback(Activity activity, String jsObjName) {
		addJavascriptInterface(new JSCallbackObject(activity, this), jsObjName);
	}

	/** 初始化设置 */
	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(false);
		webSetting.setBuiltInZoomControls(false);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(true);
		// webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(false);
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSetting.setSupportZoom(false);
		webSetting.setTextZoom(100);

		// this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
		// settings 的设计
	}

	public void setDebug(boolean isDebug) {
		X5WebView.DEBUG = isDebug;
	}

	/** debug 信息 */
	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		if(!X5WebView.DEBUG) {
			return super.drawChild(canvas, child, drawingTime);
		}
//		if(!X5WebView.isX5LoadSuccess) {
//			return super.drawChild(canvas, child, drawingTime);
//		}
		boolean ret = super.drawChild(canvas, child, drawingTime);
		canvas.save();
		Paint paint = new Paint();
		paint.setColor(0x7fff0000);
		paint.setTextSize(24.f);
		paint.setAntiAlias(true);
		if (getX5WebViewExtension() != null) {
			canvas.drawText(this.getContext().getPackageName() + "-pid:"
					+ android.os.Process.myPid(), 10, 50, paint);
			canvas.drawText(
					"X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 10,
					100, paint);
		} else {
			canvas.drawText(this.getContext().getPackageName() + "-pid:"
					+ android.os.Process.myPid(), 10, 50, paint);
			canvas.drawText("Sys Core", 10, 100, paint);
		}
		canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
		canvas.drawText(Build.MODEL, 10, 200, paint);
		canvas.restore();
		return ret;
	}

	public void execJS(final String cmd) {
		UIHandler.post(new Runnable() {
			@Override
			public void run() {
				loadUrl("javascript: " + cmd);
			}
		});
	}

	public void execJS(final String method, final JSParam paramKVMap) {
		UIHandler.post(new Runnable() {
			@Override
			public void run() {
				String newMethod = method;
				if(newMethod == null) {
					LogCat.d("JS", "callback null");
					return;
				}
				if(paramKVMap != null) {
					for (String key : paramKVMap.getParams().keySet()) {
						if(key instanceof String) {
							newMethod = newMethod.replace("${" + key + "}", "'" + paramKVMap.getParams().get(key) + "'");
						} else {
							newMethod = newMethod.replace("${" + key + "}", paramKVMap.getParams().get(key) + "");
						}
					}
				}
				LogCat.d("JS", "执行 " + newMethod);
				loadUrl("javascript: " + newMethod);
			}
		});
	}

	public void testJsAPI(HashMap<String, Object> params) {
		String paramJson = new Gson().toJson(params);
		String cmd = "window.jsApiBridge.app_callback(${params})";
		cmd = cmd.replace("${params}", "'" + paramJson + "'");
		LogCat.d("JS", cmd);
		execJS(cmd);
	}

	public ValueCallback<Uri> getValueCallback() {
		return valueCallback;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	private static boolean isX5LoadSuccess = false;

	public static boolean isX5LoadSuccess() {
		return isX5LoadSuccess;
	}

	public static void setIsX5LoadSuccess(boolean isX5LoadSuccess) {
		X5WebView.isX5LoadSuccess = isX5LoadSuccess;
	}

	public FileChooser getFileChooser() {
		return fileChooser;
	}

	public interface WebLoadProgress {
		void onChanged(String url, int i);
	}

	public void setOnOpenFileChooser(OnOpenFileChooser onOpenFileChooser) {
		this.onOpenFileChooser = onOpenFileChooser;
	}

	//	public void addJSCallback(Activity context, String appId) {
//		addJavascriptInterface(new JSCallbackObject(context, this, appId), "jsApiBridge");
//	}

	public void setWebChromeClient(WebChromeClient webUIClient) {
		this.mWebViewClient = webUIClient;
	}

	public void resetFileChoose() {
		valueCallback.onReceiveValue(null);
	}

	public void addUserAgent(String userAgent) {
		getSettings().setUserAgentString(getSettings().getUserAgentString() + " " + userAgent);
	}

	/**
	 * 添加请求拦截器
	 * @param interceptRequest
     */
	public void addInterceptRequest(InterceptRequest interceptRequest) {
		if(interceptRequest != null) {
			interceptRequests.add(interceptRequest);
		}
	}
	/**
	 * 删除请求拦截机器
	 */
	public void removeInterceptRequest(InterceptRequest interceptRequest) {
		if(interceptRequest != null) {
			interceptRequests.remove(interceptRequest);
		}
	}

	public void setX5WebResourceLoadCallback(X5WebResourceLoadCallback x5WebResourceLoadCallback) {
		this.x5WebResourceLoadCallback = x5WebResourceLoadCallback;
	}
}
