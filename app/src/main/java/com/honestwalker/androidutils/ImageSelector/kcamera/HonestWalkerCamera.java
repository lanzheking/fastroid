package com.honestwalker.androidutils.ImageSelector.kcamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.ImageSelector.kcamera.broadcast.PhotoPathSender;
import com.honestwalker.androidutils.ImageSelector.kcropper.CropImageView;
import com.honestwalker.androidutils.ImageUtil;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.honestwalker.androidutils.equipment.SDCardUtil;
import com.honestwalker.androidutils.pool.ThreadPool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by honestwalker on 15-9-29.
 */
public class HonestWalkerCamera extends Activity implements SurfaceHolder.Callback{

    private int screenWIDTH = 1440;
    private int screenHEIGHT = 2560;

    private int previewWIDTH;
    private int previewHEIGHT;

    private int pictureWIDTH;
    private int pictureHEIGHT;


    private SurfaceView surfaceView;

    private Camera camera; //这个是hardare的Camera对象
    private int shotOrientation = 0;
    private Button cameraBTN;

    private View btnLayout;

    private int cameraBTNSize;
    private int cameraBTNSizeSmall;

    private RelativeLayout previewLayout;
    private RelativeLayout reviewLayout;

    private CropImageView reviewImage;

    private Button reShotBTN;
    private Button useBTN;

    private int pictureQuality = 100;

    private boolean isReview = true;
    private boolean isCut = false;

    private int AspectRatioX = 0;
    private int AspectRatioY = 0;

    private int count = 0;

    private Bitmap bitmap ;
    private Bitmap rotat ;

    private Intent mIntent;
    private String photoPath;
    private HonestWalkerCamera context;
    private OrientationEventListener mAlbumOrientationEventListener;
    private int mOrientation = 0;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_kcamera);

        mAlbumOrientationEventListener = new AlbumOrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL);
        if (mAlbumOrientationEventListener.canDetectOrientation()) {
            mAlbumOrientationEventListener.enable();
        } else {
            Log.d("chengcj1", "Can't Detect Orientation");
        }

        context = this;
        UIHandler.init();
        mIntent = getIntent();
        if(mIntent != null){
            previewWIDTH = mIntent.getIntExtra("previewMAX_WIDTH",1080);
            previewHEIGHT = mIntent.getIntExtra("previewMAX_HEIGHT",1920);
            pictureWIDTH = mIntent.getIntExtra("pictureWIDTH",1920);
            pictureHEIGHT = mIntent.getIntExtra("pictureHEIGHT",1080);
            pictureQuality = mIntent.getIntExtra("pictureQuality",100);
            isReview = mIntent.getBooleanExtra("isReview", true);
            isCut = mIntent.getBooleanExtra("isCut",false);
            AspectRatioX = mIntent.getIntExtra("AspectRatioX",0);
            AspectRatioY = mIntent.getIntExtra("AspectRatioY",0);
        }

        previewLayout = (RelativeLayout) findViewById(R.id.layout1);
        reviewLayout =  (RelativeLayout) findViewById(R.id.layouy2);

        previewLayout.setVisibility(View.VISIBLE);
        reviewLayout.setVisibility(View.GONE);

        reviewImage = (CropImageView) findViewById(R.id.crop_image_view);


        if(AspectRatioX> 0 && AspectRatioY > 0){
            reviewImage.setFixedAspectRatio(true);
            reviewImage.setAspectRatio(AspectRatioX,AspectRatioY);
        }

        btnLayout = findViewById(R.id.btn_layout);
        cameraBTN = (Button) findViewById(R.id.button_capture);
        cameraBTN.setOnTouchListener(cameraTouchAnim);
        reShotBTN = (Button) findViewById(R.id.button1);
        useBTN = (Button) findViewById(R.id.button2);
        reShotBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewLayout.setVisibility(View.VISIBLE);
                surfaceView.setOnTouchListener(surfaceViewTouchListener);
                cameraBTN.setClickable(true);
                reviewImage.setImageBitmap(null);
                rotat.isRecycled();
                rotat = null;
                camera.startPreview();
                System.gc();
                reviewLayout.setVisibility(View.GONE);
                useBTN.setText("使用");
                useBTN.setOnClickListener(useBtnListener);

                reviewImage.setShowCut(false);
            }
        });

        useBTN.setOnClickListener(useBtnListener);

        screenWIDTH = DisplayUtil.getWidth(this);
        screenHEIGHT = DisplayUtil.getHeight(this);
        if (screenWIDTH > screenHEIGHT) {
            cameraBTNSize = (int) (screenHEIGHT * 0.2);
            cameraBTNSizeSmall = (int) (screenHEIGHT * 0.19);
        } else {
            cameraBTNSize = (int) (screenWIDTH * 0.2);
            cameraBTNSizeSmall = (int) (screenWIDTH * 0.19);
        }

        ViewSizeHelper.getInstance(this).setSize(cameraBTN, cameraBTNSize, cameraBTNSize);
        ViewSizeHelper.getInstance(this).setHeight(btnLayout, cameraBTNSize);

        cameraBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cameraBTN.setClickable(false);
                Log.i("time", System.currentTimeMillis() + count + "start");
