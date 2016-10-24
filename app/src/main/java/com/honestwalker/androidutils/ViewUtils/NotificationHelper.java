package com.honestwalker.androidutils.ViewUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationHelper {
	private NotificationHelper() {
	}

	public static Notification show(Context context,int iconRes,String title,String message,Intent intent) {
//		int icon = R.drawable.icon;
		int icon = iconRes;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
//		String title = context.getString(R.string.app_name);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FILL_IN_DATA);
  	    intent.setAction(Intent.ACTION_VIEW);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, title, message, pendingIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
		return notification;
	}

}
