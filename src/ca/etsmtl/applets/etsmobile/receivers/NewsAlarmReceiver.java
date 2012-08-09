package ca.etsmtl.applets.etsmobile.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ca.etsmtl.applets.etsmobile.services.NewsService;

public class NewsAlarmReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, NewsService.class);
		context.startService(i);
	}
}
