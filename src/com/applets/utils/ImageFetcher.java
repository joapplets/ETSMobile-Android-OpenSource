package com.applets.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Fetch an image from the web
 * 
 * @author Philippe David
 * @version 1.0
 */
public class ImageFetcher extends AsyncTask<String, Void, Drawable> {

    @Override
    protected Drawable doInBackground(final String... params) {
	try {
	    final InputStream is = fetch(params[0]);
	    final Drawable d = Drawable.createFromStream(is, "src");
	    return d;
	} catch (final MalformedURLException e) {
	    e.printStackTrace();
	    Log.e("Applets::ImageFetcher::URL", e.getMessage());
	} catch (final IOException e) {
	    Log.e("Applets::ImageFetcher::IO", e.getMessage());
	}
	return null;
    }

    /**
     * Retreive the image from the web
     * 
     * @param address
     *            Valid Http
     * @return InputStream representing the content of the image
     * @throws MalformedURLException
     * @throws IOException
     */
    private InputStream fetch(final String address)
	    throws MalformedURLException, IOException {
	final URL url = new URL(address);
	final InputStream content = (InputStream) url.getContent();
	return content;
    }

}
