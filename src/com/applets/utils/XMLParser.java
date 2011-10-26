package com.applets.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Xml;
import android.util.Xml.Encoding;

/**
 * AsyncTask to retreive the content of an xml over the internet/3G
 * 
 * @author Philippe David
 * 
 */
public class XMLParser extends AsyncTask<String, Void, Void> {

    /**
     * Names of the RSS tags
     */
    static final String CHANNEL = "channel";
    static final String ITEM = "item";
    static final String TITLE = "title";
    static final String COMMENTS = "comments";
    static final String DESCRIPTION = "description";
    static final String LINK = "link";
    private ContentHandler handler;
    private String url;

    public XMLParser(String url, ContentHandler handler) {
	this.handler = handler;
	this.url = url;
    }

    @Override
    protected Void doInBackground(String... params) {
	try {
	    Xml.parse(new URL(url).openStream(), Encoding.ISO_8859_1, handler);
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (SAXException e) {
	    e.printStackTrace();
	}
	return null;
    }

    // public ArrayList<Model> parse() {
    // ArrayList<Model> data = null;
    // XmlPullParser parser = Xml.newPullParser();
    // try {
    // // auto-detect the encoding from the stream
    // parser.setInput(this.getInputStream(), null);
    // int eventType = parser.getEventType();
    // News currentArticle = null;
    // boolean done = false;
    // while (eventType != XmlPullParser.END_DOCUMENT && !done) {
    // String name = null;
    // switch (eventType) {
    // case XmlPullParser.START_DOCUMENT:
    // data = new ArrayList<Model>();
    // break;
    // case XmlPullParser.START_TAG:
    // name = parser.getName();
    // if (name.equalsIgnoreCase(ITEM)) {
    // currentArticle = new News();
    // } else if (currentArticle != null) {
    // if (name.equalsIgnoreCase(LINK)) {
    // currentArticle.setUrl(parser.nextText());
    // } else if (name.equalsIgnoreCase(TITLE)) {
    // currentArticle.setTitle(parser.nextText());
    // } else if (name.equalsIgnoreCase(DESCRIPTION)) {
    // currentArticle.setDescription(parser.nextText());
    // } else if (name.equalsIgnoreCase(LINK)) {
    // currentArticle.setUrl(parser.nextText());
    // }
    // }
    // break;
    // case XmlPullParser.END_TAG:
    // name = parser.getName();
    // if (name.equalsIgnoreCase(ITEM) && currentArticle != null) {
    // data.add(currentArticle);
    // } else if (name.equalsIgnoreCase(CHANNEL)) {
    // done = true;
    // }
    // break;
    // }
    // eventType = parser.next();
    // }
    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // }
    // return data;
    // }

}