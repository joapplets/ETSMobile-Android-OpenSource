package com.applets;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.applets.adapters.NewsAdapter;
import com.applets.baseactivity.BaseListActivity;
import com.applets.models.Feed;
import com.applets.models.News;
import com.applets.utils.XMLParser;

public class NewsActivity extends BaseListActivity {

    private ArrayList<News> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.base_list);

	initActionBar(getString(R.string.news_activity_title),
		R.id.base_list_actionbar);

	// Parcel data sent from the FeedListActivity
	Bundle b = getIntent().getExtras();
	Feed feed = b.getParcelable("com.applets.Models.Feed");

	// Get News from feed
//	final RSSParser rssParser = new RSSParser(feed.getUrl());
//	news = rssParser.parse();

	// Fill ListView
	setListAdapter(new NewsAdapter(this, news));
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
