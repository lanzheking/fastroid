package com.honestwalker.androidutils.views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.equipment.DisplayUtil;

/**
 * Created by lanzhe on 18-4-27.
 */

public class TouchAble implements View.OnTouchListener {

    private int screenWidth, screenHeight;
    private View parentView;

    public TouchAble(Context context) {
        this.screenWidth = DisplayUtil.getWidth(context);
        this.screenHeight = DisplayUtil.getHeight(context);
    }

    public void touchable(View parentView, View touchListenView) {
        this.parentView = parentView;
        touchListenView.setOnTouchListener(this);
    }

    private int lastX, lastY;
    private int lastSX, lastSY;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int ea = event.getAction();
        switch(ea){
            case MotionEvent.ACTION_DOWN: {
                lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
                lastY = (int) event.getRawY();
                lastSX = (int) event.getX();
                lastSY = (int) event.getY();
            }break;
            case MotionEvent.ACTION_MOVE: {
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                int l = parentView.getLeft() + dx;
                int b = parentView.getBottom() + dy;
                int r = parentView.getRight() + dx;
                int t = parentView.getTop() + dy;
                // 下面判断移动是否超出屏幕
                if (l < 0) {
                    l = 0;
                    r = l + parentView.getWidth();
                }
                if (t < 0) {
                    t = 0;
                    b = t + parentView.getHeight();
                }
                if (r > screenWidth) {
                    r = screenWidth;
                    l = r - parentView.getWidth();
                }
                if (b > screenHeight) {
                    b = screenHeight;
                    t = b - parentView.getHeight();
                }
                parentView.layout(l, t, r, b);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                parentView.postInvalidate();
            }break;
            case MotionEvent.ACTION_UP: {
            } break;
            default:
                break;
        }
        return true;
    }
}
