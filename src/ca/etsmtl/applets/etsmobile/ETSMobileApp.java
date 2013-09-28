package ca.etsmtl.applets.etsmobile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import ca.etsmtl.applets.etsmobile.models.Session;

import com.bugsense.trace.BugSenseHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.testflightapp.lib.TestFlight;

@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=4422c148", formKey = "")
public class ETSMobileApp extends Application {

	private static final String ETSMOBILE_CALENDRIER_SESSIONS = "etsmobile-calendrier-sessions";
	private static ETSMobileApp instance;
	private ArrayList<Session> sessions = new ArrayList<Session>();
	private SharedPreferences prefs;

	@Override
	public void onCreate() {
		super.onCreate();

		ACRA.init(this);
		BugSenseHandler.initAndStartSession(this, "4422c148");
		TestFlight.takeOff(this, "8531a36e-8e9d-4dff-915b-716ed7768795");

		instance = this;
		prefs = PreferenceManager.getDefaultSharedPreferences(this);// getSharedPreferences("etsmobile-calendrier",
																	// MODE_PRIVATE);
	}

	public static ETSMobileApp getInstance() {
		return instance;
	}

	public void setSessions(ArrayList<Session> sessions) {
		this.sessions = sessions;
	}

	public void saveSessionsToPrefs(ArrayList<Session> result) {

		final Gson gson = new Gson();
		if (!result.isEmpty()) {
			final String json = gson.toJson(result);
			Log.d("ETSMobileApp::CalendrierToJSON", json);

			final Editor edit = prefs.edit();
			edit.putString(ETSMOBILE_CALENDRIER_SESSIONS, json);
			edit.commit();
		}
	}

	public ArrayList<Session> getSessionsFromPrefs() {
		ArrayList<Session> list;

		final Type type = new TypeToken<List<Session>>() {
		}.getType();

		final String string = prefs.getString(ETSMOBILE_CALENDRIER_SESSIONS,
				"[]");

		Log.d("ETSMobileApp::CalendrierJSONtoPOJO", string);

		list = new Gson().fromJson(string, type);
		if (list == null) {
			list = new ArrayList<Session>();
		}
		Log.d("ETSMobileApp::CalendrierJSONtoPOJO", "" + list.size());
		return list;
	}

}
