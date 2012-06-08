package ca.etsmtl.applets.etsmobile;

import android.app.ListActivity;
import android.os.Bundle;

public class CourseListActivity extends ListActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);
		registerForContextMenu(getListView());
	}
}
