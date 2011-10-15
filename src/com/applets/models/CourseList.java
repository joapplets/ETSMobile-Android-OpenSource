package com.applets.models;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class CourseList extends ArrayList<Course> {
    private static final long serialVersionUID = -1414117376761801185L;
    private String url;
    private URL feed_list;

    // XML elements
    private static String ROOT_TAG = "cours";
    private static String FEED_TAG = "cour";

    public CourseList(String url, Context context) {
	this.url = url;

	getFeeds();
    }

    /**
     * use DB or web services TODO db adapter
     */
    private void getFeeds() {
	getFeedsFromServer();
    }

    private void getFeedsFromServer() {

	XmlPullParser parser = Xml.newPullParser();
	try {
	    feed_list = new URL(url);

	    // auto-detect the encoding from the stream
	    parser.setInput(feed_list.openStream(), null);

	    int eventType = parser.getEventType();
	    Course currentFeed = null;
	    boolean done = false;

	    while (eventType != XmlPullParser.END_DOCUMENT && !done) {

		String name = null;
		switch (eventType) {
		case XmlPullParser.START_DOCUMENT:
		    break;
		case XmlPullParser.START_TAG:
		    name = parser.getName();
		    if (name.equalsIgnoreCase(FEED_TAG)) {
			currentFeed = new Course();
			currentFeed.setName(parser.getAttributeValue(null,
				"name"));
		    }
		    break;
		case XmlPullParser.END_TAG:
		    name = parser.getName();
		    if (name.equalsIgnoreCase(FEED_TAG) && currentFeed != null) {
			this.add(currentFeed);
		    } else if (name.equalsIgnoreCase(ROOT_TAG)) {
			done = true;
		    }
		    break;
		}
		eventType = parser.next();
	    }
	} catch (IOException ioe) {
	    Log.d("com.applets.modes.ProgramList", ioe.getMessage());
	} catch (XmlPullParserException e) {
	    Log.d("com.applets.models.ProgramList", e.getMessage());
	}
    }

}
