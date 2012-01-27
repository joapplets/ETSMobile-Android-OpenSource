package com.applets;

import android.os.Bundle;

import com.applets.baseactivity.BaseActivity;
import com.markupartist.android.widget.actionbar.R;

public class RadioActivity extends BaseActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.radio);

		createActionBar(getString(R.string.radio_title), R.id.radio_actionbar);
	}
}
