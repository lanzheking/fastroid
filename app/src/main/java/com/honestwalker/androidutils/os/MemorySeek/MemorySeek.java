package com.honestwalker.androidutils.os.MemorySeek;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honestwalker.androidutils.MemoryUtil;
import com.honestwalker.androidutils.ShowMemoryListener;

/**
 * Created by honestwalker on 15-10-10.
 */
public class MemorySeek implements View.OnTouchListener {

    private LinearLayout windowView;
    private WindowManager wmManager;
    private WindowManager.LayoutParams wmParams;
    private TextView memInfoTV;

    private Handler uiHandler = new Handler();

    public static WindowManager show(Activity context) {

        final MemorySeek ms = new MemorySeek();

        MemoryUtil.onShowMemoryInfo(context, new ShowMemoryListener() {
            @Override
            public void onShow(final double heapsize, final double total) {
                Log.d("MEM", "heapsize=" + heapsize + " / " + total);
                ms.uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ms.memInfoTV.setText(heapsize + " MB used. " + " / " + total + " MB ");
                    }
                });
            }
        });

        return ms.createWindow(context);
    }

    private WindowManager createWindow(Activity context) {
        Log.d("MEM", "创建窗口");
        wmManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();

        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT; // 设置window type
        wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        wmParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
//        wmParams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;

//        wmParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM; // 调整悬浮窗口至右侧中间
        wmParams.x = 0;// 以屏幕左上角为原点，设置x、y初始值
        wmParams.y = 0;

        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;// 设置悬浮窗口长宽数据
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        wmManager.addView(createWindowView(context), wmParams);

        return wmManager;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private View createWindowView(Activity context) {
//        windowView = context.getLayoutInflater().inflate(R.layout.window , null);
        windowView = new LinearLayout(context);

        windowView.setBackgroundColor(0x55000000);

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams();
//        windowView.setLayoutParams();
        windowView.setPadding(40, 40, 40, 40);

        LinearLayout contentView = new LinearLayout(context);
        memInfoTV = new TextView(context);
        memInfoTV.setTextColor(Color.BLACK);
        memInfoTV.setText("....");
        contentView.addView(memInfoTV);
        contentView.setOnTouchListener(this);

        windowView.addView(contentView);

        return windowView;
    }

    int lastX, lastY;
    int paramX, paramY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                paramX = wmParams.x;
                paramY = wmParams.y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                wmParams.x = paramX + dx;
                wmParams.y = paramY + dy;
                // 更新悬浮窗位置
                wmManager.updateViewLayout(windowView, wmParams);
                break;
        }
        return true;
    }
}
