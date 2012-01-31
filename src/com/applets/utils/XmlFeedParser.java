package com.applets.utils;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.applets.models.Model;
import com.applets.models.News;

public class XmlFeedParser extends BasicFeedParser {
	public XmlFeedParser(final String feedUrl) {
		super(feedUrl);
	}

	@Override
	public ArrayList<Model> parse() {
		ArrayList<Model> articles = null;
		final XmlPullParser parser = Xml.newPullParser();
		try {
			// auto-detect the encoding from the stream
			parser.setInput(getInputStream(), null);
			int eventType = parser.getEventType();
			News currentArticle = null;
			boolean done = false;
			while ((eventType != XmlPullParser.END_DOCUMENT) && !done) {
				String name = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					articles = new ArrayList<Model>();
					break;
				case XmlPullParser.START_TAG:
					name = parser.getName();
					if (name.equalsIgnoreCase(BasicFeedParser.ITEM)) {
						currentArticle = new News();
					} else if (currentArticle != null) {
						if (name.equalsIgnoreCase(BasicFeedParser.LINK)) {
							currentArticle.setUrl(parser.nextText());
						} else if (name.equalsIgnoreCase(BasicFeedParser.TITLE)) {
							currentArticle.setTitle(parser.nextText());
						} else if (name
								.equalsIgnoreCase(BasicFeedParser.DESCRIPTION)) {
							currentArticle.setDescription(parser.nextText());
						} else if (name.equalsIgnoreCase(BasicFeedParser.LINK)) {
							currentArticle.setUrl(parser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					name = parser.getName();
					if (name.equalsIgnoreCase(BasicFeedParser.ITEM)
							&& (currentArticle != null)) {
						articles.add(currentArticle);
					} else if (name.equalsIgnoreCase(BasicFeedParser.CHANNEL)) {
						done = true;
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		return articles;
	}
}