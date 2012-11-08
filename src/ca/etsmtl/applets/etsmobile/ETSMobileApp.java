package ca.etsmtl.applets.etsmobile;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=4a893e6b", formKey = "")
public class ETSMobileApp extends Application {

	@Override
	public void onCreate() {
		ACRA.init(this);
		super.onCreate();
	}
}
