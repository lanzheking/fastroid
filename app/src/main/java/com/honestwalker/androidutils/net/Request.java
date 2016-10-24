package com.honestwalker.androidutils.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.honestwalker.androidutils.Application;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.TimeUtil;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.ObjectStreamIO;
import com.honestwalker.androidutils.IO.SharedPreferencesLoader;
import com.honestwalker.androidutils.exception.ExceptionUtil;

/**
 * @author Lan zhe
 */
public class Request {
	
	private Context context;
	
	private final static String TAG = "REQUEST";
	
	private CookieStoreSerializable cookieStoreSerializable;
	
	public Request(Context context) {
		this.context = context;
	}
	
	/*
	 * ================================ 
	 *  		* get 和 post 请求 * *
	 * ===============================
	 */

	public Response doPost(String url, RequestParameter parameters) {


		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(parameters.getParameterList());
			httpPost.setEntity(entity);
			try {
				String agent = SharedPreferencesLoader.getInstance(context).getString("User-Agent" , "");
//				httpPost.setHeader("User-Agent", agent);
				HttpResponse response = new DefaultHttpClient()
						.execute(httpPost);
				String responseStr = EntityUtils.toString(response.getEntity());
				return new Response(url,parameters,responseStr);
			} catch (Exception e) {
				ExceptionUtil.showException(e);
			}
		} catch (UnsupportedEncodingException e1) {
			ExceptionUtil.showException(e1);
		}
		return new Response(url,parameters,"");
	}
	
	public String doPost(String actionUrl, Parameter parameters, Map<String, File> files) {
		String result = null;
		StringBuffer log = new StringBuffer("");
		
		try {

			String responseStr = "";

			String BOUNDARY = java.util.UUID.randomUUID().toString();
			String PREFIX = "--", LINEND = "\r\n";
			String MULTIPART_FROM_DATA = "multipart/form-data";
			String CHARSET = "UTF-8";

			URL uri = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
			conn.setConnectTimeout(30000);  
			conn.setReadTimeout(30 * 1000); // 缓存的最长时间
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", "UTF-8");
//			httpGet.setHeader("Cookie", SharedPreferencesLoader.get("cookie"));
			conn.setRequestProperty("Cookie", SharedPreferencesLoader.getInstance(context).getString("cookie" , ""));
			
			// Agent增加版本号
			String userAgent = conn.getRequestProperty("User-Agent");
			LogCat.d("REQUEST", "userAgent=" + userAgent);
			String agent = SharedPreferencesLoader.getInstance(context).getString("User-Agent","");
			conn.setRequestProperty("User-Agent", userAgent + "; " + agent);
			
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" + BOUNDARY);
//			conn.setRequestProperty("User-Agent", "Dr.Wine " + Application.getAppVersion(context));

			Map<String, String> params = new HashMap<String, String>();

			// 登录成功后会得到sessionKey , 这个时候应该把sessionKey也传过服务器

			log.append(params.toString());
			
			LogCat.d("REQUEST",log.toString());
			
			// test
//			params.put("image", "logo");
//			params.put("new_store", "true");
			for(NameValuePair nvp : parameters.getParameterList()) {
				params.put(nvp.getName() , nvp.getValue());
			}
			
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET
						+ LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}

			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			outStream.write(sb.toString().getBytes());
			InputStream in = null;
			// 发送文件数据
			if (files != null) {
				int index = 0;
				for (Map.Entry<String, File> file : files.entrySet()) {
					index++;
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					// name是post中传参的键 filename是文件的名称
//					sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\""
//							+ "file" + "\"" + LINEND);
					Random random1 = new Random(4);
					sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\""
//							+ file.getKey() + "\"" + LINEND);
							+ file.getValue().getParentFile().getPath() + "/" + (int)(Math.random()*10000) + ".png" + "\"" + LINEND);
					LogCat.d("REQUEST", "上传图片：" + file.getValue().getPath());
					sb1.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINEND);
					sb1.append(LINEND);
					outStream.write(sb1.toString().getBytes());

					InputStream is = new FileInputStream(file.getValue());
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}

					is.close();
					outStream.write(LINEND.getBytes());
				}
			}
			LogCat.d("REQUEST",parameters.toString());
				// 请求结束标志
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND)
						.getBytes();
				outStream.write(end_data);
				outStream.flush();
				// 得到响应码
				int res = conn.getResponseCode();
				if (res == 200) {
					in = conn.getInputStream();
					int ch;
					StringBuilder sb2 = new StringBuilder();
					while ((ch = in.read()) != -1) {
						sb2.append((char) ch);
					}
					responseStr = sb2.toString();
				}
				outStream.close();
				conn.disconnect();

			LogCat.d("REQUEST", "----");
			LogCat.d("REQUEST", responseStr);
			LogCat.d("REQUEST", "----");
			
			// 记录请求日志 ， 可注释
			try {
				String responseTime = new TimeUtil().getNow();
				
				log.append("\r\n\r\n==================response @ "
						+ responseTime + " end; "
						+ "===============\r\n");
				log.append("Response:" + responseStr + "\r\n\r\n");
				log.append("========================================="
						+ "\r\n\r\n\r\n");
				LogCat.d("REQUEST", log.toString());
			} catch (Exception ex) {
				ExceptionUtil.showException(ex);
			}

			result = responseStr;

		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}

		return result;

	}

	/**
	 * 向指定url发送请求
	 */
	public Response doGet(String url) {
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = new DefaultHttpClient().execute(httpGet);
			return new Response(url,null,EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			ExceptionUtil.showException(e);
		} catch (IOException e) {
			ExceptionUtil.showException(e);
		}
		return new Response(url,null,"");
	}
	
	/**
	 * 向指定url发送请求，和参数
	 * 
	 * @param url
	 * @param parameters
	 * @return
	 */
	public Response doGet(String url, RequestParameter parameters) {
		try {
			url += parameters.toString();
			
		} catch (Exception e1) {
			ExceptionUtil.showException(e1);
			return new Response(url, parameters, "");
		}
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		// 重试设置
		DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
		retryhandler.setRetryCount(3);
		retryhandler.setRequestSentRetryEnabled(true);
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryhandler);
		// 超时设置
		httpClient.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20000);
		
		try{
			if(cookieStoreSerializable == null ) {
				if(ObjectStreamIO.existsObjectStream(context.getCacheDir().toString(), "cookie")) {
					cookieStoreSerializable = (CookieStoreSerializable) 
							ObjectStreamIO.input(context.getCacheDir().toString(), "cookie");
				}
			}
		}catch (Exception e) {}
		
		if(cookieStoreSerializable != null) {
//			CookieStore cookieStore = createCookieStore(cookieStoreSerializable.getCookies());
//			showCookie(cookieStore);
//			httpClient.setCookieStore(cookieStore);
		}
		
		LogCat.d(TAG, "[GET]\r\n" + url);
		HttpGet httpGet = new HttpGet(url);
		
