package com.honestwalker.android.commons.activity;

import android.app.Activity;
import android.content.Intent;

import com.honestwalker.android.commons.utils.StartActivityHelper;

/**
 * Created by honestwalker on 15-10-9.
 */
public class BaseWebActivityEntry {

    public static void toActivity(Activity context , String url) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        StartActivityHelper.toActivity(context , BaseWebActivity.class , intent);
    }

}
