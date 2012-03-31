package com.applets;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.applets.adapters.NewsListAdapter;
import com.applets.adapters.NewsListAdapter.Holder;
import com.applets.models.News;
import com.applets.tools.db.NewsDB;
import com.applets.tools.xml.XMLAppletsHandler;
import com.applets.tools.xml.XMLParser;
import com.applets.tools.xml.XMLRSSAndFacebookHandler;

public class NewsListActivity extends Activity {

	private final static String FB = "http://www.facebook.com/feeds/page.php?id=8632204375&format=rss20";
	private final static String RSS_ETS = "http://www.etsmtl.ca/fils-rss?rss=NouvellesRSS";
	// Handler pour faire un dismiss du dialog et remplir la listView
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			loadContentIntoListView();

			// On ferme le popup qu'on a lancé précédement.
			progessDialog.dismiss();
		}
	};

	private ListView listView = null;

	private ArrayList<Object> newNewsList = null;
	private NewsListAdapter newsAdapter;
	private NewsDB newsDB;
	// Contient la liste de nouvelles.
	private ArrayList<News> newsList = new ArrayList<News>();

	private ProgressDialog progessDialog = null;

	private void addNewsToDB(final ArrayList<Object> newNewsList) {
		if (newNewsList != null) {
			if (newNewsList.size() > 0) {
				for (final Object object : newNewsList) {
					final News n = (News) object;
					newsDB.insertNews(n.getTitle(), n.getPubDate(),
							n.getDescription(), n.getGuid(), n.getSource());
				}
			}
		}
	}

	private void loadContentIntoListView() {

		// On crée l'adapter qui permets de remplir la listview de "views"
		newsAdapter = new NewsListAdapter(getApplicationContext(),
				R.layout.news_list_item, newsList);

		// On va chercher la listView dans le layout
		listView = (ListView) findViewById(R.id.listView);

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
						NewsActivity.class);

				final Holder holder = (Holder) arg1.getTag();
				intent.putExtra("guid", holder.getGuid());

				// On lance l'intent qui va créer la nouvelle activity.
				startActivity(intent);

			}
		});
	}

	// Vérifie s'il y a des nouvelles news, s'il y a en, il les rentre
	// dans la BD.
	private ArrayList<Object> loadNews() {

		XMLAppletsHandler handler = new XMLRSSAndFacebookHandler(this, "rssETS");

		try {
			XMLParser xml = new XMLParser(new URL(NewsListActivity.RSS_ETS),
					handler, this);
			newNewsList = xml.getParsedNews();
			addNewsToDB(newNewsList);

			handler = new XMLRSSAndFacebookHandler(this, "facebook");
			xml = new XMLParser(new URL(NewsListActivity.FB), handler, this);
			newNewsList = xml.getParsedNews();
			addNewsToDB(newNewsList);

		} catch (final MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return newNewsList;

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.news_list_view);
		newsDB = NewsDB.getInstance(this);

		// La methode getLastNonConfigurationInstance est appellée à chaque
		// fois que l'usager change l'orientation de l'écran.
		newNewsList = (ArrayList<Object>) getLastNonConfigurationInstance();

		// On vérifie si l'orientation a changé. Dans le fond si on vient juste
		// de
		// lancer l'acitivé on va recevoir un "null" et on va aller voir
		// s'il y a des nouvelles nouvelles. Si l'usager a changé l'orientation
		// on va recevoir un objet X ce qui veut dire qu'il ne faut plus
		// chercher
		// faire un query au rss...
		if (newNewsList == null) {

			// On part un pop up qui dit d'attendre.
			progessDialog = ProgressDialog.show(this, "App ETS",
					"Veuillez patentier pendant "
							+ "que je telecharge les dernières nouvelles.",
					true, false);

			new Thread(new Runnable() {

				@Override
				public void run() {
					// Et on va voir s'il y a des nouvelles news.
					loadNews();

					// On les affiche
					handler.sendEmptyMessage(0);
				}
			}).start();
		}

		final ImageButton btnHome = (ImageButton) findViewById(R.id.base_list_home_btn);
		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				finish();
			}
		});

		final Button btnSources = (Button) findViewById(R.id.base_list_source_btn);
		btnSources.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final Intent intent = new Intent(getApplicationContext(),
						NewsListPreferences.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		menu.add("Preferences");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			final Intent intent = new Intent(getApplicationContext(),
					NewsListPreferences.class);
			startActivity(intent);
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		final SharedPreferences sharePref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		final boolean showRSSETS = sharePref.getBoolean("rssETS", true);
		final boolean showFB = sharePref.getBoolean("FB", true);

		if (showRSSETS && showFB) {
			newsList = newsDB.getAllNews();
		} else if (showRSSETS && !showFB) {
			newsList = newsDB.getNewsBySource("rssETS");
		} else if (showFB && !showRSSETS) {
			newsList = newsDB.getNewsBySource("facebook");
		} else {
			newsList.clear();
		}

		if (newsAdapter == null) {
			loadContentIntoListView();
		} else {
			newsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return newNewsList;
	}
}
