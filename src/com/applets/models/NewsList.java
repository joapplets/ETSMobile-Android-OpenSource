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
		this.add(currentNews);
	}

	@Override
	public void endPrefixMapping(final String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	public void execute(final String url, final IAsyncTaskListener listener) {
		new XMLParserTask(url, this, listener).execute();
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
	}

	@Override
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes atts) throws SAXException {

		if (localName.equals(NewsList.NEWS_TAG)) {
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
			currentNews.setNewsId(id);
			currentNews.setCreator(creator);
			currentNews.setFeedId(feed_id);
			currentNews.setPubDate(pubDate);
		}
	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri)
			throws SAXException {
	}


}
