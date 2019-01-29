package com.honestwalker.androidutils.ImageSelector.BusEvent;

import com.honestwalker.androidutils.ImageSelector.ImageSelectType;

import java.util.ArrayList;

/**
 * 图片选择事件总线事件, 如果要自定义事件，继承该类
 * Created by lanzhe on 17-5-25.
 */

public class ImageSelectorCancelEvent implements ImageSelectorEvent {

    /**
     * 图片选择类型
     */
    private ImageSelectType type;

    /**
     * 图片选择后路径
     */
    private ArrayList<String> imagePath;

    /**
     * 处理结果 成功失败取消
     */
    private EventAction action;

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

    public EventAction getAction() {
        return action;
    }

    public void setAction(EventAction action) {
        this.action = action;
    }
}
