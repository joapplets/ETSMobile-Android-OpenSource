package com.applets;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.applets.models.News;

public class NewsReaderActivity extends Activity {

	private News currentNews;

	private WebView view;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_reader);

		// Parcel data sent from NewsListActivity
		final Bundle b = getIntent().getExtras();
		currentNews = b.getParcelable(News.class.getName());

		currentNews.getDescription();
		view = (WebView) findViewById(R.id.web_view_text);

		view.getSettings().setJavaScriptEnabled(true);
		view.loadUrl(currentNews.getSource());
	}
}
