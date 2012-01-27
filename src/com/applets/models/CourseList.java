package com.applets.models;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import android.content.Context;

import com.applets.utils.xml.IAsyncTaskListener;
import com.applets.utils.xml.XMLParserTask;

public class CourseList extends ArrayList<Course> implements ContentHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 440993685670978442L;
	// XML elements
	private static String FEED_TAG = "cour";
	private Course currentCours;
	private final IAsyncTaskListener listener;

	private final String url;

	public CourseList(final String url, final Context context,
			final IAsyncTaskListener listener) {
		this.url = url;
		this.listener = listener;
		getFeeds();
	}

	@Override
	public void characters(final char[] ch, final int start, final int length)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {
		this.add(currentCours);
	}

	@Override
	public void endPrefixMapping(final String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	/**
	 * use DB or web services TODO db adapter
	 */
	private void getFeeds() {
		getFeedsFromServer();
	}

	private void getFeedsFromServer() {
		new XMLParserTask(url, this, listener).execute();
		// XmlPullParser parser = Xml.newPullParser();
		// try {
		// feed_list = new URL(url);
		//
		// // auto-detect the encoding from the stream
		// parser.setInput(feed_list.openStream(), null);
		//
		// int eventType = parser.getEventType();
		// Course currentFeed = null;
		// boolean done = false;
		//
		// while (eventType != XmlPullParser.END_DOCUMENT && !done) {
		//
		// String name = null;
		// switch (eventType) {
		// case XmlPullParser.START_DOCUMENT:
		// break;
		// case XmlPullParser.START_TAG:
		// name = parser.getName();
		// if (name.equalsIgnoreCase(FEED_TAG)) {
		// currentFeed = new Course();
		// currentFeed.setName(parser.getAttributeValue(null,
		// "name"));
		// }
		// break;
		// case XmlPullParser.END_TAG:
		// name = parser.getName();
		// if (name.equalsIgnoreCase(FEED_TAG) && currentFeed != null) {
		// this.add(currentFeed);
		// } else if (name.equalsIgnoreCase(ROOT_TAG)) {
		// done = true;
		// }
		// break;
		// }
		// eventType = parser.next();
		// }
		// } catch (IOException ioe) {
		// Log.d("com.applets.modes.ProgramList", ioe.getMessage());
		// } catch (XmlPullParserException e) {
		// Log.d("com.applets.models.ProgramList", e.getMessage());
		// }
	}

	@Override
	public void ignorableWhitespace(final char[] ch, final int start,
			final int length) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(final String target, final String data)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocumentLocator(final Locator locator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(final String name) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument() throws SAXException {
		clear();
	}

	@Override
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes atts) throws SAXException {
		if (localName.equalsIgnoreCase(CourseList.FEED_TAG)) {
			currentCours = new Course();
			currentCours.setName(atts.getValue("name"));
		}

	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri)
			throws SAXException {
		// TODO Auto-generated method stub

	}

}
