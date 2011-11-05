package com.applets.baseactivity;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.applets.AboutTabActivity;
import com.applets.ETSMobileActivity;
import com.applets.R;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public abstract class BaseListActivity extends ListActivity {
    private ActionBar actionBar;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
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

    /**
     * Creates the ActionBar and sets the actions
     */
    protected void initActionBar(String title, int id) {
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
    protected void initSearchActionBar(String title, int id) {
	actionBar = (ActionBar) findViewById(id);
	actionBar.setTitle(title);
	actionBar.setHomeAction(new IntentAction(this, ETSMobileActivity
		.createHomeAction(this), R.drawable.ic_menu_home));
	actionBar.addAction(new IntentAction(this, new Intent(this,
		AboutTabActivity.class), R.drawable.ic_menu_search));

    }
}
