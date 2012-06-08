package ca.etsmtl.applets.etsmobile;

import java.io.IOException;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import ca.etsmtl.applets.etsmobile.adapters.NewsListAdapter;
import ca.etsmtl.applets.etsmobile.adapters.NewsListAdapter.Holder;
import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.preferences.NewsListPreferences;
import ca.etsmtl.applets.etsmobile.tools.db.NewsAdapter;
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

public class NewsListActivity extends Activity implements AnimationListener, OnClickListener{
	
	private final static String RSS_ETS_FEED = "http://www.etsmtl.ca/fils-rss?rss=NouvellesRSS";
	private final static String FACEBOOK_FEED = "http://www.facebook.com/feeds/page.php?id=8632204375&format=rss20";
	private final static String TWITTER_FEED = "http://api.twitter.com/1/statuses/user_timeline.rss?screen_name=etsmtl";
	private final static long REFRESHINTERVAL = 180000; // 30 minutes
	public final static String RSS_ETS = "rssETS";
	public final static String FACEBOOK = "facebook";
	public final static String TWITTER = "twitter";
	
	// Contient la liste de nouvelles.
	private ArrayList<News> newsList = new ArrayList<News>();
	
	private NewsListAdapter newsAdapter;
	private static NewsAdapter newsDB = null;
	private ListView listView = null;
	private SharedPreferences newsPreferences = null;
	private SharedPreferences timerPreferences = null;
	private boolean footerVisible = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.news_list_view);
		newsDB = NewsAdapter.getInstance(this);
		
		initializeListView();
		
		// La pref qui indique la dernière fois que les nouvelles ont été mises à jour
		timerPreferences = getSharedPreferences("timerPreferences", MODE_PRIVATE);
		
		// on va chercher le timestamp de la dernière màj (en mili sec)
		long lastUpdate = Long.valueOf(timerPreferences.getString("lastUpdate", "0"));
		Calendar calendar = Calendar.getInstance();
		
		// l'heure actuelle 
		long currentTime = calendar.getTime().getTime();
		
		// Si c'est la première fois qu'on utilise l'app on load et on mets à jour
		// le timestamp. Sinon on check le timestamp, si ça fait plus que "REFRESHINTEVAL"
		// qu'on a pas fetché des nouvelles on fetch.
		if(lastUpdate == 0){
			timerPreferences.edit().putString("lastUpdate", String.valueOf(currentTime)).commit();
			new QueryNewsFromBackground().execute();
		}else{
			if ((lastUpdate + REFRESHINTERVAL) <= currentTime){
				new QueryNewsFromBackground().execute();
				timerPreferences.edit().putString("lastUpdate", String.valueOf(currentTime)).commit();
			}
		}
		
		ImageButton btnHome = (ImageButton)findViewById(R.id.base_list_home_btn);
		btnHome.setOnClickListener(this);
		
		Button btnSources = (Button)findViewById(R.id.base_list_source_btn);
		btnSources.setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume() {
		refreshListView();
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.news_list_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		
		switch (item.getItemId()) {
		case R.id.newsListMenuUpdate:
			new QueryNewsFromBackground().execute();
			break;
		case R.id.newsListMenuPreferences:
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
	
	private class QueryNewsFromBackground extends AsyncTask<Void, Integer, Integer>{
			
		LinearLayout footer = (LinearLayout)findViewById(R.id.listView_footer);
		
		@Override
		protected void onPreExecute() {
			footer.startAnimation(showFooter());
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Void... params) {

			try {
				XMLAppletsHandler handler = new XMLRssFbTwitterHandler(NewsListActivity.this, RSS_ETS);
				XMLParser xml = new XMLParser(new URL(RSS_ETS_FEED), handler, NewsListActivity.this);
				addNewsToDB(xml.getParsedNews());
				
				handler = new XMLRssFbTwitterHandler(NewsListActivity.this, FACEBOOK);
				xml = new XMLParser(new URL(FACEBOOK_FEED), handler, NewsListActivity.this);
				addNewsToDB(xml.getParsedNews());

				handler = new XMLRssFbTwitterHandler(NewsListActivity.this, TWITTER);
				xml = new XMLParser(new URL(TWITTER_FEED), handler, NewsListActivity.this);
				addNewsToDB(xml.getParsedNews());
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);	
				footer.startAnimation(hideFooter());
				refreshListView();			
		}
		
	}
	
	private void addNewsToDB(ArrayList<News> newNewsList){
		if(newNewsList != null){
			if(newNewsList.size() > 0){
				for (News n : newNewsList) {
					newsDB.insertNews(n.getTitle(), n.getPubDate(), n.getDescription(), n.getGuid(), n.getSource());
				}
			}
		}
	}
	
    private void initializeListView(){
    		
		// On va chercher la listView dans le layout
		listView = (ListView)findViewById(R.id.listView);
		
		// On crée l'adapter qui permets de remplir la listview de nouvelles
		newsAdapter = new NewsListAdapter(getApplicationContext(), R.layout.news_list_item, newsList);
		
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
    	newsPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		ArrayList<String> sources = new ArrayList<String>();
		
		if(newsPreferences.getBoolean(RSS_ETS, true)){
			sources.add(RSS_ETS);
		}
		
		if(newsPreferences.getBoolean(FACEBOOK, true)){
			sources.add(FACEBOOK);
		}
		
		if(newsPreferences.getBoolean(TWITTER, true)){
			sources.add(TWITTER);
		}
		
		newsList.clear();
		
		if(sources.size() > 0){
			newsList.addAll(newsDB.getNewsBySource(sources));
		}
		
		sources.clear();
		
		if(newsAdapter != null){
			newsAdapter.notifyDataSetChanged();
		}
    }
    
    private Animation hideFooter(){
		Animation animation = new TranslateAnimation(0, 0, 0, 40);
		animation.setDuration(500);
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		animation.setAnimationListener(this);
		footerVisible = false;
		return animation;
    }
    
    private Animation showFooter(){
    	LinearLayout footer = (LinearLayout)findViewById(R.id.listView_footer);
		footer.setVisibility(View.VISIBLE);
		Animation animation = new TranslateAnimation(0, 0, 40, 0);
		animation.setDuration(500);
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		animation.setAnimationListener(this);
		footerVisible = true;
		return animation;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_list_home_btn:
			finish();
			break;
		case R.id.base_list_source_btn:
			Intent intent = new Intent(getApplicationContext(), NewsListPreferences.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onAnimationRepeat(Animation arg0) {}

	@Override
	public void onAnimationStart(Animation arg0) {}

	@Override
	public void onAnimationEnd(Animation arg0) {
		if(!footerVisible){
	    	LinearLayout footer = (LinearLayout)findViewById(R.id.listView_footer);
			footer.setVisibility(View.GONE);
		}
	}
	
}
