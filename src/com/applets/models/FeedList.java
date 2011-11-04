package com.applets.models;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.util.Xml;

import com.applets.utils.FeedDbAdapter;

public class FeedList extends ArrayList<Feed> {

    private static final long serialVersionUID = 1942747371612502138L;
    private URL feed_list;
    private String url;

    // XML elements
    private static String ROOT_TAG = "feeds";
    private static String FEED_TAG = "feed";

    // Database
    private FeedDbAdapter feedAdapter;

    public FeedList(String url, Context context) {
	this.url = url;

	// open the database
	feedAdapter = new FeedDbAdapter(context);
	feedAdapter.open();
	// init the list
	getFeeds();
	feedAdapter.close();
    }

    /**
     * Retreive feed list from internal SQLite Database If the database is empty
     * or out of date, retreive it from the internet.
     */
    private void getFeeds() {
	if (this.size() == 0) {

	    Cursor mCursor = feedAdapter.getAllFeeds();
	    if (mCursor.getCount() == 0) {

		getFeedsFromServer();
	    } else {
		while (mCursor.moveToNext()) {
		    int id = mCursor.getInt(mCursor
			    .getColumnIndex(FeedDbAdapter.KEY_ROWID));
		    String name = mCursor.getString(mCursor
			    .getColumnIndex(FeedDbAdapter.KEY_NAME));
		    String url = mCursor.getString(mCursor
			    .getColumnIndex(FeedDbAdapter.KEY_URL));
		    String image = mCursor.getString(mCursor
			    .getColumnIndex(FeedDbAdapter.KEY_IMAGE));
		    String lang = mCursor.getString(mCursor
			    .getColumnIndex(FeedDbAdapter.KEY_LANG));

		    add(new Feed(id, name, url, image, lang));
		}
	    }
	    mCursor.close();
	}
    }

    /**
     * <<<<<<< HEAD Retreive the list of available rss feed ======= Retrieve the
     * list of available rss feed from the backend >>>>>>>
     * e234fd63b8783f7a331310de5153bddfae7a444e
     */
    private void getFeedsFromServer() {

	XmlPullParser parser = Xml.newPullParser();
	try {
	    feed_list = new URL(url);

	    // auto-detect the encoding from the stream
	    parser.setInput(feed_list.openStream(), null);

	    int eventType = parser.getEventType();
	    Feed currentFeed = null;
	    boolean done = false;

	    while (eventType != XmlPullParser.END_DOCUMENT && !done) {

		String name = null;
		switch (eventType) {
		case XmlPullParser.START_DOCUMENT:
		    break;
		case XmlPullParser.START_TAG:
		    name = parser.getName();
		    if (name.equalsIgnoreCase(FEED_TAG)) {

			currentFeed = new Feed(parser.getAttributeValue(null,"name"),
				parser.getAttributeValue(null, "url"),
				parser.getAttributeValue(null,"image"),
				parser.getAttributeValue(null,"lang"));
			currentFeed.setName(parser.getAttributeValue(null,
				"name"));
			currentFeed.setUrl(parser
				.getAttributeValue(null, "url"));
			currentFeed.setImage(parser.getAttributeValue(null,
				"image"));
			currentFeed.setLang(parser.getAttributeValue(null,
				"lang"));
		    }
		    break;
		case XmlPullParser.END_TAG:
		    name = parser.getName();
		    if (name.equalsIgnoreCase(FEED_TAG) && currentFeed != null) {

			long id = feedAdapter.createFeed(currentFeed);
			currentFeed.setId(id);
			this.add(currentFeed);
		    } else if (name.equalsIgnoreCase(ROOT_TAG)) {
			done = true;
		    }
		    break;
		}
		eventType = parser.next();
	    }
	} catch (IOException ioe) {
	    Log.d("com.applets.Models.FeedList", ioe.getMessage());
	    Log.d("com.applets.Models.FeedList.Xmlparser Url", url);
	} catch (XmlPullParserException e) {
	    Log.d("com.applets.Models.FeedList", e.getMessage());
	    Log.d("com.applets.Models.FeedList.Xmlparser Url", url);
	}
    }
}
