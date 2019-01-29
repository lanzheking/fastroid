package com.honestwalker.androidutils.net;


import android.content.Context;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.ObjectStreamIO;
import com.honestwalker.androidutils.IO.SharedPreferencesLoader;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hkyy on 16-10-14.
 */
public class XutilsHttpsUtil {

    private final String cookieFileName = "COOKIE";

    public void upload(Context context,String url,String name,String path, Callback.CommonCallback<XutilsHttpsResponseEntity> cacheCallback){
        RequestParams params = new RequestParams(url);
        params.setMultipart(true);
        params.addParameter(name, new File(path));
        LogCat.d("REQUEST", "");
        params.addHeader("Cookie", getCookie(context));
        String userAgent = SharedPreferencesLoader.getInstance(context).getString("User-Agent" , "");
        params.addHeader("User-Agent", userAgent);
        LogCat.d("REQUEST", "上传文件:" + url + "  cookie=" + getCookie(context));
        LogCat.d("REQUEST", "参数    :" + "  filekey=" + name);
        Callback.Cancelable callback =  x.http().post(params,cacheCallback);
    }

    public String getCookie(Context context) {
        return SharedPreferencesLoader.getInstance(context).getString("cookie" , "");
    }

//    public synchronized String getCookie(Context context) {
//        try {
//            HashMap<String, String> cookieMap = (HashMap<String, String>) ObjectStreamIO.input(context.getFilesDir().getAbsolutePath(), cookieFileName);
//            if(cookieMap == null) {
//                return null;
//            }
////			CookieStore cookieStore = httpClient.getCookieStore();
////			cookieStore.clear();
//
//            Iterator<Map.Entry<String, String>> iter = cookieMap.entrySet().iterator();
//            StringBuffer cookieStr = new StringBuffer();
//            while(iter.hasNext()) {
//                Map.Entry<String,String> ent = iter.next();
//                cookieStr.append(ent.getKey() + "=" + ent.getValue() + ";");
//            }
//
//            LogCat.d("REQUEST", "\r\n[SET COOKIE]" + cookieStr.toString());
//
//            return cookieStr.toString();
//        } catch (Exception e) {
//        }
//        return "";
//    }

}
