package ca.etsmtl.applets.etsmobile;

import java.util.ArrayList;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import ca.etsmtl.applets.etsmobile.models.Session;

@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=4a893e6b", formKey = "")
public class ETSMobileApp extends Application {

    private static ETSMobileApp instance;
    private ArrayList<Session> sessions = new ArrayList<Session>();

    @Override
    public void onCreate() {
	ACRA.init(this);
	super.onCreate();
	instance = this;
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

}
