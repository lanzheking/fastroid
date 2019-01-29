package com.honestwalker.androidutils.ImageSelector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.Application;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.ImageSelector.crop.CropView;
import com.honestwalker.androidutils.ImageSelector.crop.GrallyAndPhotoUtils;
import com.honestwalker.androidutils.ImageUtil;
import com.honestwalker.androidutils.system.ProcessUtil;
import com.honestwalker.androidutils.ui.Size;
import com.honestwalker.androidutils.views.loading.Loading;

import java.io.InputStream;

/**
 * Created by lanzhe on 17-7-19.
 */

public class CropBitmapActivity extends Activity implements View.OnClickListener {

    private Button mBtnLeftRotate;
    private Button mBtnRightRotate;
    private Button mBtnConfirm;
    private Button mBtnCancel;
    private CropView mCropView;

    Handler mHandler = null;
//    ProgressDialog mDialog = null;
    Bitmap mPhoto = null;
    String mFilePath = null;
    String mOutputPath = null;

    private int maxWidth;

    private int bmpQuality = 100;

    private final static String TAG = "ImageSelector";

    /**
     * 启动此Activity
     *
     * @param act
     * @param srcBitmapPath 来源图片的路径
     * @param outputPath    裁剪后输出的图片路径
     * @param degree        图片旋转了的角度
     */
    public static void startThisActivitySelf(Activity act, String srcBitmapPath, String outputPath, int maxWidth, int bmpQuality, int degree) {
        Intent intent = new Intent(act, CropBitmapActivity.class);
        intent.putExtra("inputPath", srcBitmapPath);
        intent.putExtra("outputPath", outputPath);
        intent.putExtra("degree", degree);
        intent.putExtra("bmpQuality", bmpQuality);
        intent.putExtra("maxWidth", maxWidth);
        LogCat.d(TAG, "原图：" + srcBitmapPath + "  剪切到:" + outputPath);
        act.startActivityForResult(intent, ImageSelector.REQUEST_IMAGE_CUT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transform_bitmap);
        mBtnLeftRotate = (Button) findViewById(R.id.transform_left_rotate_btn);
        mBtnRightRotate = (Button) findViewById(R.id.transform_right_rotate_btn);
        mBtnConfirm = (Button) findViewById(R.id.transform_confirm_btn);
        mBtnCancel = (Button) findViewById(R.id.transform_cancel_btn);
        mCropView = (CropView) findViewById(R.id.transform_bitmap_cv);

        mBtnLeftRotate.setOnClickListener(this);
        mBtnRightRotate.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        this.bmpQuality = getIntent().getIntExtra("degree", 100);
        //输入地址
        this.mFilePath = getIntent().getStringExtra("inputPath");
        //输出地址
        this.mOutputPath = getIntent().getStringExtra("outputPath");
        int degree = getIntent().getIntExtra("degree", 0);
        this.maxWidth = getIntent().getIntExtra("maxWidth", 0);
        InputStream in = null;
        //不存在源图片路径时,加载默认的示例图片资源
        if (mFilePath == null) {
//            mPhoto = GrallyAndPhotoUtils.decodeBitmapInScale(getResources(), R.raw.pkq, 720);
            setResult(RESULT_CANCELED);
            finish();
        } else {
            Size size = GrallyAndPhotoUtils.getImageSize(mFilePath);
            LogCat.d(TAG, "待剪切图片尺寸 " + size.getWidth() + " x " + size.getHeight());
            mPhoto = GrallyAndPhotoUtils.decodeBitmapInScale(mFilePath, (int)size.getWidth());
        }

        //存在旋转角度,对图片进行旋转
        if (degree != 0) {
            //旋转图片
            Bitmap originalBitmap = GrallyAndPhotoUtils.rotatingBitmap(degree, mPhoto);
            //回收旧图片
            mPhoto.recycle();
            mPhoto = originalBitmap;
        }
        mCropView.setImageBitmap(mPhoto);

//        mDialog = new ProgressDialog(this);
//        mDialog.setTitle("正在处理图片...");
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x1: {
                        Loading.showCancelunable(CropBitmapActivity.this, "剪切中....");
                    } break;
                    case 0x2: {
                        Loading.dismiss(CropBitmapActivity.this);
                        Intent intent = new Intent();
                        intent.putExtra("imgPath", mOutputPath);
                        setResult(RESULT_OK, intent);
                        finish();
                    } break;
                    case 0x3: {
                        Loading.dismiss(CropBitmapActivity.this);
                        setResult(RESULT_CANCELED);
                        finish();
                    } break;
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPhoto != null && !mPhoto.isRecycled()) {
            mPhoto.recycle();
            mPhoto = null;
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.transform_confirm_btn) {
            //显示加载对话框
            mHandler.sendEmptyMessage(0x1);
            new Thread() {
                @Override
                public void run() {
                    if (mCropView.restoreBitmap(mOutputPath, Bitmap.CompressFormat.PNG, true, bmpQuality)) {
                        zipImage();
                        setResult(RESULT_OK);
                        mPhoto.recycle();
                        mPhoto = null;
                        Message msg = Message.obtain();
                        msg.what = 0x2;
                        mHandler.sendMessage(msg);
                    } else {
                        //仅取消对话框
                        mHandler.sendEmptyMessage(0x3);
                    }
                }
            }.start();
        } else if(v.getId() ==  R.id.transform_cancel_btn)  {
            //取消时需要回收图片资源
            mCropView.recycleBitmap();
            setResult(RESULT_CANCELED);
            finish();
            Application.exit(getApplicationContext());
        }

    }

    private void zipImage() {
        ImageUtil imageUtl = new ImageUtil();
        imageUtl.imageZip(mOutputPath, maxWidth, 100);
    }

}
