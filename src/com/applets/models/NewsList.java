package com.applets.models;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.applets.utils.xml.IAsyncTaskListener;
import com.applets.utils.xml.XMLParserTask;

@SuppressWarnings("serial")
public class NewsList extends ArrayList<News> implements ContentHandler {

    // XML elements
    private static String NEWS_TAG = "news";
    private News currentNews;

    public NewsList() {
    }

    public void execute(String url, IAsyncTaskListener listener) {
	new XMLParserTask(url, this, listener).execute();
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
    }

    @Override
    public void startElement(String uri, String localName, String qName,
	    Attributes atts) throws SAXException {

	if (localName.equals(NEWS_TAG)) {
	    currentNews = new News();
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
    }

}
