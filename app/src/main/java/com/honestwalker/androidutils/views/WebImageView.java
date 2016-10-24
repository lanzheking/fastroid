package com.honestwalker.androidutils.views;


import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.honestwalker.androidutils.pool.ThreadPool;

/**
 * 利用WebView 显示图片
 * WebView 可以显示清晰大图而不会内存溢出，弥补imageview的去点
 * WebImageView是自定义控件，显示效果和ImageView一样
 * 注意，不要使用OnClick时间，应该定义一个OnClick的Handler，用setOnClickHandler设置
 * 
 * 
 * 
 *
 * @author Administrator
 *
 */
public class WebImageView extends WebView {

	private Context context;
	
	private Handler onClickHandler = null;
	private Message	onClickHandlerMessage;
	private WebSettings webSettings;
	
	private int imageWidth = 0;
	private int height = 0;
	
	private WebImageLoadListener webImageLoadListener;
	
	private boolean useCache = false;
	
	private String charset = HTTP.UTF_8;
	private String htmlBase = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset={htmlCharset}\"></head>" +
			"<body style='margin:0px'>{htmlBody}</body></html>";
	
	private int screenWidth  = 0;
	private int screenHeight = 0;
	
	public WebImageView(Context context) {
		super(context);
		
		init();
	}
	
	@SuppressLint("NewApi")
	public WebImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		init();
	}
	
	private void init() {
		screenWidth  = DisplayUtil.getWidth(context) ;
		screenHeight = DisplayUtil.getHeight(context) ;
		
		webSettings = this.getSettings();
		webSettings.setJavaScriptEnabled(false);
		webSettings.setSupportZoom(false);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		
		this.setBackgroundColor(0);
		this.setWebViewClient(webViewClient);
		this.setOnTouchListener(onTouchListener);
//		this.setScrollContainer(false);
//		this.setScrollbarFadingEnabled(false);
	}
	
	/**
	 * 设置是否允许手势放大缩小，默认不允许
	 * @param supportZoon
	 */
	public void setSupportZoom(boolean supportZoon) {
		webSettings.setSupportZoom(supportZoon);
	}
	
	/**
	 * 设置是否显示放大缩小按钮，默认不显示
	 * @param showZoomControls
	 */
	public void setBuiltInZoomControls(boolean showZoomControls) {
		webSettings.setBuiltInZoomControls(showZoomControls);
	}
	
	/**
	 * 设置点击控件触发的事件
	 * @param onClickHandler 单击触发的事件
	 * @param onClickHandlerMessage 发送给onClickHandler的Message 可以为null
	 */
	public void setOnClickHandler(Handler onClickHandler,Message onClickHandlerMessage) {
		this.onClickHandler = onClickHandler;
		this.onClickHandlerMessage = onClickHandlerMessage;
	}
	
	/**
	 * 处理单击事件的onTouchEvent
	 */
	private OnTouchListener onTouchListener = new OnTouchListener() {
		private long action_down_time = 0;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN : {
					action_down_time = System.currentTimeMillis();
				} break;
				case MotionEvent.ACTION_UP : {
					long action_up_time = System.currentTimeMillis();
					if(action_up_time - action_down_time < 400) {
						if(onClickHandler != null) {
							if(onClickHandlerMessage != null) {
								Message msg = new Message();		// 每次点击必须拷贝一个新的message 否则报错message已经被占用
								msg.copyFrom(onClickHandlerMessage);
								onClickHandler.sendMessage(msg);
							} else {
								onClickHandler.sendEmptyMessage(0);
							}
						}
					}
				} break;
			}
			
			return false;
		}
	};
	
	public void setImage(final String imgUrl) {
		
		if(imageWidth <= 0) {
			imageWidth = DisplayUtil.getWidth(context) ;
		}
		
		if(useCache) {
			
			ThreadPool.threadPool(new Runnable() {
				
				@Override
				public void run() {
					
					LogCat.d("home_image","222222222222222");
					String imgCode = htmlBase.replace("{htmlCharset}", charset).replace("{htmlBody}", "<img src='" + imgUrl + "' width='" + screenWidth + "' />");
					LogCat.d("home_image","33333 imgCode=" + imgCode);
					Message msg = new Message() ;
					msg.obj = imgCode;
					msg.what = 1;
					loadDataWithBaseURLHandler.sendMessage(msg);

				}
			});
			
		} else {
			String imgCode = "";
			LogCat.d("home_image","缓存不存在图片");
			try {
				imgCode = htmlBase.replace("{htmlCharset}", charset).replace("{htmlBody}", "<img src='" + imgUrl + "' width='" + screenWidth + "' />");
				Message msg = new Message() ;
				msg.obj = imgCode;
				msg.what = 1;
				loadDataWithBaseURLHandler.sendMessage(msg);
			} catch (Exception e) {
			}
		}
		this.reload();
	}
	
	/**
	 * what = 0 loadDataWithBaseURL方式加载，适合带有本地图片   what = 1 loadData方式加载，适合网络加载
	 */
	private Handler loadDataWithBaseURLHandler = new Handler() {
		public void handleMessage(Message msg) {
			Object imgCode = msg.obj;
			if(imgCode != null) {
				if(msg.what == 0) {
					loadDataWithBaseURL(null, imgCode.toString(), "text/html;charset=" + charset, charset, null);
				} else if(msg.what == 1) {
					loadData(imgCode.toString(), "text/html;charset=" + charset, charset);
				}
			}
		};
	};
	
	private WebViewClient webViewClient = new WebViewClient() {
		public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
			if(webImageLoadListener != null) {
				webImageLoadListener.onLoad(url);
			}
		};
		public void onPageFinished(WebView view, String url) {
			if(webImageLoadListener != null) {
				webImageLoadListener.onLoaded(url);
			}
		};
	};
	
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}
	
	public WebImageLoadListener getWebImageLoadListener() {
		return webImageLoadListener;
	}

	public void setWebImageLoadListener(WebImageLoadListener webImageLoadListener) {
		this.webImageLoadListener = webImageLoadListener;
	}

	public interface WebImageLoadListener {
		public void onLoad(String url);
		public void onLoaded(String url);
	}

	public void setImageWidth(int width) {
		this.imageWidth = width;
	}
	
	public void setImageHeight(int height) {
		this.height = height;
	}
	
}

