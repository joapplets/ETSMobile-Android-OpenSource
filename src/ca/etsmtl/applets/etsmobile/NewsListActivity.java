package ca.etsmtl.applets.etsmobile;

import java.net.MalformedURLException;

import java.net.URL;
import java.util.ArrayList;

import ca.etsmtl.applets.etsmobile.adapters.NewsListAdapter;
import ca.etsmtl.applets.etsmobile.adapters.NewsListAdapter.Holder;
import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.preferences.NewsListPreferences;
import ca.etsmtl.applets.etsmobile.tools.db.NewsDB;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLAppletsHandler;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLParser;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLRssFbTwitterHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class NewsListActivity extends Activity{
	
	private final static String RSS_ETS_FEED = "http://www.etsmtl.ca/fils-rss?rss=NouvellesRSS";
	private final static String FACEBOOK_FEED = "http://www.facebook.com/feeds/page.php?id=8632204375&format=rss20";
	private final static String TWITTER_FEED = "http://api.twitter.com/1/statuses/user_timeline.rss?screen_name=etsmtl";
	public final static String RSS_ETS = "rssETS";
	public final static String FACEBOOK = "facebook";
	public final static String TWITTER = "twitter";
	
	// Contient la liste de nouvelles.
	private ArrayList<News> newsList = new ArrayList<News>();
	
	private NewsListAdapter newsAdapter;
	private static NewsDB newsDB = null;
	private ArrayList<Object> newsToTransferToDB = null;
	private ListView listView = null;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.news_list_view);
		if(newsDB == null){
			newsDB = NewsDB.getInstance(this);
		}
		
		initializeListView();
		
		// La methode getLastNonConfigurationInstance est appellée à chaque
		// fois que l'usager change l'orientation de l'écran.
		newsToTransferToDB  = (ArrayList<Object>) getLastNonConfigurationInstance();
		
		// On vérifie si l'orientation a changé. Dans le fond si on vient juste de
		// lancer l'acitivé on va recevoir un "null" et on va aller voir
		// s'il y a des nouvelles nouvelles. Si l'usager a changé l'orientation
		// on va recevoir un objet X ce qui veut dire qu'il ne faut plus chercher
		// faire un query au rss...
		if(newsToTransferToDB == null){
			new QueryNewsFromBackground().execute(null);
		}
		
		ImageButton btnHome = (ImageButton)findViewById(R.id.base_list_home_btn);
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Button btnSources = (Button)findViewById(R.id.base_list_source_btn);
		btnSources.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), NewsListPreferences.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onResume() {
		refreshListView();
		super.onResume();
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return newsToTransferToDB;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Preferences");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case 0:
			intent = new Intent(getApplicationContext(), NewsListPreferences.class);
			break;
		default:
			break;
		}
		
		if(intent != null){
			startActivity(intent);
		}
		
		return true;
	}
	
	private class QueryNewsFromBackground extends AsyncTask<String, Integer, Integer>{
		
		
		
		@Override
		protected Integer doInBackground(String... params) {
			
			int result = -1;
			
			XMLAppletsHandler handler = new XMLRssFbTwitterHandler(NewsListActivity.this, RSS_ETS);
			
			try {
				XMLParser xml = new XMLParser(new URL(RSS_ETS_FEED), handler, NewsListActivity.this);
				newsToTransferToDB = xml.getParsedNews();
				addNewsToDB(newsToTransferToDB);
				
				handler = new XMLRssFbTwitterHandler(NewsListActivity.this, FACEBOOK);
				xml = new XMLParser(new URL(FACEBOOK_FEED), handler, NewsListActivity.this);
				newsToTransferToDB = xml.getParsedNews();
				addNewsToDB(newsToTransferToDB);

				handler = new XMLRssFbTwitterHandler(NewsListActivity.this, TWITTER);
				xml = new XMLParser(new URL(TWITTER_FEED), handler, NewsListActivity.this);
				newsToTransferToDB = xml.getParsedNews();
				addNewsToDB(newsToTransferToDB);
				
				result = 1;
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return result;
		}
		
		
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(result == 1){
				refreshListView();
			}
		}
		
	}
	
	private void addNewsToDB(ArrayList<Object> newNewsList){
		if(newNewsList != null){
			if(newNewsList.size() > 0){
				for (Object object : newNewsList) {
					News n = (News) object;
					newsDB.insertNews(n.getTitle(), n.getPubDate(), n.getDescription(), n.getGuid(), n.getSource());
				}
			}
		}
	}
	
    private void initializeListView(){
    		
		// On crée l'adapter qui permets de remplir la listview de "views"
		newsAdapter = new NewsListAdapter(getApplicationContext(), R.layout.news_list_item, newsList);
		
		// On va chercher la listView dans le layout
		listView = (ListView)findViewById(R.id.listView);
		
		// On lui associe un adapter
		listView.setAdapter(newsAdapter);
		
		// Ce qui arrive quand on click sur les éléments...
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				// On crée un nouveau intent qui va nous permettre de lancer
				// la nouvelle activity
				Intent intent = new Intent(getApplicationContext(), SingleNewsActivity.class);
				
				Holder holder = (Holder)arg1.getTag();
				intent.putExtra("guid", holder.getGuid());
				
				// On lance l'intent qui va créer la nouvelle activity.			
				startActivity(intent);
				
			}
		});
    }

    private void refreshListView(){
		SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ArrayList<String> sources = new ArrayList<String>();
		
		if(sharePref.getBoolean(RSS_ETS, true)){
			sources.add(RSS_ETS);
		}
		
		if(sharePref.getBoolean(FACEBOOK, true)){
			sources.add(FACEBOOK);
		}
		
		if(sharePref.getBoolean(TWITTER, true)){
			sources.add(TWITTER);
		}
		
		newsList.clear();
		
		if(sources.size() != 0){
			newsList.addAll(newsDB.getNewsBySource(sources));
		}
		
		sources.clear();
		
		if(newsAdapter != null){
			newsAdapter.notifyDataSetChanged();
		}
    }
 
  
}
