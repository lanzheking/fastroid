package com.honestwalker.androidutils.ImageSelector;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.ImageSelector.BusEvent.ImageSelectorCancelEvent;
import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.honestwalker.androidutils.views.DialogPage;

import java.util.ArrayList;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 *   选择拍照或图片对话框
 */
public class ImageSelectorDialogPage extends DialogPage {

    private Button cameraBTN;
    private Button imageBTN;

    private ImageSelector imageSelector;

    private String outputCachePath;

//    private ImageSelectListener listener;

    private DialogInterface.OnDismissListener onDismissListener;

    private DialogInterface.OnCancelListener onCancelListener;

    /** 设定剪切比例宽 */
    private int aspectX;
    /** 设定剪切比例高 */
    private int aspectY;

    /** 是否需要剪切 */
    private boolean needCut;

    /** 图片输出最大宽度 */
    private int maxWidth;

    /**
     * @param context
     * @param outputCachePath 输出缓存文件名，剪切需要它 ， 要包含文件名
     */
    public ImageSelectorDialogPage(Activity context , String outputCachePath) {
        this(context , outputCachePath , false , 512 , 1 , 1);
    }

    public ImageSelectorDialogPage(Activity context , String outputCachePath ,
                                   boolean needCut , int maxWidth , int aspectX , int aspectY) {
        super(context, R.layout.page_imageselector);
        this.needCut = needCut;
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        this.maxWidth = maxWidth;
        this.outputCachePath = outputCachePath;
        setWidth((int) (DisplayUtil.getWidth(context) * 0.7));
    }

    public ImageSelectorDialogPage(Activity context, int resLayoutId, int theme) {
        super(context, resLayoutId, theme);
    }

    public ImageSelectorDialogPage(Activity context, View contentView, int theme) {
        super(context, contentView, theme);
    }

    @Override
    protected void initView() {
        cameraBTN = (Button) findViewById(R.id.topBTN);
        imageBTN  = (Button) findViewById(R.id.bottomBTN);
        setTitleVisible(false);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        imageSelector = new ImageSelector(context);

//        imageSelector.setImageSelectListener(new ImageSelectListener() {
//            @Override
//            public void onSelect() {
//                if (listener != null) {
//                    listener.onSelect();
//                }
//            }
//
//            @Override
//            public void onSelected(ImageSelectType type, ArrayList<String> imagePath) {
//                LogCat.d("FILE", "onSelected 1");
//                if (listener != null) {
//                    listener.onSelected(type, imagePath);
//                }
//            }
//
//            @Override
//            public void onCancel() {
//                if (listener != null) {
//                    listener.onCancel();
//                }
//            }
//
//            @Override
//            public void onComplete() {
//                if (listener != null) {
//                    listener.onComplete();
//                }
//            }
//        });

    }

    @Override
    protected void displayContent() {
        cameraBTN.setOnClickListener(btnOnClickListener);
        imageBTN.setOnClickListener(btnOnClickListener);

        getDialog().setOnDismissListener(onDismissListener);
//        getDialog().setOnCancelListener(onCancelListener);

        getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                ImageSelectorCancelEvent cancelEvent = new ImageSelectorCancelEvent();
                HermesEventBus.getDefault().post(cancelEvent);
            }
        });

    }

    private View.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.topBTN) {
                if(needCut) {
                    imageSelector.openCameraAndCrop(outputCachePath, maxWidth , aspectX, aspectY);
                } else {
                    imageSelector.openCamera(outputCachePath, maxWidth , aspectX, aspectY);
                }

            } else if(v.getId() == R.id.bottomBTN) {
                if(needCut) {
                    imageSelector.selectImageAndCrop(outputCachePath , ImageSelectorDialogPage.this.maxWidth, aspectX , aspectY);
                } else {
                    imageSelector.selectImage();
                }
            }
            dismiss();
        }
    };

//    public void setImageSelectorListener(ImageSelectListener listener) {
//        this.listener = listener;
//    }
//
//    public ImageSelectListener getImageSelectorListener() {
//        return this.listener;
//    }

    public ImageSelector getImageSelector() {
        return this.imageSelector;
    }

//    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
//        this.onDismissListener = onDismissListener;
//    }

//    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
//        this.onCancelListener = onCancelListener;
//    }

    public void onDestroy() {
        imageSelector.unRegisterEvent();
    }

}
