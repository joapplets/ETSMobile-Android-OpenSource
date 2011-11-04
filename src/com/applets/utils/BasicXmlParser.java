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
public abstract class BasicXmlParser implements IParser {

    private final URL url;

    protected BasicXmlParser(String url) {
	try {
	    this.url = new URL(url);
	} catch (MalformedURLException e) {
	    Log.e("APPETS.BASICPARSER::", e.getMessage());
	    throw new RuntimeException();
	}
    }

    protected InputStream getInputStream() {
	try {
	    return url.openConnection().getInputStream();
	} catch (IOException e) {
	    Log.e("APPETS.BASICPARSER::", e.getMessage());
	    throw new RuntimeException();
	}
    }

}
