package com.applets;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;

import com.applets.adapters.NewsAdapter;
import com.applets.baseactivity.BaseListActivity;
import com.applets.models.News;
import com.applets.models.NewsList;

public class NewsListActivity extends BaseListActivity {

    public static final String COM_APPLETS_MODELS_FEED = "com.applets.models.Feed";
    private ArrayList<News> news;
    private boolean[] userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.news_list);

	// Parcel data sent from the FeedListActivity
	// Bundle b = getIntent().getExtras();
	// Feed feed = b.getParcelable(COM_APPLETS_MODELS_FEED);
	// Init action bar with feed name
	initSearchActionBar(getString(R.string.news_activity_title),
		R.id.news_list_actionbar);

	// Get News from feed
	// XmlFeedParser rssParser = new XmlFeedParser(feed.getUrl());
	// news = rssParser.parse();

	// Fill ListView
	getNewsList();
	setListAdapter(new NewsAdapter(this, news));
    }

    /**
     * Creates the url based on the user prefs.
     */
    private void getNewsList() {
	// get the preferences
	SharedPreferences sharedPrefs = PreferenceManager
		.getDefaultSharedPreferences(this);
	userPref = new boolean[10];
	userPref[0] = sharedPrefs.getBoolean("feed_ets", true);
	userPref[1] = sharedPrefs.getBoolean("feed_aaets", true);
	userPref[2] = sharedPrefs.getBoolean("feed_applets", true);
	userPref[3] = sharedPrefs.getBoolean("feed_conjure", true);
	userPref[4] = sharedPrefs.getBoolean("feed_chinook", true);
	userPref[5] = sharedPrefs.getBoolean("feed_interface", true);

	// Retreive the list of feed from server
	StringBuilder sBuilder = new StringBuilder();
	sBuilder.append(getString(R.string.host));
	sBuilder.append(getString(R.string.api_feed));

//	sBuilder.append("?q=");
//	for (int i = 0; i < userPref.length; i++) {
//	    sBuilder.append(userPref[i] ? i : "");
//	    sBuilder.append(i + 1 < userPref.length ? "," : "");
//	}

	news = new NewsList(sBuilder.toString(), this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	String link = ((News) news.get(position)).getUrl();
	startWebBrowser(link);
    }

    private void startWebBrowser(String url) {
	Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	startActivity(myIntent);
    }
}
