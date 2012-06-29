package ca.etsmtl.applets.etsmobile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import ca.etsmtl.applets.etsmobile.adapters.NewsListAdapter;
import ca.etsmtl.applets.etsmobile.adapters.NewsListAdapter.Holder;
import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.preferences.NewsListPreferences;
import ca.etsmtl.applets.etsmobile.tools.db.NewsAdapter;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLAppletsHandler;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLParser;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLRssFbTwitterHandler;

public class NewsListActivity extends Activity implements AnimationListener,
		OnClickListener {

	private class QueryNewsFromBackground extends
			AsyncTask<Void, Integer, Integer> {

		LinearLayout footer = (LinearLayout) findViewById(R.id.listView_footer);

		@Override
		protected Integer doInBackground(final Void... params) {

			try {
				XMLAppletsHandler handler = new XMLRssFbTwitterHandler(
						NewsListActivity.this, NewsListActivity.RSS_ETS);
				XMLParser xml = new XMLParser(new URL(
						NewsListActivity.RSS_ETS_FEED), handler,
						NewsListActivity.this);
				addNewsToDB(xml.getParsedNews());

				handler = new XMLRssFbTwitterHandler(NewsListActivity.this,
						NewsListActivity.FACEBOOK);
				xml = new XMLParser(new URL(NewsListActivity.FACEBOOK_FEED),
						handler, NewsListActivity.this);
				addNewsToDB(xml.getParsedNews());

				handler = new XMLRssFbTwitterHandler(NewsListActivity.this,
						NewsListActivity.TWITTER);
				xml = new XMLParser(new URL(NewsListActivity.TWITTER_FEED),
						handler, NewsListActivity.this);
				addNewsToDB(xml.getParsedNews());

			} catch (final MalformedURLException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(final Integer result) {
			super.onPostExecute(result);
			footer.startAnimation(hideFooter());
			refreshListView();
		}

		@Override
		protected void onPreExecute() {
			footer.startAnimation(showFooter());
			super.onPreExecute();
		}

	}

	public final static String FACEBOOK = "facebook";
	private final static String FACEBOOK_FEED = "http://www.facebook.com/feeds/page.php?id=8632204375&format=rss20";
	private static NewsAdapter newsDB = null;
	private final static long REFRESHINTERVAL = 180000; // 30 minutes
	public final static String RSS_ETS = "rssETS";
	private final static String RSS_ETS_FEED = "http://www.etsmtl.ca/fils-rss?rss=NouvellesRSS";

	public final static String TWITTER = "twitter";

	private final static String TWITTER_FEED = "http://api.twitter.com/1/statuses/user_timeline.rss?screen_name=etsmtl";
	private boolean footerVisible = false;
	private ListView listView = null;
	private NewsListAdapter newsAdapter;
	// Contient la liste de nouvelles.
	private final ArrayList<News> newsList = new ArrayList<News>();
	private SharedPreferences newsPreferences = null;

	private SharedPreferences timerPreferences = null;

	private void addNewsToDB(final ArrayList<News> newNewsList) {
		if (newNewsList != null) {
			if (newNewsList.size() > 0) {
				for (final News n : newNewsList) {
					NewsListActivity.newsDB.insertNews(n.getTitle(),
							n.getPubDate(), n.getDescription(), n.getGuid(),
							n.getSource());
				}
			}
		}
	}

	private Animation hideFooter() {
		final Animation animation = new TranslateAnimation(0, 0, 0, 40);
		animation.setDuration(500);
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		animation.setAnimationListener(this);
		footerVisible = false;
		return animation;
	}

	private void initializeListView() {

		// On va chercher la listView dans le layout
		listView = (ListView) findViewById(R.id.listView);

		// On crée l'adapter qui permets de remplir la listview de nouvelles
		newsAdapter = new NewsListAdapter(getApplicationContext(),
				R.layout.news_list_item, newsList);

		// On lui associe un adapter
		listView.setAdapter(newsAdapter);

		// Ce qui arrive quand on click sur les éléments...
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> arg0, final View arg1,
					final int arg2, final long arg3) {

				// On crée un nouveau intent qui va nous permettre de lancer
				// la nouvelle activity
				final Intent intent = new Intent(getApplicationContext(),
						SingleNewsActivity.class);

				final Holder holder = (Holder) arg1.getTag();
				intent.putExtra("guid", holder.getGuid());

				// On lance l'intent qui va créer la nouvelle activity.
				startActivity(intent);

			}
		});
	}

	@Override
	public void onAnimationEnd(final Animation arg0) {
		if (!footerVisible) {
			final LinearLayout footer = (LinearLayout) findViewById(R.id.listView_footer);
			footer.setVisibility(View.GONE);
		}
	}

	@Override
	public void onAnimationRepeat(final Animation arg0) {
	}

	@Override
	public void onAnimationStart(final Animation arg0) {
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.base_list_home_btn:
			finish();
			break;
		case R.id.base_list_source_btn:
			final Intent intent = new Intent(getApplicationContext(),
					NewsListPreferences.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.news_list_view);
		NewsListActivity.newsDB = NewsAdapter.getInstance(this);

		initializeListView();

		// La pref qui indique la dernière fois que les nouvelles ont été mises
		// à jour
		timerPreferences = getSharedPreferences("timerPreferences",
				Context.MODE_PRIVATE);

		// on va chercher le timestamp de la dernière màj (en mili sec)
		final long lastUpdate = Long.valueOf(timerPreferences.getString(
				"lastUpdate", "0"));
		final Calendar calendar = Calendar.getInstance();

		// l'heure actuelle
		final long currentTime = calendar.getTime().getTime();

		// Si c'est la première fois qu'on utilise l'app on load et on mets à
		// jour
		// le timestamp. Sinon on check le timestamp, si ça fait plus que
		// "REFRESHINTEVAL"
		// qu'on a pas fetché des nouvelles on fetch.
		if (lastUpdate == 0) {
			timerPreferences.edit()
					.putString("lastUpdate", String.valueOf(currentTime))
					.commit();
			new QueryNewsFromBackground().execute();
		} else {
			if (lastUpdate + NewsListActivity.REFRESHINTERVAL <= currentTime) {
				new QueryNewsFromBackground().execute();
				timerPreferences.edit()
						.putString("lastUpdate", String.valueOf(currentTime))
						.commit();
			}
		}

		final ImageButton btnHome = (ImageButton) findViewById(R.id.base_list_home_btn);
		btnHome.setOnClickListener(this);

		final Button btnSources = (Button) findViewById(R.id.base_list_source_btn);
		btnSources.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.news_list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.newsListMenuUpdate:
			new QueryNewsFromBackground().execute();
			break;
		case R.id.newsListMenuPreferences:
			intent = new Intent(getApplicationContext(),
					NewsListPreferences.class);
			break;
		default:
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}

		return true;
	}

	@Override
	protected void onResume() {
		refreshListView();
		super.onResume();
	}

	private void refreshListView() {
		newsPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		final ArrayList<String> sources = new ArrayList<String>();

		if (newsPreferences.getBoolean(NewsListActivity.RSS_ETS, true)) {
			sources.add(NewsListActivity.RSS_ETS);
		}

		if (newsPreferences.getBoolean(NewsListActivity.FACEBOOK, true)) {
			sources.add(NewsListActivity.FACEBOOK);
		}

		if (newsPreferences.getBoolean(NewsListActivity.TWITTER, true)) {
			sources.add(NewsListActivity.TWITTER);
		}

		newsList.clear();

		if (sources.size() > 0) {
			newsList.addAll(NewsListActivity.newsDB.getNewsBySource(sources));
		}

		sources.clear();

		if (newsAdapter != null) {
			newsAdapter.notifyDataSetChanged();
		}
	}

	private Animation showFooter() {
		final LinearLayout footer = (LinearLayout) findViewById(R.id.listView_footer);
		footer.setVisibility(View.VISIBLE);
		final Animation animation = new TranslateAnimation(0, 0, 40, 0);
		animation.setDuration(500);
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		animation.setAnimationListener(this);
		footerVisible = true;
		return animation;
	}

}
