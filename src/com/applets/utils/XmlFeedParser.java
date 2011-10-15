package com.applets.utils;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.applets.models.Model;
import com.applets.models.News;

public class XmlFeedParser extends BasicFeedParser {
    public XmlFeedParser(String feedUrl) {
	super(feedUrl);
    }

    @Override
    public ArrayList<Model> parse() {
	ArrayList<Model> articles = null;
	XmlPullParser parser = Xml.newPullParser();
	try {
	    // auto-detect the encoding from the stream
	    parser.setInput(this.getInputStream(), null);
	    int eventType = parser.getEventType();
	    News currentArticle = null;
	    boolean done = false;
	    while (eventType != XmlPullParser.END_DOCUMENT && !done) {
		String name = null;
		switch (eventType) {
		case XmlPullParser.START_DOCUMENT:
		    articles = new ArrayList<Model>();
		    break;
		case XmlPullParser.START_TAG:
		    name = parser.getName();
		    if (name.equalsIgnoreCase(ITEM)) {
			currentArticle = new News();
		    } else if (currentArticle != null) {
			if (name.equalsIgnoreCase(LINK)) {
			    currentArticle.setUrl(parser.nextText());
			} else if (name.equalsIgnoreCase(TITLE)) {
			    currentArticle.setTitle(parser.nextText());
			} else if (name.equalsIgnoreCase(DESCRIPTION)) {
			    currentArticle.setDescription(parser.nextText());
			} else if (name.equalsIgnoreCase(LINK)) {
			    currentArticle.setUrl(parser.nextText());
			}
		    }
		    break;
		case XmlPullParser.END_TAG:
		    name = parser.getName();
		    if (name.equalsIgnoreCase(ITEM) && currentArticle != null) {
			articles.add(currentArticle);
		    } else if (name.equalsIgnoreCase(CHANNEL)) {
			done = true;
		    }
		    break;
		}
		eventType = parser.next();
	    }
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
	return articles;
    }
}