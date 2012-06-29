package ca.etsmtl.applets.etsmobile.tools.xml;

import java.util.ArrayList;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.tools.db.NewsAdapter;

public abstract class XMLNewsHandler extends DefaultHandler {

	protected StringBuffer buffer;
	protected Context context;
	protected ArrayList<Object> newNews;
	protected ArrayList<News> news;
	protected NewsAdapter newsDB;

	public XMLNewsHandler(final Context context) {
		this.context = context;
	}

	@Override
	public void characters(final char[] ch, final int start, final int length)
			throws SAXException {
		final String lecture = new String(ch, start, length);
		if (buffer != null) {
			buffer.append(lecture);
		}
	}

	public ArrayList<Object> getData() {
		return newNews;
	}

	// On instantie la liste.
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		newsDB = NewsAdapter.getInstance(context);
		news = newsDB.getAllNews();
		newNews = new ArrayList<Object>();
	}
}
