package com.applets;

import java.util.ArrayList;

import android.os.Bundle;

import com.applets.baseactivity.BaseActivity;
import com.applets.models.Feed;
import com.applets.models.News;

public class NewsActivity extends BaseActivity {

    private ArrayList<News> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.base_list);

	createActionBar(getString(R.string.news_activity_title),
		R.id.base_list_actionbar);

	// Parcel data sent from the FeedListActivity
	Bundle b = getIntent().getExtras();
	Feed feed = b.getParcelable("com.applets.Models.Feed");

	// Get News from feed
//	final RSSParser rssParser = new RSSParser(feed.getUrl());
//	news = rssParser.parse();

	// Fill ListView
//	setListAdapter(new NewsAdapter(this, news));
    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//	super.onListItemClick(l, v, position, id);
//	String link = ((News) news.get(position)).getUrl();
//	startWebBrowser(link);
//    }
//
//    private void startWebBrowser(String url) {
//	Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//	startActivity(myIntent);
//    }
}
