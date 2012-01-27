package com.applets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.applets.baseactivity.BaseTabActivity;
import com.markupartist.android.widget.actionbar.R;

public class AboutTabActivity extends BaseTabActivity {
	private static View createTabView(final Context context, final String text) {
		final View view = LayoutInflater.from(context).inflate(
				R.layout.customtab_bg, null);
		final TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	TabHost tab;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		createActionBar(getString(R.string.about_actionbar_title),
				R.id.about_actionbar);

		// Resources res = getResources( );
		// Get the table from the view
		tab = getTabHost();
		tab.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		// Setup tabs
		setupTab(new TextView(this), getString(R.string.about_info_tab_title),
				new Intent(this, InfoActivity.class));
		setupTab(new TextView(this),
				getString(R.string.about_urgence_tab_title), new Intent(this,
						UrgenceActivity.class));
	}

	private void setupTab(final View view, final String tag, final Intent i) {
		final View tabview = AboutTabActivity.createTabView(tab.getContext(),
				tag);
		final TabSpec setContent = tab.newTabSpec(tag).setIndicator(tabview)
				.setContent(i);
		tab.addTab(setContent);
	}
}