//	    BasicHttpParams params = new BasicHttpParams();
//		parameters.convert(params);
//		httpGet.setParams(params);
		
		httpGet.setHeader("Cookie", SharedPreferencesLoader.getInstance(context).getString("cookie" , ""));
		
		
		// Agent增加版本号
//		String userAgent = httpGet.geth.getRequestProperty("User-Agent");
//		LogCat.d("REQUEST", "userAgent=" + userAgent);
		String agent = SharedPreferencesLoader.getInstance(context).getString("User-Agent" , "");
		httpGet.setHeader("User-Agent", agent);
		
		LogCat.d(TAG, "[Cookie]=" + SharedPreferencesLoader.getInstance(context).getString("cookie" , ""));
		
		try {
//			LogCat.d(TAG, "[参数]\r\n" + params.getParameter("json"));
			HttpResponse response = httpClient.execute(httpGet);
			
			CookieStore cookieStore = httpClient.getCookieStore();
			showCookie(cookieStore);
			List<Cookie> cookies = cookieStore.getCookies();
			if(cookies != null) {
				if(cookieStoreSerializable == null) {
					cookieStoreSerializable = new CookieStoreSerializable();
				}
				for(Cookie cookie : cookies) {
					CookieSerializable cookieSerializable = new CookieSerializable(cookie.getName(), cookie.getValue());
					cookieSerializable.setComment(cookie.getComment());
					cookieSerializable.setDomain(cookie.getDomain());
					cookieSerializable.setExpiryDate(cookie.getExpiryDate());
					cookieSerializable.setPath(cookie.getPath());
					cookieSerializable.setVersion(cookie.getVersion());
					cookieStoreSerializable.add(cookieSerializable);
				}
			}
			ObjectStreamIO.output(context.getCacheDir().toString(), cookieStoreSerializable, "cookie");
			
			String responseStr = EntityUtils.toString(response.getEntity());
			return new Response(url, parameters, responseStr);
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
		return new Response(url, parameters, "");
	}
	
	public CookieStore createCookieStore(List<CookieSerializable> cookies) {
		
		BasicCookieStore cookieStore = new BasicCookieStore();
		String cookiesStr = SharedPreferencesLoader.getInstance(context).getString("cookie" , "");
		if(!StringUtil.isEmptyOrNull(cookiesStr)) {
			LogCat.d("COOKIES", "解析cookies=" + cookiesStr);
			String[] cookieKVs = cookiesStr.split(";");
			try {
				LogCat.d("COOKIES", "\r\n\r\n===========\r\n");
				for(String c : cookieKVs) {
					String[] kv = c.split("=");
					BasicClientCookie basicCookie = null;
					if(kv.length == 1) {
						LogCat.d("COOKIES", "kv[0]= 空\r\n");
						basicCookie = new BasicClientCookie(kv[0] , "");
					} else if(kv.length > 2){
						LogCat.d("COOKIES", kv[0] + "=" + kv[1] + "\r\n");
						basicCookie = new BasicClientCookie(kv[0] , kv[1]);
					}
					cookieStore.addCookie(basicCookie);
				}
				LogCat.d("COOKIES", "\r\n===========\r\n");
			} catch (Exception e) {
			}
			return cookieStore;
		} else {
			LogCat.d("COOKIES", "序列化读取cookie");
			for(CookieSerializable cookie : cookies) {
				BasicClientCookie basicCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
				basicCookie.setComment(cookie.getComment());
				basicCookie.setDomain(cookie.getDomain());
				basicCookie.setExpiryDate(cookie.getExpiryDate());
				basicCookie.setPath(cookie.getPath());
				basicCookie.setVersion(cookie.getVersion());
				cookieStore.addCookie(basicCookie);
			}
		}
		return cookieStore;
		
	}
	
	public void showCookie(CookieStore cookieStore) {
		if(cookieStore == null) return;
		List<org.apache.http.cookie.Cookie> cookieList = cookieStore.getCookies();
		if(cookieList == null)  return;
		LogCat.d("COOKIES" , "============= cookie ===========V");
		for(org.apache.http.cookie.Cookie cookie : cookieList) {
			LogCat.d("COOKIES" , cookie.getName() + "=" + cookie.getValue() + ";");
		}
		LogCat.d("COOKIES" , "============= cookie ===========^");
	}

}
