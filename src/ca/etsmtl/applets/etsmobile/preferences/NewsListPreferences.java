package ca.etsmtl.applets.etsmobile.preferences;

import ca.etsmtl.applets.etsmobile.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class NewsListPreferences extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
