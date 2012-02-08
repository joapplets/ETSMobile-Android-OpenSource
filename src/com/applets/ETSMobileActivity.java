package com.applets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.applets.adapters.HomeIconAdapter;

public class ETSMobileActivity extends Activity {

    public class HomeGridListener implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position,
		long id) {

	    final Context c = v.getContext();
	    switch (position) {
	    case 0:
		startActivity(new Intent(c, StudentProfileActivity.class));
		break;
	    case 1:
		startActivity(new Intent(c, NewsListActivity.class));
		break;
	    // case 2:
	    // tour
	    case 3:
		startActivity(new Intent(c, UrgenceActivity.class));
		break;
	    // case 4:
	    // horraire
	    case 5:
		startActivity(new Intent(c, DirectoryActivity.class));
		break;
	    case 6:
		startActivity(new Intent(c, CourseListActivity.class));
		break;
	    default:
		Toast.makeText(c, "Applets !", Toast.LENGTH_SHORT).show();
		break;
	    }
	}
    }

    private Menu prefMenu = null;

    /**
     * Launch Preference Activity
     */
    private void launchPreferences() {
	startActivity(new Intent(this, AppPreferenceActivity.class));
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	GridView gridview = (GridView) findViewById(R.id.gridview);
	gridview.setAdapter(new HomeIconAdapter(this));

	gridview.setOnItemClickListener(new HomeGridListener());

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
	prefMenu = menu;
	new MenuInflater(getApplication()).inflate(R.menu.main_menu, prefMenu);
	return super.onCreateOptionsMenu(menu);
    }

    /**
     * When an option from the optionMenu is selected
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
	switch (item.getItemId()) {
	case R.id.main_menu_preferences:
	    launchPreferences();
	    break;
	}

	return super.onOptionsItemSelected(item);
    }
}