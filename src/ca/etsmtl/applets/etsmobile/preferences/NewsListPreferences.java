package ca.etsmtl.applets.etsmobile.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import ca.etsmtl.applets.etsmobile.R;

public class NewsListPreferences extends PreferenceActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.newspreferences);
	}
}
