package com.applets;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.applets.models.News;

public class NewsReaderActivity extends Activity {

    private News currentNews;

    // private WebView view;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.web_reader);

	// Parcel data sent from NewsListActivity
	final Bundle b = getIntent().getExtras();
	currentNews = b.getParcelable(News.class.getName());

	String s = currentNews.getDescription();
	((TextView) findViewById(R.id.web_view_text)).setText(Html.fromHtml(s));

	// view.getSettings().setJavaScriptEnabled(true);
	// view.loadUrl(currentNews.getUrl());
    }
}
