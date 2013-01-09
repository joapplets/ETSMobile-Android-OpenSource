package ca.etsmtl.applets.etsmobile.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ca.etsmtl.applets.etsmobile.services.NewsService;

public class NewsAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
	final Intent i = new Intent(context, NewsService.class);
	context.startService(i);
    }
}
