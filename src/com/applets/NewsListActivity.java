package com.applets;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.applets.adapters.NewsAdapter;
import com.applets.baseactivity.BaseListActivity;
import com.applets.models.Model;
import com.applets.models.News;
import com.applets.models.NewsList;
import com.applets.utils.db.NewsDbAdapter;
import com.applets.utils.xml.IAsyncTaskListener;
import com.applets.utils.xml.XMLParserTask;

public class NewsListActivity extends BaseListActivity implements
	IAsyncTaskListener {

    public static final String COM_APPLETS_MODELS_FEED = "com.applets.models.Feed";
    private NewsList news;
    private boolean[] userPref;
    private NewsDbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.news_list);
	// Init action bar with feed name
	initSearchActionBar(getString(R.string.news_activity_title),
		R.id.news_list_actionbar);
	db = (NewsDbAdapter) new NewsDbAdapter(this).open();
	// Retreive news
	getNewsList();
	// getListView().setVisibility(ListView.INVISIBLE);
    }

    /**
     * Creates the url based on the user prefs.
     */
    private void getNewsList() {
	final Cursor cursor = db.getAll();

	news = new NewsList();
	if (cursor.getCount() < 1) {
	    final String url = buildQuery();
	    new XMLParserTask(url, news, this).execute();
	} else {
	    // create news objects from DB
	    while (cursor.moveToNext()) {
		news.add(new News(
			cursor.getString(cursor
				.getColumnIndex(NewsDbAdapter.KEY_NEWS_ID)),
			cursor.getString(cursor
				.getColumnIndex(NewsDbAdapter.TABLE_TITLE)),
			cursor.getString(cursor
				.getColumnIndex(NewsDbAdapter.KEY_URL)),
			cursor.getString(cursor
				.getColumnIndex(NewsDbAdapter.KEY_DESCRIPTION)),
			cursor.getString(cursor
				.getColumnIndex(NewsDbAdapter.KEY_IMAGE)),
			cursor.getString(cursor
				.getColumnIndex(NewsDbAdapter.KEY_CREATOR)),
			cursor.getInt(cursor
				.getColumnIndex(NewsDbAdapter.KEY_FEED_ID)),
			cursor.getString(cursor
				.getColumnIndex(NewsDbAdapter.KEY_PUB_DATE))

		));
	    }
	    setListAdapter(new NewsAdapter(this, new ArrayList<Model>(news)));
	}
	cursor.close();
	db.close();
    }

    /**
     * Build the query to retreive news
     * 
     * @return String the full qualified url
     */
    private String buildQuery() {
	// get the prefered feeds
	SharedPreferences sharedPrefs = PreferenceManager
		.getDefaultSharedPreferences(this);
	userPref = new boolean[6];
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

	// sBuilder.append("?q=");
	// for (int i = 0; i < userPref.length; i++) {
	// sBuilder.append(userPref[i] ? i : "");
	// sBuilder.append(i + 1 < userPref.length ? "," : "");
	// }

	return sBuilder.toString();
    }

    /**
     * On click, launch browser. This should be replaced by our own webView
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	String url = ((News) news.get(position)).getUrl();
	Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	startActivity(myIntent);
    }

    /**
     * When the XMLParser is done with his job, we change the list data
     */
    @Override
    public void onPostExecute() {
	ArrayList<Model> models = new ArrayList<Model>(news);
	setListAdapter(new NewsAdapter(this, models));
	findViewById(R.id.news_list_loading).setVisibility(TextView.INVISIBLE);
    }

    @Override
    public void onProgressUpdate(Integer[] values) {
	((TextView) findViewById(R.id.news_list_loading)).setText("Loading :"
		+ values.toString());
    }
}
