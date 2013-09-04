package ca.etsmtl.applets.etsmobile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import ca.etsmtl.applets.etsmobile.models.Session;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=4422c148", formKey = "")
public class ETSMobileApp extends Application {

	private static final String ETSMOBILE_CALENDRIER_SESSIONS = "etsmobile-calendrier-sessions";
	private static ETSMobileApp instance;
	private ArrayList<Session> sessions = new ArrayList<Session>();
	private SharedPreferences prefs;

	@Override
	public void onCreate() {
		// ACRA.init(this);
		// BugSenseHandler.initAndStartSession(this, "4422c148");
		super.onCreate();
		instance = this;
		prefs = getSharedPreferences("etsmobile-calendrier", MODE_PRIVATE);
	}

	public static ETSMobileApp getInstance() {
		return instance;
	}

	public ArrayList<Session> getSessions() {
		return sessions;
	}

	public void setSessions(ArrayList<Session> sessions) {
		this.sessions = sessions;
	}

	public void saveSessionsToPrefs(ArrayList<Session> result) {

		final Gson gson = new Gson();
		String json = gson.toJson(result);
		prefs = getSharedPreferences("etsmobile-calendrier", MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.putString(ETSMOBILE_CALENDRIER_SESSIONS, json).commit();
	}

	public ArrayList<Session> getSessionsFromPrefs() {
		ArrayList<Session> list;

		final Type type = new TypeToken<List<Session>>() {
		}.getType();

		list = new Gson().fromJson(
				prefs.getString(ETSMOBILE_CALENDRIER_SESSIONS, "[]"), type);
		if(list == null){
			list = new ArrayList<Session>();
		}
		return list;
	}

}
