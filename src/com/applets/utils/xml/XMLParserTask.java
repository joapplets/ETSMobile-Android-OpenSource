package com.applets.utils.xml;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.util.Xml.Encoding;

/**
 * AsyncTask to retreive the content of an xml over the internet/3G
 * 
 * @author Philippe David
 * 
 */
public class XMLParserTask extends AsyncTask<String, Integer, Void> {

	/**
	 * Names of the RSS tags
	 */
	static final String CHANNEL = "channel";
	static final String COMMENTS = "comments";
	static final String DESCRIPTION = "description";
	static final String ITEM = "item";
	static final String LINK = "link";
	static final String TITLE = "title";
	private final ContentHandler handler;
	private final IAsyncTaskListener listener;
	private final String url;

	public XMLParserTask(final String url, final ContentHandler handler,
			final IAsyncTaskListener listener) {
		this.handler = handler;
		this.url = url;
		this.listener = listener;
	}

	@Override
	protected Void doInBackground(final String... params) {
		try {
			Xml.parse(new URL(url).openStream(), Encoding.UTF_8, handler);
			publishProgress(0);
		} catch (final MalformedURLException e) {
			cancel(true);
			Log.e(XMLParserTask.class.getName(), e.getMessage());
		} catch (final IOException e) {
			Log.e(XMLParserTask.class.getName(), e.getMessage());
		} catch (final SAXException e) {
			Log.e(XMLParserTask.class.getName(), e.getMessage());
		}
		return null;
	}

	@Override
	protected void onPostExecute(final Void result) {
		listener.onPostExecute();
	}

}