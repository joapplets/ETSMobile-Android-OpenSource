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

	private static final String LINK_END_TAG = ".aspx";
	private static final String HTTP_ETS_HOTDOG_ETSMTL_CA = "http://ets-hotdog.etsmtl.ca/";
	// XML elements
	static final String CHANNEL = "channel";
	static final String PUB_DATE = "pubDate";
	static final String DESCRIPTION = "description";
	static final String ITEM = "item";
	static final String LINK = "link";
	static final String TITLE = "title";
	static final String LANG = "language";
	private StringBuilder builder = new StringBuilder();
	private News currentNews;
	private Channel currentChannel;

	public NewsList() {

	}

	@Override
	public void characters(final char[] ch, final int start, final int length)
			throws SAXException {
		builder.append(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {

	}

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {

		if (currentNews != null) {
			final String elementValue = builder.toString().trim();
			if (localName.equalsIgnoreCase(TITLE)) {
				currentNews.setName(elementValue);
			} else if (localName.equalsIgnoreCase(LINK)) {
				currentNews.setUrl(elementValue);
			} else if (localName.equalsIgnoreCase(DESCRIPTION)) {
				final String d = elementValue.substring(0,
						elementValue.indexOf("<br>"));

				currentNews.setDescription(d);

				final int start = elementValue
						.indexOf(HTTP_ETS_HOTDOG_ETSMTL_CA);
				if (start != -1) {
					final int end = elementValue.indexOf(LINK_END_TAG, start);
					currentNews.setImage(elementValue.substring(start+18, end));
				}

			} else if (localName.equalsIgnoreCase(PUB_DATE)) {
				currentNews.setPubDate(elementValue);
			} else if (localName.equalsIgnoreCase(ITEM)) {
				this.add(currentNews);
			}
		}
		builder.setLength(0);
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
		} else if (localName.equalsIgnoreCase(CHANNEL)) {
			currentChannel = new Channel();
		}
	}

	@Override
	public void startPrefixMapping(final String prefix, final String uri)
			throws SAXException {
	}

	public Channel getChannel() {
		return currentChannel;

	}

}
