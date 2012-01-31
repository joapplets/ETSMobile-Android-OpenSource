package com.applets.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

/**
 * Abstract class for Xml parsing
 * 
 * @author Philippe David
 * 
 */
public abstract class BasicFeedParser implements IFeedParser {

	/**
	 * Names of the xml tags
	 */
	static final String CHANNEL = "channel";
	static final String COMMENTS = "comments";
	static final String DESCRIPTION = "description";
	static final String ITEM = "item";
	static final String LINK = "link";
	static final String TITLE = "title";

	private final URL feedUrl;

	protected BasicFeedParser(final String feedUrl) {
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (final MalformedURLException e) {
			Log.e("APPETS.BASICFEEDPARSER::", e.getMessage());
			throw new RuntimeException();
		}
	}

	protected InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (final IOException e) {
			Log.e("APPETS.BASICFEEDPARSER::", e.getMessage());
			throw new RuntimeException();
		}
	}

}
