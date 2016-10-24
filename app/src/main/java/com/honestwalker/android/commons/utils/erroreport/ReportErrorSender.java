package com.honestwalker.android.commons.utils.erroreport;

import android.content.Context;
import android.util.Log;

import com.honestwalker.androidutils.Application;
import com.honestwalker.androidutils.Base64;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.equipment.TelephoneUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.net.Parameter;
import com.honestwalker.androidutils.pool.ThreadPool;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;

public class ReportErrorSender {

	private final static String TAG = "REPORT";

	/// 错误报告相关

//	public static final String debugHost = "http://192.168.1.36:8080";
	public static final String debugHost = "http://47.88.148.212/log";
	public static final String bugServerUrl = debugHost + "/savebug.action";
	public static final String logServerUrl = debugHost + "/savelog.action";
	public static final String settingServerUrl = debugHost + "/setting.action";

	public static void send(final Context context, final ReportError reportError) {
		ThreadPool.threadPool(new Runnable() {
			@Override
			public void run() {
				final Parameter parameters = new Parameter();
				parameters.put("name", reportError.getName());
				parameters.put("client", reportError.getClient());
				parameters.put("remark", reportError.getRemark());
				parameters.put("version", reportError.getVersion());

				reportError.setLogContent(getLogContent(reportError.getException()));

				// log 1.0版发送
//    			parameters.put("logContent", reportError.getLogContent());
				// log 2.0版本发送方式
				parameters.put("encodeMethod", "BASE64");
				parameters.put("logContent", Base64.encode(reportError.getLogContent().getBytes()));

				LogCat.d(TAG, "-----------------");
				LogCat.d(TAG, "name=" + reportError.getName());
				LogCat.d(TAG, "client=" + reportError.getClient());
				LogCat.d(TAG, "remark=" + reportError.getRemark());
				LogCat.d(TAG, "version=" + reportError.getVersion());
				LogCat.d(TAG, "logContent=" + reportError.getLogContent());
				LogCat.d(TAG, "logContent base64=" + Base64.encode(reportError.getLogContent().getBytes()));
				LogCat.d(TAG, "-----------------");
				// log 2.0版本发送方式
//    			parameters.put("logContent", Base64.encode(reportError.getLogContent().getBytes()));
//    			parameters.put("encodeMethod", "BASE64");

				try {
					parameters.put("deviceId", TelephoneUtil.getInstance(context).getDeviceId());
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					LogCat.d(TAG, "准备发送请求");
					BasicHttpParams httpParams = new BasicHttpParams();
//					HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
//					HttpConnectionParams.setSoTimeout(httpParams, 30000);

					final DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
					final HttpPost httpPost = new HttpPost(bugServerUrl);
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters.getParameterList());
					httpPost.setEntity(entity);

					LogCat.d(TAG, "开始发送请求");
					HttpResponse response =  httpClient.execute(httpPost);
					String responseStr = EntityUtils.toString(response.getEntity());
					LogCat.d(TAG, "请求发送成功 responseStr"+responseStr);

					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						ExceptionUtil.showException(TAG, e);
					}
					Application.exit(context);

				} catch (Exception e1) {
					ExceptionUtil.showException(e1);
					Application.exit(context);
				}

			}

			;
		});
	}

	private static String getLogContent(Throwable ex) {
		final StringBuffer exceptionSB = new StringBuffer("");
		if (ex != null) {
			StackTraceElement[] stes = ex.getStackTrace();
			Log.e("AndroidRuntime", ex.toString());
			exceptionSB.append(ex.toString() + "<br />");
			for (StackTraceElement ste : stes) {
				Log.e("AndroidRuntime", ste.toString());
				exceptionSB.append(ste.toString() + "<br />");
			}
			if(ex.getCause() != null) {
				exceptionSB.append("<================Case==========><br />");
				StackTraceElement[] stesCause = ex.getCause().getStackTrace();
				exceptionSB.append(ex.getCause() + "<br />");
				if(stesCause != null) {
					for (StackTraceElement ste : stesCause) {
						Log.e("AndroidRuntime", ste.toString());
						exceptionSB.append(ste.toString() + "<br />");
					}
				}
			}
		}
		return exceptionSB.toString();
	}
}
