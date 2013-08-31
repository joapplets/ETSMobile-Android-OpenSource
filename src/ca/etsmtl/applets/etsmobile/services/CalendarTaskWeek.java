package ca.etsmtl.applets.etsmobile.services;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Handler;
import android.os.Message;
import ca.etsmtl.applets.etsmobile.models.Session;

public class CalendarTaskWeek extends CalendarTask {

	public static final int ON_POST_EXEC = 11;

	public CalendarTaskWeek(final Handler handler) {
		super(handler);
	}

	@Override
	protected void onPostExecute(final ArrayList<Session> result) {
		super.onPostExecute(result);

		Collections.sort(result);

		// Bundle data = new Bundle();
		final Message msg = handler.obtainMessage(
				CalendarTaskWeek.ON_POST_EXEC, result);
		msg.sendToTarget();

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		handler.obtainMessage().sendToTarget();
	}
}