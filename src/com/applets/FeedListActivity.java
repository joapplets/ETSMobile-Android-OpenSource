package com.applets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;

import com.applets.adapters.FeedListAdapter;
import com.applets.baseactivity.BaseListActivity;
import com.applets.models.Feed;
import com.applets.models.FeedList;

public class FeedListActivity extends BaseListActivity {

    private FeedList feedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.base_list);

	// Set the default Actions of the ActionBar
	initActionBar(getString(R.string.feed_list_title),
		R.id.base_list_actionbar);

	// get the preferences
	SharedPreferences sharedPrefs = PreferenceManager
		.getDefaultSharedPreferences(this);
	boolean bFeedEts = sharedPrefs.getBoolean("feed_ets", true);
	boolean bFeedAaets = sharedPrefs.getBoolean("feed_aaets", true);
	boolean bFeedApplets = sharedPrefs.getBoolean("feed_applets", true);
	boolean bFeedConjure = sharedPrefs.getBoolean("feed_conjure", true);
	boolean bFeedChinook = sharedPrefs.getBoolean("feed_chinook", true);
	boolean bFeedInterface = sharedPrefs.getBoolean("feed_interface", true);

	// Retreive the list of feed from server
	StringBuilder sBuilder = new StringBuilder();
	sBuilder.append(getString(R.string.host));
	sBuilder.append(getString(R.string.api_feed));

	if (!(bFeedEts && bFeedApplets && bFeedAaets && bFeedConjure
		&& bFeedChinook && bFeedInterface)) {
	    sBuilder.append("?q=id:");
	    sBuilder.append(bFeedEts ? "1," : "");
	    sBuilder.append(bFeedApplets ? "2" : "");
	}

	feedList = new FeedList(sBuilder.toString(), this);

	// Fill the interface
	setListAdapter(new FeedListAdapter(this, feedList));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	// Load the NewsListActivity with the selected site
	loadNewsActivity(feedList.get(position));
    }

    private void loadNewsActivity(Feed feed) {
	Intent i = new Intent(this, NewsListActivity.class);
	i.putExtra(Feed.class.getName(), feed);
	startActivity(i);
    }
}
