package com.honestwalker.androidutils.ui;

/**
 * Depiction:
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-2-19 上午10:19. <br />
 * Rewrite Date :  16-2-19 上午10:19. <br />
 */
public class Size {

    private float width;

    private float height;

    public Size() {}

    public Size(float width , float height) {
        this.width = width;
        this.height = height;
    }

    public Size(int width , int height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
