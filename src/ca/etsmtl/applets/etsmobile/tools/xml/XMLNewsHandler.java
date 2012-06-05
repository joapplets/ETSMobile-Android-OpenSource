package ca.etsmtl.applets.etsmobile.tools.xml;

import java.util.ArrayList;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.tools.db.NewsDB;

import android.content.Context;

public abstract class XMLNewsHandler extends DefaultHandler{
	
	protected StringBuffer buffer;
	protected ArrayList<Object> newNews;
	protected Context context;
	protected NewsDB newsDB;
	protected ArrayList<News> news;
	
	public XMLNewsHandler(Context context){
		this.context = context;
	}
	
	// On instantie la liste.
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		newsDB = NewsDB.getInstance(context);
		news = newsDB.getAllNews();
		newNews = new ArrayList<Object>();
	}
	
	 public void characters(char[] ch,int start, int length) throws SAXException{
		 String lecture = new String(ch,start,length);
		 if(buffer != null) buffer.append(lecture);
	 }
	 
	 public ArrayList<Object> getData() {
		return newNews;
	}
}
