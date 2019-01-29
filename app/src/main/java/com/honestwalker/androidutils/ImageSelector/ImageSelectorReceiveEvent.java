package com.honestwalker.androidutils.ImageSelector;

import android.content.Context;
import android.content.Intent;

import com.honestwalker.androidutils.ImageSelector.BusEvent.EventAction;
import com.honestwalker.androidutils.ImageSelector.ImageSelectType;

import java.util.ArrayList;

/**
 * 图片选择事件总线事件, 如果要自定义事件，继承该类
 * Created by lanzhe on 17-5-25.
 */

class ImageSelectorReceiveEvent {

    private Context context;

    private Intent intent;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
