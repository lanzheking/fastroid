package com.honestwalker.android.kc_test;

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

import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.equipment.DisplayUtil;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lanzhe on 16-11-4.
 */
public class LogWindow implements View.OnTouchListener {

    private Context context;

    private LinearLayout windowView;
    private WindowManager wmManager;
    private WindowManager.LayoutParams wmParams;
    private TextView logTV;

    private Queue<String> infoQueue;
    private final int maxLogLine = 5;

    private Handler uiHandler = new Handler();

    public WindowManager show(Context context) {
        infoQueue = new LinkedBlockingQueue<>();
        return createWindow(context);
    }

    public void appentText(String text) {
        if(infoQueue.size() == maxLogLine) {
            infoQueue.poll();
        }
        infoQueue.add(text);
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                logTV.setText(infoQueueToText());
            }
        });
    }

    private String infoQueueToText() {
        StringBuffer infoSB = new StringBuffer();
        for (String s : infoQueue) {
            infoSB.append(s).append("\r\n");
        }
        return infoSB.toString();
    }

    private WindowManager createWindow(Context context) {
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

//        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;// 设置悬浮窗口长宽数据
//        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.width = (int) (DisplayUtil.getWidth(context) * 0.6);
        wmParams.height = (int) (DisplayUtil.getWidth(context) * 0.4);

        wmManager.addView(createWindowView(context), wmParams);

        return wmManager;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private View createWindowView(Context context) {
//        windowView = context.getLayoutInflater().inflate(R.layout.window , null);
        windowView = new LinearLayout(context);

        windowView.setBackgroundColor(0x55000000);

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams();
//        windowView.setLayoutParams();
        windowView.setPadding(40, 40, 40, 40);

        LinearLayout contentView = new LinearLayout(context);
        logTV = new TextView(context);
        logTV.setTextColor(Color.BLACK);
        logTV.setText("....");
        logTV.setTextSize(10);
        contentView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT));
        contentView.addView(logTV);
        contentView.setOnTouchListener(this);

        windowView.addView(contentView);

        return windowView;
    }

    private int lastX, lastY;
    private int paramX, paramY;
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
