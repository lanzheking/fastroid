package com.honestwalker.android.x5engine;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.honestwalker.android.x5engine.views.X5WebView;
import com.honestwalker.androidutils.IO.LogCat;
import com.tencent.smtt.sdk.ValueCallback;

/**
 * Created by lanzhe on 17-6-22.
 */
public class FileChooser {

    private X5WebView webView;
    private Activity activity;
    private String acceptType;
    private String capture;
    private ValueCallback uploadFile;

    private static final String TAG = FileChooser.class.getSimpleName();

    public static final int WEB_FILE_CHOOSE_REQUEST_CODE = 100301;

    public FileChooser(Activity context, X5WebView webView) {
        this(context, webView, null, null);
    }

    public FileChooser(Activity context, X5WebView webView, String acceptType, String capture) {
        this.activity = context;
        this.uploadFile = webView.getValueCallback();
        this.acceptType = acceptType;
        this.capture = capture;
        this.webView = webView;
    }

    public void choose() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        activity.startActivityForResult(Intent.createChooser(i, "File Chooser"), WEB_FILE_CHOOSE_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == WEB_FILE_CHOOSE_REQUEST_CODE) {
            if(resultCode == activity.RESULT_OK) {
                if( uploadFile != null ) {
                    Uri uri = intent.getData();
                    uploadFile.onReceiveValue(uri);
                    LogCat.i(TAG, "uri = "+ uri);
                }
            } else {
                uploadFile.onReceiveValue(null);
            }
        }
    }

}
