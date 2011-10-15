package com.applets.baseactivity;

import android.app.ListActivity;
import android.content.Intent;

import com.applets.AboutTabActivity;
import com.applets.MainActivity;
import com.applets.R;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public abstract class BaseListActivity extends ListActivity {
    private ActionBar actionBar;

    /**
     * Creates the ActionBar and sets the actions
     */
    protected void initActionBar(String title, int id) {
	actionBar = (ActionBar) findViewById(id);
	actionBar.setTitle(title);
	actionBar.setHomeAction(new IntentAction(this, MainActivity
		.createHomeAction(this), R.drawable.ic_menu_home));
	actionBar.addAction(new IntentAction(this, new Intent(this,
		AboutTabActivity.class), R.drawable.ic_menu_help));
    }

    /**
     * Creates the ActionBar and sets the actions
     */
    protected void initSearchActionBar(String title, int id) {
	actionBar = (ActionBar) findViewById(id);
	actionBar.setTitle(title);
	actionBar.setHomeAction(new IntentAction(this, MainActivity
		.createHomeAction(this), R.drawable.ic_menu_home));
	actionBar.addAction(new IntentAction(this, new Intent(this,
		AboutTabActivity.class), R.drawable.ic_menu_search));

    }
}
