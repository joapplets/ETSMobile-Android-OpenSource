package ca.etsmtl.applets.etsmobile.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;
import ca.etsmtl.applets.etsmobile.providers.ETSMobileContentProvider;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLNewsParser;

public class NewsService extends Service implements Observer {

	private final static String TAG = "NewsFetcher";

	private final static String RSS_ETS_FEED = "http://www.etsmtl.ca/fils-rss?rss=NouvellesRSS";
	private final static String FACEBOOK_FEED = "http://www.facebook.com/feeds/page.php?id=8632204375&format=rss20";
	private final static String TWITTER_FEED = "http://api.twitter.com/1/statuses/user_timeline.rss?screen_name=etsmtl";
	public final static String RSS_ETS = "rssETS";
	public final static String FACEBOOK = "facebook";
	public final static String TWITTER = "twitter";
	private NewsFetcherBinder binder = new NewsFetcherBinder();
	private boolean working;

	private ObservableBundle bundle;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		bundle = new ObservableBundle();
		bundle.addObserver(this);
		if (!working) {
			new Fetcher().execute();
		}
		return START_NOT_STICKY;
	}

	private class Fetcher extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			working = true;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				SAXParser saxParser = SAXParserFactory.newInstance()
						.newSAXParser();
				InputStream stream;

				String[] projection = { NewsTableHelper.NEWS_GUID };
				String[] rss = { RSS_ETS };
				String[] fb = { FACEBOOK };
				String[] tw = { TWITTER };

				Cursor cursor = getContentResolver().query(
						ETSMobileContentProvider.CONTENT_URI_NEWS, projection,
						null, rss, null);
				ArrayList<String> guids = new ArrayList<String>();
				if (cursor.moveToFirst()) {
					do {
						guids.add(cursor.getString(cursor
								.getColumnIndex(NewsTableHelper.NEWS_GUID)));
					} while (cursor.moveToNext());
				}
				cursor.close();

				URL url = new URL(RSS_ETS_FEED);
				stream = url.openStream();
				XMLNewsParser newsParser = new XMLNewsParser(RSS_ETS, guids,
						bundle);
				saxParser.parse(stream, newsParser);
				stream.close();

				cursor = getContentResolver().query(
						ETSMobileContentProvider.CONTENT_URI_NEWS, projection,
						null, fb, null);
				guids = new ArrayList<String>();
				if (cursor.moveToFirst()) {
					do {
						guids.add(cursor.getString(cursor
								.getColumnIndex(NewsTableHelper.NEWS_GUID)));
					} while (cursor.moveToNext());
				}
				cursor.close();
				url = new URL(FACEBOOK_FEED);
				stream = url.openStream();
				newsParser = new XMLNewsParser(FACEBOOK, guids, bundle);
				saxParser.parse(stream, newsParser);
				stream.close();

				cursor = getContentResolver().query(
						ETSMobileContentProvider.CONTENT_URI_NEWS, projection,
						null, tw, null);
				guids = new ArrayList<String>();
				if (cursor.moveToFirst()) {
					do {
						guids.add(cursor.getString(cursor
								.getColumnIndex(NewsTableHelper.NEWS_GUID)));
					} while (cursor.moveToNext());
				}
				cursor.close();
				url = new URL(TWITTER_FEED);
				stream = url.openStream();
				newsParser = new XMLNewsParser(TWITTER, guids, bundle);
				saxParser.parse(stream, newsParser);
				stream.close();

			} catch (MalformedURLException e) {
				Log.e(TAG, e.toString());
			} catch (ParserConfigurationException e) {
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

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	public class NewsFetcherBinder extends Binder {

		public void startFetching() {
			if (!working) {
				new Fetcher().execute();
			}
		}

		public boolean isWorking() {
			return working;
		}

	}

	@Override
	public void update(Observable observable, Object object) {
		if (object instanceof ContentValues[]) {
			long start = System.currentTimeMillis();
			Log.d(TAG, "start insert");
			getContentResolver().bulkInsert(
					ETSMobileContentProvider.CONTENT_URI_NEWS,
					(ContentValues[]) object);
			long stop = System.currentTimeMillis();
			Log.d(TAG, "end insert : " + (stop - start) + "ms");

		}
	}
}
