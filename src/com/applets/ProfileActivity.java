package com.applets;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.applets.baseactivity.BaseActivity;
import com.markupartist.android.widget.actionbar.R;

public class ProfileActivity extends BaseActivity {

    private LinearLayout mySessions;
    private Menu prefMenu;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.profile);

	createActionBar(getString(R.string.profile_title),
		R.id.profile_actionbar);
	mySessions = (LinearLayout) findViewById(R.id.linearLayout1);
	mySessions.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(final View v) {
		startActivity(new Intent(v.getContext(),
			ProfileEditionActivity.class));
	    }
	});
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
	prefMenu = menu;
	new MenuInflater(getApplication()).inflate(R.menu.profile_menu,
		prefMenu);
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
	// TODO Auto-generated method stub
	return super.onOptionsItemSelected(item);
    }
}
