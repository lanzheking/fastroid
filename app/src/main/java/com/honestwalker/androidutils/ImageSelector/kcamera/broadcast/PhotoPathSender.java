package com.honestwalker.androidutils.ImageSelector.kcamera.broadcast;

import android.content.Context;
import android.content.Intent;

import com.honestwalker.android.commons.KancartReceiverManager;


/**
 * Created by hkyy on 16-10-21.
 */
public class PhotoPathSender {
    public void sendPath(Context context,String path){
        Intent intent = new Intent();
        intent.putExtra("photoPath",path);
        KancartReceiverManager.sendBroadcase(context, "GET_PICTURE", intent);
    }
}
