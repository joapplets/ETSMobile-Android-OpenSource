package com.applets;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.applets.adapters.NewsCursorAdapter;
import com.applets.models.Model;
import com.applets.models.News;
import com.applets.models.NewsList;
import com.applets.utils.db.NewsDbAdapter;
import com.applets.utils.xml.IAsyncTaskListener;

public class NewsListActivity extends ListActivity implements
		IAsyncTaskListener {

	private NewsDbAdapter db;
	private Menu menu;
	private NewsList news;
	private boolean[] userPref;
	private ProgressDialog dialog;

	/**
	 * "Refresh",Retreive latest news from the server
	 */
	private void getLatestNews() {
		String[] urls = new String[4];
		// get the prefered feeds
		final SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		userPref = new boolean[2];
		userPref[0] = sharedPrefs.getBoolean("feed_ets", true);
		userPref[1] = sharedPrefs.getBoolean("feed_ets_fb", true);

		if (userPref[0] == true) {
			urls[0] = getString(R.string.ets_rss);
		}

		if (userPref[1] == false) {
			urls[1] = getString(R.string.ets_fb);
		}
		// dialog = ProgressDialog.show(NewsListActivity.this, "",
		// "Loading. Please wait...", true);
		// dialog.setCancelable(true);

		// for (String url : urls) {
		news.execute(urls[0], this);
		// }

	}

	/**
	 * Init the list view with data currently sotred on phone
	 */
	private void getList() {

		final Cursor cursor = db.getAll();

		news = new NewsList();
		if (cursor.getCount() < 1) {
			getLatestNews();
		} else {
			setListAdapter(new NewsCursorAdapter(this, cursor));
		}
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);

		db = (NewsDbAdapter) new NewsDbAdapter(this).open();
		getList();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		this.menu = menu;
		new MenuInflater(this).inflate(R.menu.news_menu, this.menu);
		return super.onCreateOptionsMenu(this.menu);
	}

	/**
	 * On click, launch the NewsReaderActivity. This should be replaced by our
	 * own webView
	 */
	@Override
	protected void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
		super.onListItemClick(l, v, position, id);

		final Intent myIntent = new Intent(this, NewsReaderActivity.class);
		myIntent.putExtra(News.class.getName(), db.get((int) id));

		startActivity(myIntent);
	}

	/**
	 * When an option from the optionMenu is selected
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_menu_preferences:
			startActivity(new Intent(this, AppPreferenceActivity.class));
			break;
		case R.id.main_menu_refresh:
			getLatestNews();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * When the XMLParser is done with his job, we change the list data
	 */
	@Override
	public void onPostExecute() {

		// dialog.dismiss();
		if (news.size() == 0) {
			Toast.makeText(this, getString(R.string.empty_update),
					Toast.LENGTH_SHORT).show();
		} else {
			// insert data
			for (final Model model : news) {
				if (model != null) {
					db.create(model);
				}
			}
		}
		setListAdapter(new NewsCursorAdapter(this, db.getAll()));
	}

}
