package com.applets.models;

import java.net.URL;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import android.content.Context;

import com.applets.utils.xml.IAsyncTaskListener;
import com.applets.utils.xml.XMLParserTask;

public class CourseList extends ArrayList<Course> implements ContentHandler {

    private String url;
    private URL feed_list;
    private Course currentCours;
    private IAsyncTaskListener listener;
    
    // XML elements
    private static String FEED_TAG = "cour";
    

    public CourseList(String url, Context context, IAsyncTaskListener listener) {
	this.url = url;
	this.listener = listener;
	getFeeds();
    }

    /**
     * use DB or web services TODO db adapter
     */
    private void getFeeds() {
	getFeedsFromServer();
    }

    private void getFeedsFromServer() {
	new XMLParserTask(url, this, listener).execute();
//	XmlPullParser parser = Xml.newPullParser();
//	try {
//	    feed_list = new URL(url);
//
//	    // auto-detect the encoding from the stream
//	    parser.setInput(feed_list.openStream(), null);
//
//	    int eventType = parser.getEventType();
//	    Course currentFeed = null;
//	    boolean done = false;
//
//	    while (eventType != XmlPullParser.END_DOCUMENT && !done) {
//
//		String name = null;
//		switch (eventType) {
//		case XmlPullParser.START_DOCUMENT:
//		    break;
//		case XmlPullParser.START_TAG:
//		    name = parser.getName();
//		    if (name.equalsIgnoreCase(FEED_TAG)) {
//			currentFeed = new Course();
//			currentFeed.setName(parser.getAttributeValue(null,
//				"name"));
//		    }
//		    break;
//		case XmlPullParser.END_TAG:
//		    name = parser.getName();
//		    if (name.equalsIgnoreCase(FEED_TAG) && currentFeed != null) {
//			this.add(currentFeed);
//		    } else if (name.equalsIgnoreCase(ROOT_TAG)) {
//			done = true;
//		    }
//		    break;
//		}
//		eventType = parser.next();
//	    }
//	} catch (IOException ioe) {
//	    Log.d("com.applets.modes.ProgramList", ioe.getMessage());
//	} catch (XmlPullParserException e) {
//	    Log.d("com.applets.models.ProgramList", e.getMessage());
//	}
    }

    @Override
    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void endDocument() throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	this.add(currentCours);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length)
	    throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void processingInstruction(String target, String data)
	    throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void setDocumentLocator(Locator locator) {
	// TODO Auto-generated method stub

    }

    @Override
    public void skippedEntity(String name) throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void startDocument() throws SAXException {
	this.clear();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
	    Attributes atts) throws SAXException {
	if (localName.equalsIgnoreCase(FEED_TAG)) {
	    currentCours = new Course();
	    currentCours.setName(atts.getValue("name"));
	}

    }

    @Override
    public void startPrefixMapping(String prefix, String uri)
	    throws SAXException {
	// TODO Auto-generated method stub

    }

}
