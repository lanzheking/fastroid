package com.honestwalker.android.APICore.API;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honestwalker.android.APICore.API.net.Parameter;
import com.honestwalker.android.APICore.API.resp.BaseResp;
import com.honestwalker.android.APICore.IO.API;
import com.honestwalker.android.spring.core.annotation.RunInMainThread;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.net.HttpRequest;
import com.honestwalker.androidutils.pool.ThreadPool;

import org.apache.http.NameValuePair;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lanzhe on 18-3-21.
 */
public abstract class APISupport {

    protected static Context context;
    protected static String apiHost;

    public static void init(Context context, String apiHost) {
        APISupport.context = context;
        APISupport.apiHost = apiHost;
    }

    /**
     * 从注解中读取响应请求信息
     * @param req 请求对象
     * @return req对象的API注解
     * @throws ApiException
     */
    private API readRequestAnnotation(Object req) throws ApiException {
        Class<?> target = req.getClass();
        API api = target.getAnnotation(API.class);
        if (api == null) throw new ApiException("req 未配置");
        return api;
    }

    protected <T extends BaseResp> void request(
            final Object req,
            final APIListener<T> listener ,
            final Class<T> respClass) {
        request(req, null, listener, respClass);
    }

    protected <T extends BaseResp> void request(
            final Object req,
            final Map<String, String> files,
            final APIListener<T> listener ,
            final Class<T> respClass) {
        onStart(listener);
        try {
            try {
                if(context == null) {
                    throw new ApiException("请先在Application中调用 APISupport.init()");
                }
                checkClientConnect();
            } catch (Exception e) {
                ExceptionUtil.showException(e);
                throw new ApiException("网络异常");
            }
            // 获取 api注解
            final API api = readRequestAnnotation(req);

            ThreadPool.threadPool(new Runnable() {
                @Override
                public void run() {
                    // 发送请求
                    try {
                        String result = doRequest(req, api, files);
                        Gson gson = new Gson();
                        T resp = gson.fromJson(result, respClass);
                        resp.setJson(result);
                        onComplete(listener, resp);
                    } catch (ApiException e) {
                        onFail(listener, e);
                    }
                }
            });

        } catch (ApiException e) {
            ExceptionUtil.showException(e);
            onFail(listener, e);
        }
    }

    @RunInMainThread
    private <T> void onComplete(final APIListener<T> listener, final T result) {
        if(listener == null) return;
        listener.onComplete(result);
    }

    @RunInMainThread
    private <T> void onFail(final APIListener<T> listener, final ApiException e) {
        if(listener == null) return;
        listener.onFail(e);
    }

    @RunInMainThread
    private void onStart(final APIListener listener) {
        if(listener == null) return;
        listener.onStart();
    }

    /**
     * 发起请求，获取api返回值json
     * @param api          req对象的api注解对象
     * @param files        上传的文件
     * @return 请求结果字符串数据
     * @throws IOException
     * @throws InvalidKeyException
     */
    private String doRequest(Object req , API api , Map<String, String> files) throws ApiException {
        if(api == null) {
            throw new ApiException("Api Req参数缺少注解@API");
        }
        HttpRequest httpRequest = new HttpRequest();
        HttpMethod httpMethod = api.requestMethod();
        String requestUrl = apiHost;
        // req 可以host覆盖
        if (!StringUtil.isEmptyOrNull(api.host()))  requestUrl = api.host();
        // 如果请求地址有uri ， 拼接上uri地址
        if (!StringUtil.isEmptyOrNull(api.uri()))   requestUrl = requestUrl + api.uri();
        RequestParams requestParams = new RequestParams(requestUrl);
        LogCat.d("REQUEST", "requestUrl=" + requestUrl);

        // 填充method参数,如果有
        if(!StringUtil.isEmptyOrNull(api.action_key())) {
            requestParams.addQueryStringParameter(api.action_key(), api.value());
        }
        LogCat.d("REQUEST", api.action_key() + "=" + api.value());

        // 填充API参数
        Parameter parameters = getParameters(req);
        for (NameValuePair nameValuePair : parameters.getParameterList()) {
            requestParams.addQueryStringParameter(nameValuePair.getName(), nameValuePair.getValue());
        }

        // 文件上传
        if(files != null) {
            for (String key : files.keySet()) {
                requestParams.addParameter(key, new File(files.get(key)));
            }
        }

        LogCat.d("REQUEST", "requestParams=" + requestParams.toString());

        // 发起请求
        try {
            String response = httpRequest.request(httpMethod, requestParams);
            LogCat.d("REQUEST", "response=" + response);
            return response;
        } catch (Throwable throwable) {
            ApiException apiException = new ApiException();
            apiException.setStackTrace(throwable.getStackTrace());
            throw  apiException;
        }

    }

    /** request对象转parameter */
    private Parameter getParameters(Object req) {
        Parameter params = new Parameter();
        Field[] fields = req.getClass().getFields();
        for(Field field : fields) {
            try {
                if(field.getType().equals(HashMap.class)) {   // 如果是自定义参数类型
                    HashMap<String, String> kvs = (HashMap<String, String>) field.get(req);
                    Iterator<Map.Entry<String, String>> iter = kvs.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, String> ent = iter.next();
                        params.put(ent.getKey(), ent.getValue());
                    }
                } else {
                    if(field.get(req) != null && !StringUtil.isEmptyOrNull(field.get(req) + "")) {
                        SerializedName anno = field.getAnnotation(SerializedName.class);
                        if(anno != null) {
                            params.put(anno.value() , field.get(req));
                        } else {
                            params.put(field.getName(), field.get(req));
                        }
                    }
                }
            } catch (Exception e) {
                ExceptionUtil.showException(e);
            }
        }
        return params;
    }

    /**
     * 检测连接状态
     * @throws Exception
     */
    private void checkClientConnect() throws Exception {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            throw new Exception("NetWork Error");
        }
    }

}
