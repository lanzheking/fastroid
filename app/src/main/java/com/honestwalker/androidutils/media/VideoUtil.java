package com.honestwalker.androidutils.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.UriUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.io.File;

public class VideoUtil {

    public int getVideoDuration(Context context, String videoPath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        Uri uri = UriUtil.fromFile(new File(videoPath));
        return getVideoDuration(context, uri);
    }

    public int getVideoDuration(Context context, Uri uri) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
            int longtime = mediaPlayer.getDuration();
            LogCat.d("TEST", "视频长度 " + (longtime / 1000));
            return longtime;
        } catch (Exception e) {
            ExceptionUtil.showException("TEST", e);
        }
        return -1;
    }

}
