package ca.etsmtl.applets.etsmobile.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ca.etsmtl.applets.etsmobile.services.NewsFetcher;

public class NewsAlarmReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, NewsFetcher.class);
		context.startService(i);
	}
}
