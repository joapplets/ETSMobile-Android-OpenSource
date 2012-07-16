package ca.etsmtl.applets.etsmobile.tools.xml;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ca.etsmtl.applets.etsmobile.models.ObservableBundle;

public abstract class XMLAppletsHandler extends DefaultHandler {

	protected ObservableBundle bundle;
	protected StringBuffer buffer;

	public XMLAppletsHandler(ObservableBundle b) {
		this.bundle = b;
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String lecture = new String(ch, start, length);
		if (buffer != null)
			buffer.append(lecture);
	}

}