//                activity_kcamera.takePicture(mShutterCallback, RawPictureCallback, null);
                camera.takePicture(mShutterCallback, null, mJpegPictureCallback);
                shotOrientation = mOrientation;
                surfaceView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }
        });

        surfaceView = (SurfaceView)this.findViewById(R.id.myCameraView);
        surfaceView.setFocusable(true);
        surfaceView.setFocusableInTouchMode(true);
        surfaceView.setClickable(true);
        surfaceView.setOnTouchListener(surfaceViewTouchListener);

        //SurfaceView中的getHolder方法可以获取到一个SurfaceHolder实例
        SurfaceHolder holder = surfaceView.getHolder();
        //为了实现照片预览功能，需要将SurfaceHolder的类型设置为PUSH
        //这样，画图缓存就由Camera类来管理，画图缓存是独立于Surface的
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this);
        //MemorySeek.show(this);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {}
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 当Surface被创建的时候，该方法被调用，可以在这里实例化Camera对象
        //同时可以对Camera进行定制
        camera = Camera.open(); //获取Camera实例

        /**
         * Camera对象中含有一个内部类Camera.Parameters.该类可以对Camera的特性进行定制
         * 在Parameters中设置完成后，需要调用Camera.setParameters()方法，相应的设置才会生效
         * 由于不同的设备，Camera的特性是不同的，所以在设置时，需要首先判断设备对应的特性，再加以设置
         * 比如在调用setEffects之前最好先调用getSupportedColorEffects。如果设备不支持颜色特性，那么该方法将
         * 返回一个null
         */
        try {

            Camera.Parameters param = camera.getParameters();
            param.setPictureFormat(PixelFormat.JPEG);
            param.setJpegQuality(pictureQuality);
            // param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦

            camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上


            if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
                //如果是竖屏
                param.set("orientation", "portrait");
                //在2.2以上可以使用
                camera.setDisplayOrientation(90);
            }else{
                param.set("orientation", "landscape");
                //在2.2以上可以使用
                camera.setDisplayOrientation(0);
            }
            //首先获取系统设备支持的所有颜色特效，有复合我们的，则设置；否则不设置
            List<String> colorEffects = param.getSupportedColorEffects();
            Iterator<String> colorItor = colorEffects.iterator();
            while(colorItor.hasNext()){
                String currColor = colorItor.next();
                if(currColor.equals(Camera.Parameters.EFFECT_NONE)){
                    param.setColorEffect(Camera.Parameters.EFFECT_NONE);
                    break;
                }
            }
            //设置完成需要再次调用setParameter方法才能生效
            camera.setParameters(param);

            camera.setPreviewDisplay(holder);


            List<Camera.Size> ps = param.getSupportedPictureSizes();        //照片size


            if(ps.size()>1){
                Camera.Size findPhotoSzie = binarysearchKey(ps, pictureWIDTH * pictureHEIGHT);

                if(findPhotoSzie != null){
                    param.setPictureSize( findPhotoSzie.width, findPhotoSzie.height);
                }
            }


            /**
             * 在显示了预览后，我们有时候希望限制预览的Size
             * 我们并不是自己指定一个SIze而是指定一个Size，然后
             * 获取系统支持的SIZE，然后选择一个比指定SIZE小且最接近所指定SIZE的一个
             * Camera.Size对象就是该SIZE。
             *
             */


            List<Camera.Size> previewSizeList = param.getSupportedPreviewSizes();   //预览size
            //如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
            if(previewSizeList.size() > 1){

                Camera.Size findPreviewSzie = binarysearchKey(previewSizeList, previewWIDTH * previewHEIGHT);
                if(findPreviewSzie != null) {
                    //这里改变了SIze后，我们还要告诉SurfaceView，否则，Surface将不会改变大小，进入Camera的图像将质量很差
//                    surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(screenHEIGHT, findSzie.height));
                    surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(screenWIDTH, screenHEIGHT));
                }

                // int x =  findSzie.height;
            }
            camera.setParameters(param);
        } catch (Exception e) {
            // 如果出现异常，则释放Camera对象
            LogCat.i("e", e.getCause() + " " + e.getMessage());
            camera.release();
        }

        //启动预览功能
        camera.startPreview();

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 当Surface被销毁的时候，该方法被调用
        //在这里需要释放Camera资源
        camera.stopPreview();
        camera.release();

    }



    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            LogCat.d("CAMERA", "对焦:" + arg0);
            // if(arg0) {
            // mCamera.cancelAutoFocus();
            // }
            if (arg0) {
                List<Camera.Area> as = arg1.getParameters().getFocusAreas();
                Iterator<Camera.Area> asiter = as.iterator();

                while (asiter.hasNext()){
                    Camera.Area area2 = asiter.next();
                    Rect r = area2.rect;
                    LogCat.i("CAMERA", "left = " + r.left + "top = " + r.top + " right = " + r.right + "bottom = " + r.bottom + "weight = " + area2.weight);
                }
            } else {
            }

        }
    };


    public void focusOnTouch(MotionEvent event) {
        float scale = (float)getResolution().width/screenHEIGHT;
        Rect focusRect = calculateTapArea(event.getRawY()*scale,(screenWIDTH - event.getRawX())*scale, 1f);
        Rect meteringRect = calculateTapArea(event.getRawY()*scale,(screenWIDTH - event.getRawX())*scale, 1f);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        if (parameters.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
            focusAreas.add(new Camera.Area(focusRect, 1000));
            parameters.setFocusAreas(focusAreas);
        }
        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
            meteringAreas.add(new Camera.Area(meteringRect, 1000));
            parameters.setMeteringAreas(meteringAreas);
        }
        camera.cancelAutoFocus();
        camera.setParameters(parameters);
        camera.autoFocus(autoFocusCallback);
    }


    private Rect calculateTapArea(float x, float y, float coefficient) {
      /*  y = bestWidth / screenWIDTH * y;
        x = bestHeight / screenHEIGHT * x;*/
        float focusAreaSize = 100;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        LogCat.i("calculateTapArea", "areaSize--->" + areaSize);//300
        LogCat.i("calculateTapArea", "x--->" + x + ",,,y--->" + y);//对的
        int centerX = (int) ((x / getResolution().width) * 2000 - 1000);
        int centerY = (int) ((y / getResolution().height) * 2000 - 1000);
        /*int centerX = (int) x ;
        int centerY = (int) y ;*/
        LogCat.i("calculateTapArea", "getResolution().width--->" + getResolution().width + ",,,,getResolution().height--->" + getResolution().height);
        int left = clamp(centerX - (areaSize / 2), -1000, 900);
        int top = clamp(centerY - (areaSize / 2), -1000, 900);
        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        LogCat.i("calculateTapArea", "left--->" + left + ",,,top--->" + top + ",,,right--->" + (left + areaSize) + ",,,bottom--->" + (top + areaSize));
        LogCat.i("calculateTapArea", "centerX--->" + centerX + ",,,centerY--->" + centerY);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top),
                Math.round(rectF.right), Math.round(rectF.bottom));
    }


    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    public Camera.Size getResolution() {
        Camera.Parameters params = camera.getParameters();
        Camera.Size s = params.getPreviewSize();
        LogCat.i("CAMERA", "PreviewWidth = " + s.width + "PreviewHeight = " + s.height);
        params.getFocalLength();
        return s;
    }

    private View.OnTouchListener cameraTouchAnim = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            LogCat.d("CAMERA", "点击了");

            LogCat.i("CAMERA", "visibility " + useBTN.getVisibility());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE: {
                    ViewSizeHelper.getInstance(HonestWalkerCamera.this).setSize(
                            cameraBTN, cameraBTNSizeSmall, cameraBTNSizeSmall);
                }
                break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    ViewSizeHelper.getInstance(HonestWalkerCamera.this).setSize(
                            cameraBTN, cameraBTNSize, cameraBTNSize);
                }
                break;
            }
            return false;
        }
    };


    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback()
            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
    {
        public void onShutter() {
            // TODO Auto-generated method stub
            LogCat.i("YU", "myShutterCallback:onShutter...");
            shootSound();
        }
    };

    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback() {
        //对jpeg图像数据的回调,最重要的一个回调

        public void onPictureTaken(final byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            LogCat.i("YU", "myJpegCallback:onPictureTaken...");

            if(null != data){
                cameraBTN.setClickable(true);

                photoPath = getNewFile();

                if(isReview || isCut){
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                    Matrix matrix =  new Matrix();
                    matrix.postRotate(shotOrientation);
                    matrix.postScale(1.5f, 1.5f);
                    rotat = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    bitmap.recycle();
                    bitmap = null;
                    System.gc();
                    previewLayout.setVisibility(View.GONE);
                    reviewLayout.setVisibility(View.VISIBLE);
                    reviewImage.setImageBitmap(rotat);


                    if(!isReview){
                        reviewImage.setShowCut(true);
                        useBTN.setText("保存");
                        useBTN.setOnClickListener(saveListener);
                    }else {}

                }else {
                    saveAndBroadcast();
                    finish();
                }

                camera.stopPreview();

                System.gc();
            }
        }
    };


    @Override
    protected void onDestroy() {
        mAlbumOrientationEventListener.disable();

        if(bitmap != null){
            bitmap.recycle();
        }
        bitmap = null;
        if(rotat != null){
            rotat.recycle();
        }
        rotat = null;

        super.onDestroy();

       /* PhotoPathSender photoPathSender = new PhotoPathSender();
        photoPathSender.sendPath(context, null);*/

    }

    private String getNewFile() {
        String photoName = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpeg";
        String picturePath = getSDImageCachePath() + photoName;
        LogCat.i("YU", picturePath);
        // picturePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"abc.png";
        return picturePath;
    }


    public String getSDImageCachePath() {
        String picPath = getSDCachePath() + "/image/";
        LogCat.i("TAG", "picPath = " + picPath);
        File picFile = new File(picPath);
        if(!picFile.exists()){
            if(picFile.mkdirs()){
                LogCat.i("TAG", " file is mkdir success");
            }else{
                LogCat.i("TAG", " file is mkdir fail");
            }
        }else{
            LogCat.i("TAG", " file is not mkdir");
        }
        return picPath;
    }
    public String getSDCachePath() {

        return SDCardUtil.getSDRootPath() + "kancartcamera";

    }

    public void shootSound() {
        AudioManager meng = (AudioManager) getApplication().getSystemService(Context.AUDIO_SERVICE);
        int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);
        MediaPlayer shootMP = null;
        if (volume != 0)
        {
            if (shootMP == null)
                shootMP = MediaPlayer.create(getApplication(), Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
            if (shootMP != null)
                shootMP.start();
        }
        meng = null;
        shootMP = null;
    }

    View.OnTouchListener surfaceViewTouchListener = new View.OnTouchListener() {
        private boolean lock = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (lock)
                return true;
            lock = true;

            focusOnTouch(event);
            UIHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lock = false;
                }
            }, 1500);
            return false;
        }
    };


    private Camera.Size binarysearchKey(List<Camera.Size> sizeList, int targetNum) {
        HashMap<Integer,Camera.Size> hashMap = new HashMap<>();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < sizeList.size(); i++) {
            int product = sizeList.get(i).width * sizeList.get(i).height;
            arrayList.add(product);
            hashMap.put(product,sizeList.get(i));
        }

        Object[] array = arrayList.toArray();
        Arrays.sort(array);
        int left = 0, right = 0;
        for (right = array.length - 1; left != right;) {
            int midIndex = (right + left) / 2;
            int mid = (right - left);
            int midValue = (Integer) array[midIndex];
            if (targetNum == midValue) {
                Log.i("find" , midIndex +"");
                return hashMap.get(array[midIndex]);
            }
            if (targetNum > midValue) {
                left = midIndex;
            } else {
                right = midIndex;
            }

            if (mid <= 2) {
                break;
            }
        }

        int rightnum = ((Integer) array[right]).intValue();
        int leftnum = ((Integer) array[left]).intValue();
        int ret =  Math.abs((rightnum - leftnum)/2) > Math.abs(rightnum -targetNum) ? leftnum : rightnum ;

        return hashMap.get(ret);
    }

    private void broadcast(){
        PhotoPathSender photoPathSender = new PhotoPathSender();
        photoPathSender.sendPath(context, photoPath);
    }

    private void savePhoto(){
        File file = new File(photoPath);
        try {
            ImageUtil.bitmapToFile(file.getAbsolutePath(), rotat, 100, rotat.getWidth());
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void saveAndBroadcast(){
        ThreadPool.threadPool(new Runnable() {
            @Override
            public void run() {
                savePhoto();
                broadcast();
            }
        });
    }


    View.OnClickListener useBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isCut){
                reviewImage.setShowCut(true);
                useBTN.setText("保存");
                useBTN.setOnClickListener(saveListener);
            }else {
                saveAndBroadcast();
                finish();
            }
        }
    };



    View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //cropBitmapManager.onSaveClicked(context, reviewImage);
            final Bitmap croppedImage = reviewImage.getCroppedImage();
            try {
                File file = ImageUtil.bitmapToFile(getNewFile(), croppedImage, 100, croppedImage.getWidth());
                //ToastHelper.alert(context, "hahah");
                PhotoPathSender photoPathSender = new PhotoPathSender();
                photoPathSender.sendPath(context,file.getAbsolutePath());
                finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            PhotoPathSender photoPathSender = new PhotoPathSender();
            photoPathSender.sendPath(context,null);
        }
        return super.onKeyDown(keyCode, event);
    }

    private class AlbumOrientationEventListener extends OrientationEventListener {
        public AlbumOrientationEventListener(Context context) {
            super(context);
        }

        public AlbumOrientationEventListener(Context context, int rate) {
            super(context, rate);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                return;
            }
            int Orientation = ((orientation + 45 + 90) / 90 * 90) % 360;
            //保证只返回四个方向
            if(mOrientation != Orientation){
                mOrientation = Orientation;
                LogCat.i("mOrientation", "mOrientation = " + mOrientation);
            }
        }
    }

}