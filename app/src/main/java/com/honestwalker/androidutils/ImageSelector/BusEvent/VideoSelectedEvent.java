package com.honestwalker.androidutils.ImageSelector.BusEvent;

import android.net.Uri;

import com.honestwalker.androidutils.ImageSelector.ImageSelectType;

import java.util.ArrayList;

/**
 * 图片选择事件总线事件, 如果要自定义事件，继承该类
 * Created by lanzhe on 17-5-25.
 */

public class VideoSelectedEvent implements ImageSelectorEvent {

    /**
     * 图片选择类型
     */
    private ImageSelectType type = ImageSelectType.TYPE_VIDEO;

    private Object taskTag;

    /**
     * 图片选择后路径
     */
    private String videoPath;

    private Uri videoUri;

    public ImageSelectType getType() {
        return type;
    }

    public void setType(ImageSelectType type) {
        this.type = type;
    }

    public Object getTaskTag() {
        return taskTag;
    }

    public void setTaskTag(Object taskTag) {
        this.taskTag = taskTag;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }
}
