package com.applets.models;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import android.app.Activity;

import com.applets.utils.XMLParser;

@SuppressWarnings("serial")
public class NewsList extends ArrayList<News> implements ContentHandler {

    // XML elements
//    private static String ROOT_TAG = "cours";
    private static String NEWS_TAG = "news";
    private News currentNews;

    public NewsList(String url, Activity ctx) {
	XMLParser parser = new XMLParser(url, this);
	parser.execute();
    }

//    private void getNews(String url) {

	// XmlPullParser parser = Xml.newPullParser();
	// try {
	//
	// // auto-detect the encoding from the stream
	// parser.setInput(new URL(url).openStream(), null);
	// int eventType = parser.getEventType();
	// News currentNews = null;
	//
	// boolean done = false;
	//
	// while (eventType != XmlPullParser.END_DOCUMENT && !done) {
	// String name = null;
	// switch (eventType) {
	// case XmlPullParser.START_DOCUMENT:
	// break;
	// case XmlPullParser.START_TAG:
	// name = parser.getName();
	// if (name.equalsIgnoreCase(NEWS_TAG)) {
	// currentNews = new News();
	// currentNews.setTitle(parser.getAttributeValue(null,
	// "name"));
	// currentNews.setUrl(parser
	// .getAttributeValue(null, "url"));
	// currentNews.set_id(parser.getAttributeValue(null,
	// "id"));
	// currentNews.setImage(parser.getAttributeValue(null,
	// "image"));
	// currentNews.setCreator(parser.getAttributeValue(null,
	// "creator"));
	// currentNews.setFeedId(parser.getAttributeValue(null,
	// "feed_id"));
	// currentNews.setPubDate(parser.getAttributeValue(null,
	// "pubDate"));
	// } else if (currentNews != null) {
	//
	// }
	// break;
	// case XmlPullParser.END_TAG:
	// name = parser.getName();
	// if (name.equalsIgnoreCase(NEWS_TAG) && currentNews != null) {
	// this.add(currentNews);
	// } else if (name.equalsIgnoreCase(ROOT_TAG)) {
	// done = true;
	// }
	// break;
	// }
	// eventType = parser.next();
	// }
	// } catch (XmlPullParserException e) {
	// // throw new RuntimeException(e);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
//    }

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
	this.add(currentNews);
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
	currentNews = new News();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
	    Attributes atts) throws SAXException {
	if (localName.equals(NEWS_TAG)) {
	    String title, url, id, feed_id, image, pubDate, creator;

	    title = atts.getValue("name");
	    url = atts.getValue("url");
	    id = atts.getValue("id");
	    feed_id = atts.getValue("feed_id");
	    image = atts.getValue("image");
	    pubDate = atts.getValue("pubDate");
	    creator = atts.getValue("creator");

	    currentNews.setTitle(title);
	    currentNews.setUrl(url);
	    currentNews.setImage(image);
	    currentNews.set_id(id);
	    currentNews.setCreator(creator);
	    currentNews.setFeedId(feed_id);
	    currentNews.setPubDate(pubDate);
	}
    }

    @Override
    public void startPrefixMapping(String prefix, String uri)
	    throws SAXException {
	// TODO Auto-generated method stub

    }

}
