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
	static final String CHANNEL = "channel";
	static final String PUB_DATE = "pubDate";
	static final String DESCRIPTION = "description";
	static final String ITEM = "item";
	static final String LINK = "link";
	static final String TITLE = "title";
	private News currentNews;

	public NewsList() {
	}

	@Override
	public void characters(final char[] ch, final int start, final int length)
			throws SAXException {
		// TODO Auto-generated method stub
		String s= "";
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {
		if (currentNews != null) {
			this.add(currentNews);
		}
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

		if (localName.equals(NewsList.ITEM)) {
			currentNews = new News();
//			String title, url, id, feed_id, image, pubDate, creator;

//			title = atts.getValue(TITLE);
//			url = atts.getValue(LINK);
//			id = atts.getValue("id");
//			feed_id = atts.getValue("feed_id");
//			image = atts.getValue("image");
//			pubDate = atts.getValue(PUB_DATE);
//			creator = atts.getValue("creator");

//			currentNews.setTitle(title);
//			currentNews.setUrl(url);
//			currentNews.setImage(image);
//			currentNews.setNewsId(id);
//			currentNews.setCreator(creator);
//			currentNews.setFeedId(feed_id);
//			currentNews.setPubDate(pubDate);
		}
	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri)
			throws SAXException {
	}

}
