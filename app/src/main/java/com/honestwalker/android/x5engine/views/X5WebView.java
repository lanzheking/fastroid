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

import com.honestwalker.android.BusEvent.EventBusUtil;
import com.honestwalker.android.BusEvent.event.WebProgressChangedEvent;
import com.honestwalker.android.commons.jscallback.actions.JSCallbackObject;
import com.honestwalker.android.commons.bean.JSParam;
import com.honestwalker.android.x5engine.FileChooser;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.UIHandler;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class X5WebView extends WebView {

	private Activity context;

	public static final int REQUEST_CODE_Choose_FILE = 101;

	private ValueCallback<Uri> valueCallback;

	private String sourceFilePath;

	private static boolean DEBUG = false;

	private WebChromeClient mWebViewClient;

	private FileChooser fileChooser;

	public X5WebView(Context arg0) {
		super(arg0);
		setBackgroundColor(85621);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(Context context, AttributeSet arg1) {
		super(context, arg1);
		initWebViewSettings();
		this.getView().setClickable(true);
		String x5UserAgent = "";
		if(isX5LoadSuccess) {
			x5UserAgent = " X5/" + X5WebView.getTbsCoreVersion(context);
		}
		this.getSettings().setUserAgent(this.getSettings().getUserAgentString() + x5UserAgent);
		super.setWebChromeClient(webChromeClient);
		super.setWebViewClient(webViewClient);
		setJSCallback();
		fileChooser = new FileChooser((Activity) context, this);
	}

	private WebViewClient webViewClient = new WebViewClient() {

		@Override
		public void onLoadResource(WebView webView, String url) {
			super.onLoadResource(webView, url);
		}

		@Override
		public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
			super.onPageStarted(webView, url, bitmap);
		}

		@Override
		public void onPageFinished(WebView webView, String url) {
			super.onPageFinished(webView, url);
			if(url.indexOf("about:blank") == 0) return;
		}

		@Override
		public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
			super.onReceivedSslError(webView, sslErrorHandler, sslError);
			sslErrorHandler.proceed();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView webView, String url) {
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
		public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> valueCallback, String s, String s1) {
//			X5WebView.this.valueCallback = valueCallback;
//			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//			i.addCategory(Intent.CATEGORY_OPENABLE);
//			i.setType("*/*");
//			context.startActivityForResult(Intent.createChooser(i, "File Chooser"), REQUEST_CODE_Choose_FILE);
			fileChooser.choose();
		}

		@Override
		public void onProgressChanged(WebView webView, int progressInPercent) {
			super.onProgressChanged(webView, progressInPercent);
			Log.d("webview", "progressInPercent=" + progressInPercent);
			WebProgressChangedEvent event = new WebProgressChangedEvent();
			event.setNewProgress(progressInPercent);
			EventBusUtil.getInstance().post(event);
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

	public void setJSCallback() {
		addJavascriptInterface(new JSCallbackObject(context, this), "jsApiBridge");
	}

	public void setJSCallback(String jsObjName) {
		addJavascriptInterface(new JSCallbackObject(context, this), jsObjName);
	}

	/** 初始化设置 */
	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(true);
		// webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

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

}
