package com.honestwalker.androidutils.ImageSelector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.ImageSelector.ImageScan.MultiImageSelectorActivity;

import java.io.File;

/**
 * Created by lanzhe on 16-9-27.
 */
class MediaOprator {

    private final String TAG = "ImageSelector";

    private ImageView previewIV;

    private Uri cameraOutputPath;
    private String outputPath = "";

    private ImageSelectType type;

    private boolean signleSelect = false;
    private int maxSelect = 9;

    private Uri imgCutUri;

    private int maxWidthPx = 0;

    private int aspectX;
    private int aspectY;

    private boolean needCut;

    private static String processName;

    private Activity context;

    MediaOprator(Activity context) {
        this.context = context;
    }

    private void keepIntent(Intent intent) {
        intent.putExtra("maxWidth" , maxWidthPx);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
    }

    /**
     * 打开摄像头开始拍照
     */
    public void openCamera(String outputPath, int maxWidthPx,int aspectX, int aspectY) {

        this.maxWidthPx = maxWidthPx;
        this.aspectX    = aspectX;
        this.aspectY    = aspectY;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, Configuration.ORIENTATION_PORTRAIT);

        try {
            cameraOutputPath = Uri.fromFile(new File(outputPath));
        } catch (Exception e) {}

        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutputPath);
//		intent.putExtra("android.intent.extra.screenOrientation", false);
        keepIntent(intent);

        context.startActivityForResult(intent, ImageSelector.REQUEST_CAMERA);

    }

    /** 打开图片剪切工具 */
    public void toImageCut(Uri resultUri, String outputPath, int maxWidthPx,int aspectX, int aspectY) {

        this.outputPath = outputPath;
        this.maxWidthPx = maxWidthPx;
        this.aspectX    = aspectX;
        this.aspectY    = aspectY;

        Intent intentCut = new Intent("com.android.camera.action.CROP");
        //需要裁减的图片格式

        intentCut.setDataAndType(resultUri, "image/*");

        //允许裁减
        intentCut.putExtra("crop", "true");
        if(maxWidthPx > 0) {
            //剪裁后ImageView显时图片的宽
            intentCut.putExtra("outputX", maxWidthPx);
            int maxHeightPx = maxWidthPx * aspectY / aspectX;
            //剪裁后ImageView显时图片的高
            intentCut.putExtra("outputY", maxHeightPx);
            LogCat.d(TAG , "剪切最大像素 " + maxWidthPx);
        }
        //设置剪裁框的宽高比例
        intentCut.putExtra("aspectX", aspectX);
        intentCut.putExtra("aspectY", aspectY);
        intentCut.putExtra("scale", true);
//		intentCut.putExtra(MediaStore.EXTRA_OUTPUT, outputPath);                // 剪切工具保存到这个uri
        intentCut.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputPath)));                // 剪切工具保存到这个uri
//		intentCut.putExtra("return-data", true);                    // 加这个 小米2 会报错
        intentCut.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());//返回格式
        Log.d(TAG, "剪切图片将要输出到:" + outputPath);

        keepIntent(intentCut);

        context.startActivityForResult(intentCut, ImageSelector.REQUEST_IMAGE_CUT);
    }

    /** 系统图片选择工具 */
    public void sysImageSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(
                Intent.createChooser(intent, "Select Picture"), ImageSelector.REQUEST_IMAGE_SELECT);
    }

    /** 单图片选择工具 */
    public void singleImageSelect() {
        Intent intent = new Intent(context,
                MultiImageSelectorActivity.class);
        intent.putExtra("signleSelect", signleSelect);
        intent.putExtra("maxSelect", maxSelect);
        context.startActivityForResult(intent, ImageSelector.REQUEST_SINGLE_IMAGE_SELECT);
    }
    /** 多图片选择工具 */
    public void multiImageSelect(int maxSelect) {
        Intent intent = new Intent(context,
                MultiImageSelectorActivity.class);
        intent.putExtra("signleSelect", false);
        intent.putExtra("maxSelect", maxSelect);
        context.startActivityForResult(intent, ImageSelector.REQUEST_MULTI_IMAGE_SELECT);
    }

}
