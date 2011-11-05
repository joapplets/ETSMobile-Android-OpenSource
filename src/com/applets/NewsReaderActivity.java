package com.applets;

import android.os.Bundle;
import android.webkit.WebView;

import com.applets.baseactivity.BaseActivity;
import com.applets.models.News;

public class NewsReaderActivity extends BaseActivity {

    private News currentNews;
    private WebView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.web_reader);

	// Parcel data sent from NewsListActivity
	Bundle b = getIntent().getExtras();
	currentNews = b.getParcelable(News.class.getName());

	createActionBar(currentNews.getName(), R.id.web_reader_actionbar);

	view = (WebView) findViewById(R.id.webView1);

	view.getSettings().setJavaScriptEnabled(true);
	view.loadUrl(currentNews.getUrl());
    }
}
