package com.honestwalker.androidutils.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.honestwalker.android.commons.activity.BaseActivity;
import com.honestwalker.android.commons.utils.StartActivityHelper;
import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.spring.core.annotation.IntentInject;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.MD5;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.views.PhotoView.PhotoGifView;
import com.honestwalker.androidutils.views.PhotoView.PhotoView;
import com.honestwalker.androidutils.views.loading.Loading;
import com.honestwalker.androidutils.window.ToastHelper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class ImagePreviewActivity extends BaseActivity {

    private final String TAG = "ImagePreview";

    private View backBTN;

    private PhotoView photoView;
    private PhotoGifView photoGifView;

    @IntentInject
    private String imagePath;

    private Bitmap currentImageBitmap;

    /** 支持的文件类型 */
    private static final String[] supportFileTypes = new String[]{".gif", ".jpg", ".png", ".jpeg", ".js"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        backBTN      = findViewById(R.id.btn1);
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        photoView    = (PhotoView)    findViewById(R.id.imageview1);
        photoGifView = (PhotoGifView) findViewById(R.id.imageview2);

        LogCat.d(TAG, "imagePath=========" + imagePath);

        if(imagePath == null || (isLocalPath() && !new File(imagePath).exists()) || (!isLocalPath() && !isUrl()) ) {
            ToastHelper.alert(context, "图片不存在");
            finish();
            return;
        }
        if(StringUtil.isEmptyOrNull(getFileType(imagePath))) {
            ToastHelper.alert(context, "不支持的文件类型");
            finish();
            return;
        }

        showPhotoViewByFileType();

        if(isLocalPath()) {
            loadWithLocalPath(imagePath);
        } else {
            //  如果是网络图片先判断本地是否存在缓存
            String name = MD5.encrypt(imagePath);
            final String cachePath = getImageCachePath() + name + getFileType(imagePath);
            if(new File(cachePath).exists()) {
                loadWithLocalPath(cachePath);
            } else {

                LogCat.d(TAG, "开始下载 " + imagePath + " 下载到" + cachePath);
                Loading.showCancelunable(context);
                RequestParams params = new RequestParams(imagePath);
                params.setSaveFilePath(cachePath);
//                params.setSaveFilePath(getImageCachePath());
                params.setAutoRename(false);
                x.http().get(params, new Callback.CacheCallback<File>() {
                    @Override
                    public boolean onCache(File result) {
                        return false;
                    }

                    @Override
                    public void onSuccess(File result) {
                        LogCat.d(TAG, "下载 " + imagePath + "成功");
                        loadWithLocalPath(cachePath);
                        Loading.dismiss(context);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ToastHelper.alert(context, "图片加载失败");
                        Loading.dismiss(context);
                        ExceptionUtil.showException(TAG, ex);
                        finish();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });

            }

        }

    }

    private void showPhotoViewByFileType() {
        if(".gif".equals(getFileType(imagePath))) {
            photoGifView.setVisibility(View.VISIBLE);
            photoView.setVisibility(View.GONE);
        } else {
            photoGifView.setVisibility(View.GONE);
            photoView.setVisibility(View.VISIBLE);
        }
    }

    private void loadWithLocalPath(String localPath) {
        try {
            LogCat.d(TAG, "打开 " + localPath);
            if(".gif".equals(getFileType(localPath))) {
                GifDrawable gifFromPath = new GifDrawable(localPath);
                photoGifView.setImageDrawable(gifFromPath);
            } else {
                currentImageBitmap = BitmapFactory.decodeFile(localPath);
                photoView.setImageBitmap(currentImageBitmap);
            }
        } catch (IOException e) {
            ExceptionUtil.showException(TAG, e);
            ToastHelper.alert(context, "打开图片失败");
            finish();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    private boolean isLocalPath() {
        return imagePath.startsWith("/");
    }

    private boolean isUrl() {
        return imagePath.startsWith("http");
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(currentImageBitmap != null && !currentImageBitmap.isRecycled()) {
            try {
                photoView.setImageBitmap(null);
                currentImageBitmap.recycle();
                currentImageBitmap = null;
            } catch (Exception e) {}
        }
    }

    private String getFileType(String url) {
        try {
            for (String supportFileType : supportFileTypes) {
                String fileType = url.toLowerCase().substring(url.lastIndexOf("."));
                if(supportFileType.equals(fileType)) {
                    return fileType;
                }
            }
        } catch (Exception e) {}
        return "";
    }

    public static void startActivity(Activity context, String imagePath) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra("imagePath", imagePath);
        StartActivityHelper.toActivity(context, ImagePreviewActivity.class, intent, StartActivityHelper.ANIM_ENTER);
    }

}
