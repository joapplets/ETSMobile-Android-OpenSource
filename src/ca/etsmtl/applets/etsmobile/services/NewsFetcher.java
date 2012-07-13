package ca.etsmtl.applets.etsmobile.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;
import ca.etsmtl.applets.etsmobile.providers.NewsListContentProvider;
import ca.etsmtl.applets.etsmobile.tools.db.NewsAdapter;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTable;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLNewsParser;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NewsFetcher extends Service implements Observer{
	
	private final static String TAG = "ca.etsmtl.applets.etsmobile.services.NewsFetcher";
	
	private final static String RSS_ETS_FEED = "http://www.etsmtl.ca/fils-rss?rss=NouvellesRSS";
	private final static String FACEBOOK_FEED = "http://www.facebook.com/feeds/page.php?id=8632204375&format=rss20";
	private final static String TWITTER_FEED = "http://api.twitter.com/1/statuses/user_timeline.rss?screen_name=etsmtl";
	public final static String RSS_ETS = "rssETS";
	public final static String FACEBOOK = "facebook";
	public final static String TWITTER = "twitter";
	private NewsFetcherBinder binder = new NewsFetcherBinder();
	private boolean working;
	
	private NewsAdapter newsAdapter;
	private ObservableBundle bundle;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		newsAdapter = NewsAdapter.getInstance(this);
		bundle = new ObservableBundle();
		bundle.addObserver(this);
		if(!working){
			new Fetcher().execute();
		}
		return START_NOT_STICKY;
	}
		
	private class Fetcher extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			working = true;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
				InputStream stream;
				
				URL url = new URL(RSS_ETS_FEED);
				stream = url.openStream();
				XMLNewsParser newsParser = new XMLNewsParser(RSS_ETS, newsAdapter.getAllGUIDFromSource(RSS_ETS), bundle);
				saxParser.parse(stream, newsParser);
				stream.close();
				
				url = new URL(FACEBOOK_FEED);
				stream = url.openStream();
				newsParser = new XMLNewsParser(FACEBOOK, newsAdapter.getAllGUIDFromSource(FACEBOOK), bundle);
				saxParser.parse(stream, newsParser);
				stream.close();
				
				url = new URL(TWITTER_FEED);
				stream = url.openStream();
				newsParser = new XMLNewsParser(TWITTER, newsAdapter.getAllGUIDFromSource(TWITTER), bundle);
				saxParser.parse(stream, newsParser);
				stream.close();
				
			} catch (MalformedURLException e) {
				Log.e(TAG, e.toString());
			}catch (ParserConfigurationException e) {
				Log.e(TAG, e.toString());
			} catch (SAXException e) {
				Log.e(TAG, e.toString());
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			working = false;
		}
		
	}
	
	private void insertNewsIntoDB(News n){
		//newsAdapter.insertNews(n.getTitle(), n.getPubDate().getTime(), n.getDescription(), n.getGuid(), n.getSource());
		ContentValues values = new ContentValues();
		values.put(NewsTable.NEWS_TITLE, n.getTitle());
		values.put(NewsTable.NEWS_DATE, n.getPubDate().getTime());
		values.put(NewsTable.NEWS_DESCRIPTION, n.getDescription());
		values.put(NewsTable.NEWS_GUID, n.getGuid());
		values.put(NewsTable.NEWS_SOURCE, n.getSource());
		getContentResolver().insert(NewsListContentProvider.CONTENT_URI, values);
		getSharedPreferences("dbpref", MODE_PRIVATE).edit().putBoolean("isEmpty", false).commit();
	}
	
	private void downloadImage(String url){
		
	}
	
	private String formatDescription(String description){
		
		return null;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	
	public class NewsFetcherBinder extends Binder{
		
		public void startFetching(){
			if(!working){
				new Fetcher().execute();
			}
		}
		
		public boolean isWorking(){ return working; }
		
	}

	@Override
	public void update(Observable observable, Object object) {
		if(object instanceof News){
			News n = (News)object;
			if(n.getSource().equals(RSS_ETS)){
				n.setDescription(formatDescription(n.getDescription()));
			}
			insertNewsIntoDB(n);
		}
	}
}
