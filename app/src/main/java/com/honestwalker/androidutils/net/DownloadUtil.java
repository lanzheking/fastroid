package com.honestwalker.androidutils.net;

import android.content.Context;

import com.honestwalker.android.APICore.API.net.cookie.ApiCookieManager;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by hkyy on 16-8-18.
 */
public class DownloadUtil {

    private DownloadStateListener stateListener;

    public void download(Context context, String downloadUrl, final String filePath){

        LogCat.d("download", "开始下载 " + downloadUrl);

        RequestParams params = new RequestParams(downloadUrl);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filePath);
        params.setUseCookie(true);

        params.addHeader("Cookie", ApiCookieManager.getLocalCookie(context));
        Callback.Cancelable cancelable = x.http().get(params, callback);

    }

    public interface DownloadStateListener{
        void onSuccess(String path);
        void onLoading(int i);
        void onFail(Throwable ex);
        void onCancelled(Callback.CancelledException cex);
        void onFinished();
    }


    public DownloadStateListener getStateListener() {
        return stateListener;
    }

    public void setStateListener(DownloadStateListener stateListener) {
        this.stateListener = stateListener;
    }

    Callback.CommonCallback<File> callback = new Callback.ProgressCallback<File>() {
        @Override
        public void onWaiting() {

        }

        @Override
        public void onStarted() {
            LogCat.i("download", "正在连接...");
        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            LogCat.i("download", current + "/" + total);
            if(stateListener != null){
                stateListener.onLoading((int)(100*current/total));
            }
        }

        @Override
        public void onSuccess(File result) {

            LogCat.i("download", "downloaded: success   " + result.getPath());
            //ToastHelper.alert(context,"下载完成，保存地址：" + responseInfo.result.getPath());
            if(stateListener != null){
                stateListener.onSuccess(result.getPath());
            }

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            if(stateListener != null){
                stateListener.onFail(ex);
            }

        }

        @Override
        public void onCancelled(CancelledException cex) {
            LogCat.i("download","cabcel");
        }

        @Override
        public void onFinished() {
            LogCat.i("download","finish");
        }
    };

}
