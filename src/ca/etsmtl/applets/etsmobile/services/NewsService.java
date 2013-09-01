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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;
import ca.etsmtl.applets.etsmobile.providers.ETSMobileContentProvider;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLNewsParser;

public class NewsService extends Service implements Observer {

	private class Fetcher extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(final Void... params) {
			try {
				final SAXParser saxParser = SAXParserFactory.newInstance()
						.newSAXParser();
				InputStream stream;

				final String[] projection = { NewsTableHelper.NEWS_GUID };
				final String[] rss = { NewsService.RSS_ETS };
				final String[] fb = { NewsService.FACEBOOK };
//				final String[] tw = { NewsService.TWITTER };
				final String[] in = { NewsService.INTERFACE };

				// rss cached news
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

				// rss ets
				URL url = new URL(NewsService.RSS_ETS_FEED);
				stream = url.openStream();
				XMLNewsParser xmlParser = new XMLNewsParser(
						NewsService.RSS_ETS, guids, bundle);
				saxParser.parse(stream, xmlParser);
				stream.close();

				// fb cached news
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

				// fb xml feed
				url = new URL(NewsService.FACEBOOK_FEED);
				stream = url.openStream();
				xmlParser = new XMLNewsParser(NewsService.FACEBOOK, guids,
						bundle);
				saxParser.parse(stream, xmlParser);
				stream.close();

				// twitter cached news
//				cursor = getContentResolver().query(
//						ETSMobileContentProvider.CONTENT_URI_NEWS, projection,
//						null, tw, null);
//				guids = new ArrayList<String>();
//				if (cursor.moveToFirst()) {
//					do {
//						guids.add(cursor.getString(cursor
//								.getColumnIndex(NewsTableHelper.NEWS_GUID)));
//					} while (cursor.moveToNext());
//				}
//				cursor.close();

				// rss twitter
//				url = new URL(NewsService.TWITTER_FEED);
//				stream = url.openStream();
//				xmlParser = new XMLNewsParser(NewsService.TWITTER, guids,
//						bundle);
//				saxParser.parse(stream, xmlParser);
//				stream.close();

				// interface cached news
				cursor = getContentResolver().query(
						ETSMobileContentProvider.CONTENT_URI_NEWS, projection,
						null, in, null);
				guids = new ArrayList<String>();
				if (cursor.moveToFirst()) {
					do {
						guids.add(cursor.getString(cursor
								.getColumnIndex(NewsTableHelper.NEWS_GUID)));
					} while (cursor.moveToNext());
				}
				cursor.close();

				// rss interface
				url = new URL(NewsService.INTERFACE);
				stream = url.openStream();
				xmlParser = new XMLNewsParser(NewsService.INTERFACE, guids,
						bundle);
				saxParser.parse(stream, xmlParser);
				stream.close();

			} catch (final MalformedURLException e) {
				Log.e(NewsService.TAG, e.toString());
			} catch (final ParserConfigurationException e) {
				Log.e(NewsService.TAG, e.toString());
			} catch (final SAXException e) {
				Log.e(NewsService.TAG, e.toString());
			} catch (final IOException e) {
				Log.e(NewsService.TAG, e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(final Void result) {
			working = false;
		}

		@Override
		protected void onPreExecute() {
			working = true;
		}

	}

	public class NewsFetcherBinder extends Binder {

		public boolean isWorking() {
			return working;
		}

		public void startFetching() {
			if (!working) {
				new Fetcher().execute();
			}
		}

	}

	private final static String TAG = "ca.etsmtl.applets.etsmobile.services.NewsFetcher";
	private final static String RSS_ETS_FEED = "http://www.etsmtl.ca/fils-rss?rss=NouvellesRSS";
	private final static String FACEBOOK_FEED = "http://www.facebook.com/feeds/page.php?id=8632204375&format=rss20";
//	private final static String TWITTER_FEED = "http://api.twitter.com/1/statuses/user_timeline.rss?screen_name=etsmtl";
	public static final String INTERFACE = "http://interfaceets.wordpress.com/feed/";
	public final static String RSS_ETS = "rssETS";
	public final static String FACEBOOK = "facebook";
	public final static String TWITTER = "twitter";
	private final NewsFetcherBinder binder = new NewsFetcherBinder();

	private boolean working;

	private Document doc;

	private ObservableBundle bundle;

	private News formatNews(final News news) {
		final String source = news.getSource();

		if (source.equals(NewsService.RSS_ETS)) {
			doc = Jsoup.parse(news.getDescription());

			// enlève l'icône fb et twitter en haut du feed
			doc.select("a[href*=http://www.facebook.com/share.php?]").remove();
			doc.select("a[href*=http://api.tweetmeme.com/share?]").remove();

			final Elements images = doc.select("img");
			for (final Element element : images) {
				element.removeAttr("width");
				element.removeAttr("height");
				element.removeAttr("style");
			}
			news.setDescription(doc.html());
		}

		if (source.equals(NewsService.FACEBOOK)) {
			updateLink(news, "http://www.facebook.com", "http://m.facebook.com");
		}

		if (source.equals(NewsService.TWITTER)) {
			updateLink(news, "http://twitter.com", "http://m.twitter.com");
		}

		return news;
	}

	private void insertNewsIntoDB(final News n) {
		getContentResolver().insert(ETSMobileContentProvider.CONTENT_URI_NEWS,
				n.getContentValues());
		getSharedPreferences("dbpref", Context.MODE_PRIVATE).edit()
				.putBoolean("isEmpty", false).commit();
	}

	@Override
	public IBinder onBind(final Intent arg0) {
		return binder;
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags,
			final int startId) {
		bundle = new ObservableBundle();
		bundle.addObserver(this);
		if (!working) {
			new Fetcher().execute();
		}
		return Service.START_NOT_STICKY;
	}

	@Override
	public void update(final Observable observable, final Object object) {
		if (object instanceof News) {
			News n = (News) object;
			n = formatNews(n);
			insertNewsIntoDB(n);
		}
	}

	private void updateLink(final News news, final String old,
			final String updated) {
		String link = news.getLink();
		link = link.replace(old, updated);
		if (link != null) {
			news.setLink(link);
		}
	}
}
