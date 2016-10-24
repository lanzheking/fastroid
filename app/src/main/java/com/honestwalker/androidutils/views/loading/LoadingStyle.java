package com.honestwalker.androidutils.views.loading;

import android.view.View;

/**
 * Created by honestwalker on 16-1-20.
 */
public class LoadingStyle {

    private String id;
    private int layoutResId;
    private View contentView;
    private int styleResId;
    private int textResourceId;
    private int width;
    private int height;
    private String text;
    private boolean cancelable;
    private boolean touchCancelable;

    public LoadingStyle(){}

//    public DialogStyle(String dialogId,int layoutId,int styleId,int textResourceId,int width,int height,String text) {
//        this.dialogName = dialogId;
//        this.layoutId   = layoutId;
//        this.styleId    = styleId;
//        this.textResourceId	= textResourceId;
//        this.width		= width;
//        this.height 	= height;
//        this.text 		= text;
//    }
//
//    public DialogStyle(String dialogId,View contentView,int styleId,int width,int height,boolean cancelable){
//        this.dialogName = dialogId;
//        this.contentView = contentView;
//        this.styleId    = styleId;
//        this.width		= width;
//        this.height 	= height;
//        this.cancelable = cancelable;
//    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public void setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public int getStyleResId() {
        return styleResId;
    }

    public void setStyleResId(int styleResId) {
        this.styleResId = styleResId;
    }

    public int getTextResourceId() {
        return textResourceId;
    }

    public void setTextResourceId(int textResourceId) {
        this.textResourceId = textResourceId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public boolean isTouchCancelable() {
        return touchCancelable;
    }

    public void setTouchCancelable(boolean touchCancelable) {
        this.touchCancelable = touchCancelable;
    }
}
