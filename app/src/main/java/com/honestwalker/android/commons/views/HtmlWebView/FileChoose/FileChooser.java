package com.honestwalker.android.commons.views.HtmlWebView.FileChoose;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;

import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;
import com.honestwalker.androidutils.IO.LogCat;

/**
 * Created by lanzhe on 17-6-22.
 */
public class FileChooser {

    private HtmlWebView webView;
    private Activity activity;
    private String acceptType;
    private String capture;
    private ValueCallback uploadFile;

    private static final String TAG = FileChooser.class.getSimpleName();

    public static final int WEB_FILE_CHOOSE_REQUEST_CODE = 100301;

    public FileChooser(Activity context, HtmlWebView htmlWebView) {
        this(context, htmlWebView, null, null);
    }

    public FileChooser(Activity context, HtmlWebView htmlWebView, String acceptType, String capture) {
        this.activity = context;
        this.uploadFile = htmlWebView.getmUploadMessage();
        this.acceptType = acceptType;
        this.capture = capture;
        this.webView = htmlWebView;
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
