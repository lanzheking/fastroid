package com.honestwalker.android.commons.jscallback.bean;


/**
 * Created by honestwalker on 15-8-29.
 */
public class BrowseImageBean extends JSActionParamBean {

    private String curPic;
    private String[]  picArray;

    public String getCurPic() {
        return curPic;
    }

    public void setCurPic(String curPic) {
        this.curPic = curPic;
    }

    public String[] getPicArray() {
        return picArray;
    }

    public void setPicArray(String[] picArray) {
        this.picArray = picArray;
    }

}
