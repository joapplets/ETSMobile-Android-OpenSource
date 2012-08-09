package ca.etsmtl.applets.etsmobile;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class CourseListActivity extends ListActivity {

	private String u;
	private String p;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);
		registerForContextMenu(getListView());
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		u = prefs.getString("codeU", "");
		p = prefs.getString("codeP", "");
	}
}
