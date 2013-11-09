/*******************************************************************************
 * Copyright 2013 Club ApplETS
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ca.etsmtl.applets.etsmobile.utils;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import ca.etsmtl.applets.etsmobile.R;

@SuppressLint("NewApi")
public class ETSNotificationManager {

	public static int NOTIF_CALENDAR_COURSE = 1000;
	public static int NOTIF_GRADE_RESULT = 1001;
	private static WakeLock wl;

	public static void showNotification(Context context, String title,
			String subject, Intent intent, int id) {

		final long when = 2000;
		final KeyguardManager keyGuardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		final boolean isScreenOff = keyGuardManager
				.inKeyguardRestrictedInputMode();

		final NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		final PendingIntent pIntent = PendingIntent.getActivity(context, 0,
				intent, 0);

		if (isScreenOff) {
			forceWakeUp(context, 5000);
		}

		final int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {

			final Notification notif = new Notification.Builder(context)
					.setContentTitle(title).setContentText(subject)
					.setSmallIcon(R.drawable.icon).setContentIntent(pIntent)
					.build();

			notif.defaults |= Notification.DEFAULT_SOUND;
			notif.defaults |= Notification.DEFAULT_VIBRATE;
			notificationManager.notify(id, notif);

		} else {
			final Notification notif = new Notification(R.drawable.icon,
					subject, when);
			notif.defaults |= Notification.DEFAULT_SOUND;
			notif.defaults |= Notification.DEFAULT_VIBRATE;
			notificationManager.notify(id, notif);
		}

	}

	public static void removeNotification(Context context, int id) {
		final NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(id);
		wl.release();
	}

	public static void forceWakeUp(final Context context, final int timeout) {
		final PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "wl");
		wl.acquire(timeout);
	}

}
