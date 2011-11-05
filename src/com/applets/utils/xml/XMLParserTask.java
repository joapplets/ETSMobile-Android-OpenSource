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
    static final String ITEM = "item";
    static final String TITLE = "title";
    static final String COMMENTS = "comments";
    static final String DESCRIPTION = "description";
    static final String LINK = "link";
    private ContentHandler handler;
    private String url;
    private IAsyncTaskListener listener;

    public XMLParserTask(String url, ContentHandler handler,
	    IAsyncTaskListener listener) {
	this.handler = handler;
	this.url = url;
	this.listener = listener;
    }

    @Override
    protected void onPostExecute(Void result) {
	listener.onPostExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
	try {
	    Xml.parse(new URL(url).openStream(), Encoding.UTF_8, handler);
	    publishProgress(0);
	} catch (MalformedURLException e) {
	    cancel(true);
	    Log.e(XMLParserTask.class.getName(), e.getMessage());
	} catch (IOException e) {
	    Log.e(XMLParserTask.class.getName(), e.getMessage());
	} catch (SAXException e) {
	    Log.e(XMLParserTask.class.getName(), e.getMessage());
	}
	return null;
    }

}