package com.honestwalker.androidutils.ImageSelector.BusEvent;

import android.net.Uri;

import com.honestwalker.androidutils.ImageSelector.ImageSelectType;

import java.util.ArrayList;

/**
 * 图片选择事件总线事件, 如果要自定义事件，继承该类
 * Created by lanzhe on 17-5-25.
 */

public class ImageSelectedEvent implements ImageSelectorEvent {

    /**
     * 图片选择类型
     */
    private ImageSelectType type;

    private Object taskTag;

    /**
     * 图片选择后路径
     */
    private ArrayList<String> imagePath;

    private ArrayList<Uri> imageUri;

    public ImageSelectType getType() {
        return type;
    }

    public void setType(ImageSelectType type) {
        this.type = type;
    }

    public ArrayList<String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(ArrayList<String> imagePath) {
        this.imagePath = imagePath;
    }

    public ArrayList<Uri> getImageUri() {
        return imageUri;
    }

    public void setImageUri(ArrayList<Uri> imageUri) {
        this.imageUri = imageUri;
    }

    public Object getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(Object taskTag) {
        this.taskTag = taskTag;
    }
}
