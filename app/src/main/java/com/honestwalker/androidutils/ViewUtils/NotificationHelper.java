package com.honestwalker.androidutils.ViewUtils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.IO.LogCat;

public class NotificationHelper {

	private static final String TAG = "notification";

	private static NotificationManager notificationManager;

	private NotificationHelper() {
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static Notification show(Context context,
									int id,
									int iconSmallRes,
									int iconLargeIcon,
									String title,
									String message,
									boolean autoCancel,
									boolean vibrate,
									Intent intent) {

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = null;
		Notification.Builder builder = new Notification.Builder(context);
		notification = builder.setContentTitle(title)
				.setContentText(message)
				.setSmallIcon(iconSmallRes)
				.setAutoCancel(autoCancel)
				.setContentIntent(pendingIntent)
				.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), iconLargeIcon))
				.setPriority(Notification.PRIORITY_HIGH)
				.build();


		if(vibrate)    notification.defaults = Notification.DEFAULT_VIBRATE;
		if(!autoCancel) notification.flags |= Notification.FLAG_NO_CLEAR;

		if(notificationManager == null) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		notificationManager.notify(id, notification);
		return notification;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static Notification show(Context context, int iconSmallRes, int iconLargeIcon, String title, String message, Intent intent) {

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(iconSmallRes)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), iconLargeIcon)).build();
        return notification;

	}

}
