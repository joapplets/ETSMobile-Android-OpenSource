package com.applets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private Menu prefMenu = null;

    /**
     * Launch the Feed Selection ListActivity
     */
    private void launchNewsActivity() {
	try {
	    startActivity(new Intent(this, NewsListActivity.class));
	} catch (final Exception e) {
	    Log.e("Applets::", e.getMessage());
	}
    }

    /**
     * Launch Preference Activity
     */
    private void launchPreferences() {
	startActivity(new Intent(this, AppPreferenceActivity.class));
    }

    /**
     * Launch the program list selection
     */
    private void launchProgramList() {
	try {
	    startActivity(new Intent(this, ProgramListActivity.class));
	} catch (final Exception e) {
	    Log.e("Applets::", e.getMessage());
	}
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	GridView gridview = (GridView) findViewById(R.id.gridview);
	gridview.setAdapter(new HomeIconAdapter(this));

	gridview.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View v,
		    int position, long id) {

		switch (position) {
		case 1:
		    launchNewsActivity();
		    break;
		default:
		    Toast.makeText(v.getContext(), "Applets !", Toast.LENGTH_SHORT).show();
		    break;
		}
	    }
	});

	// findViewById(R.id.program_list_btn).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(final View v) {
	// launchProgramList();
	// }
	// });
	//
	// findViewById(R.id.feed_list_btn).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(final View v) {
	// launchNewsActivity();
	// }
	// });
	//
	// findViewById(R.id.mapBtn).setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(final View v) {
	// launchMap();
	// }
	// });
	//
	// findViewById(R.id.profile_btn).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(final View v) {
	// launchProfile();
	// }
	// });
	//
	// findViewById(R.id.cours_list_btn).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(final View v) {
	// startActivity(new Intent(v.getContext(),
	// CourseListActivity.class));
	// }
	// });
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