package com.applets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.applets.baseactivity.BaseActivity;
import com.markupartist.android.widget.actionbar.R;

public class ETSMobileActivity extends BaseActivity {

    /**
     * Creates an intent that will bring the user back to the HomeScreen
     * 
     * @param mainActivity
     * @return Intent MainIntent
     */
    public static Intent createHomeAction(final Activity mainActivity) {
	final Intent i = new Intent(mainActivity, ETSMobileActivity.class);
	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	return i;
    }

    private Menu prefMenu = null;

    protected void launchInteractiveTour() {
	startActivity(new Intent(this, InterrestGridActivity.class));
    }

    /**
     * Launch the Interactive Tour
     */
    protected void launchMap() {
	// startActivity(new Intent(this, InterActiveTourActivity.class));
    }

    /**
     * Launch the Feed Selection ListActivity
     */
    public void launchNewsActivity() {
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

    protected void launchProfile() {
	startActivity(new Intent(this, ProfileActivity.class));

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

	createActionBar(getString(R.string.main_title), R.id.main_actionbar);

	findViewById(R.id.program_list_btn).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(final View v) {
			launchProgramList();
		    }
		});

	findViewById(R.id.feed_list_btn).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(final View v) {
			launchNewsActivity();
		    }
		});

	findViewById(R.id.mapBtn).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(final View v) {
		launchMap();
	    }
	});

	findViewById(R.id.discoverBtn).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(final View v) {
			launchInteractiveTour();
		    }
		});

	findViewById(R.id.profile_btn).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(final View v) {
			launchProfile();
		    }
		});

	findViewById(R.id.cours_list_btn).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(final View v) {
			startActivity(new Intent(v.getContext(),
				CourseListActivity.class));
		    }
		});
	findViewById(R.id.main_radio_btn).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(final View v) {
			startActivity(new Intent(v.getContext(),
				RadioActivity.class));
		    }
		});
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