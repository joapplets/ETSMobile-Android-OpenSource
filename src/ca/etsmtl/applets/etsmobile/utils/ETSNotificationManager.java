package ca.etsmtl.applets.etsmobile.utils;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import ca.etsmtl.applets.etsmobile.R;

public class ETSNotificationManager {

	public static int NOTIF_CALENDAR_COURSE = 1000;
	public static int NOTIF_GRADE_RESULT = 1001;

	public static void showNotification(Context context, String title,
			String subject, Intent intent, int id) {

		long when = 2000;
		final KeyguardManager keyGuardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		final boolean isScreenOff = keyGuardManager
				.inKeyguardRestrictedInputMode();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Service.NOTIFICATION_SERVICE);
		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);

		if (isScreenOff) {
			forceWakeUp(context, 5000);
		}

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {

			Notification notif = new Notification.Builder(context)
					.setContentTitle(title).setContentText(subject)
					.setSmallIcon(R.drawable.icon).setContentIntent(pIntent)
					.build();

			notif.defaults |= Notification.DEFAULT_SOUND;
			notif.defaults |= Notification.DEFAULT_VIBRATE;
			notificationManager.notify(id, notif);

		} else {
			Notification notif = new Notification(R.drawable.icon, subject,
					when);
			notif.defaults |= Notification.DEFAULT_SOUND;
			notif.defaults |= Notification.DEFAULT_VIBRATE;
			notificationManager.notify(id, notif);
		}

	}

	public static void removeNotification(Context context, int id) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Service.NOTIFICATION_SERVICE);
		notificationManager.cancel(id);
	}

	public static void forceWakeUp(final Context context, final int timeout) {
		final PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		final PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.SCREEN_BRIGHT_WAKE_LOCK
						| PowerManager.ACQUIRE_CAUSES_WAKEUP, "wl");
		wl.acquire(timeout);
	}

}
