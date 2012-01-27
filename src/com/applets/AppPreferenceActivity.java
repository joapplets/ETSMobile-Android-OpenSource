package com.applets;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.markupartist.android.widget.actionbar.R;

public class AppPreferenceActivity extends PreferenceActivity {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

}
