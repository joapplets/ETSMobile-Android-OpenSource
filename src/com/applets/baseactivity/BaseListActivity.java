package com.applets.baseactivity;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.applets.AboutTabActivity;
import com.applets.ETSMobileActivity;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.markupartist.android.widget.actionbar.R;

public abstract class BaseListActivity extends ListActivity {
    private ActionBar actionBar;
    protected ProgressDialog progressDialog;

    /**
     * Creates the ActionBar and sets the actions
     */
    protected void initActionBar(final String title, final int id) {
	actionBar = (ActionBar) findViewById(id);
	actionBar.setTitle(title);
	actionBar.setHomeAction(new IntentAction(this, ETSMobileActivity
		.createHomeAction(this), R.drawable.ic_menu_home));
	actionBar.addAction(new IntentAction(this, new Intent(this,
		AboutTabActivity.class), R.drawable.ic_menu_help));
    }

    /**
     * Creates the ActionBar and sets the actions
     */
    protected void initSearchActionBar(final String title, final int id) {
	actionBar = (ActionBar) findViewById(id);
	actionBar.setTitle(title);
	actionBar.setHomeAction(new IntentAction(this, ETSMobileActivity
		.createHomeAction(this), R.drawable.ic_menu_home));
	actionBar.addAction(new IntentAction(this, new Intent(this,
		AboutTabActivity.class), R.drawable.ic_menu_search));

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
	switch (id) {
	case 0:
	    progressDialog = new ProgressDialog(BaseListActivity.this);
	    progressDialog.setMessage(getString(R.string.loading));
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progressDialog.setCancelable(false);
	    return progressDialog;
	default:
	    return null;
	}
    }
}
