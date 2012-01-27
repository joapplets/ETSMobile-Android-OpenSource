package com.applets.baseactivity;

import android.app.TabActivity;
import android.content.Intent;

import com.applets.AboutTabActivity;
import com.applets.ETSMobileActivity;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.markupartist.android.widget.actionbar.R;

public class BaseTabActivity extends TabActivity {
	private ActionBar actionBar;

	/**
	 * Creates the ActionBar and sets the actions
	 */
	protected void createActionBar(final String title, final int id) {
		actionBar = (ActionBar) findViewById(id);
		if (actionBar != null) {
			actionBar.setTitle(title);
			actionBar.setHomeAction(new IntentAction(this, ETSMobileActivity
					.createHomeAction(this), R.drawable.ic_menu_home));
			actionBar.addAction(new IntentAction(this, new Intent(this,
					AboutTabActivity.class), R.drawable.ic_menu_help));
		}

	}
}
