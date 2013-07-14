package ca.etsmtl.applets.etsmobile.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.ScheduleActivity.CalendarTaskHandler;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;
import ca.etsmtl.applets.etsmobile.models.JoursRemplaces;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;

import com.google.gson.annotations.SerializedName;

public class CalendarTaskMonth  extends CalendarTask {

	public static final int ON_POST_EXEC = 10;
    private final CalendarTaskHandler handler;

       public CalendarTaskMonth(Context ctx, final CalendarTaskHandler handler) {
    	super(ctx);
    	this.handler = handler;
    }
       



    @Override
    protected void onPostExecute(final ArrayList<Session> result) {
		super.onPostExecute(result);
	
		Collections.sort(result);
	
		// Bundle data = new Bundle();
		final Message msg = handler.obtainMessage(CalendarTaskMonth.ON_POST_EXEC, result);
		msg.sendToTarget();

    }

    @Override
    protected void onPreExecute() {	
		super.onPreExecute();
		handler.obtainMessage().sendToTarget();
    }
}