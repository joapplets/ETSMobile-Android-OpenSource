package ca.etsmtl.applets.etsmobile.receivers;

import ca.etsmtl.applets.etsmobile.services.NewsFetcher;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NewsAlarmReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, NewsFetcher.class);
		context.startService(i);
	}
}
