package ca.etsmtl.applets.etsmobile.tools.xml;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public abstract class XMLAppletsHandler extends DefaultHandler{
	
	private final static String TAG = "ca.etsmtl.applets.etsmobile.tools.xml.XMLAppletsHandler";
	protected StringBuffer buffer;

	 public void characters(char[] ch,int start, int length) throws SAXException{
		 String lecture = new String(ch,start,length);
		 if(buffer != null) buffer.append(lecture);
	 }

}
