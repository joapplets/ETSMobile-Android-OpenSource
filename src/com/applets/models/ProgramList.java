package com.applets.models;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.applets.utils.xml.IAsyncTaskListener;
import com.applets.utils.xml.XMLParserTask;

@SuppressWarnings("serial")
public class ProgramList extends ArrayList<Program> implements ContentHandler {

    // XML elements
    private static String TAG = "programme";
    private Program currentProgramme;

    public ProgramList() {
    }

    @Override
    public void characters(final char[] ch, final int start, final int length)
	    throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void endDocument() throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void endElement(final String uri, final String localName,
	    final String qName) throws SAXException {
	this.add(currentProgramme);
    }

    @Override
    public void endPrefixMapping(final String prefix) throws SAXException {
	// TODO Auto-generated method stub

    }

    public void execute(final String url, final IAsyncTaskListener listener) {
	new XMLParserTask(url, this, listener).execute();
    }

    @Override
    public void ignorableWhitespace(final char[] ch, final int start,
	    final int length) throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void processingInstruction(final String target, final String data)
	    throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void setDocumentLocator(final Locator locator) {
	// TODO Auto-generated method stub

    }

    @Override
    public void skippedEntity(final String name) throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void startDocument() throws SAXException {
	// TODO Auto-generated method stub

    }

    @Override
    public void startElement(final String uri, final String localName,
	    final String qName, final Attributes atts) throws SAXException {
	if (localName.equalsIgnoreCase(ProgramList.TAG)) {

	    currentProgramme = new Program();
	    currentProgramme.setId(Integer.parseInt(atts.getValue("id")));
	    currentProgramme.setName(atts.getValue("name"));
	    currentProgramme.setDescription(atts.getValue("description"));
	    currentProgramme.setShortName(atts.getValue("shortname"));
	    currentProgramme.setUrl(atts.getValue("url"));
	    currentProgramme.setUrlPdf(atts.getValue("url_pdf"));
	}
    }

    @Override
    public void startPrefixMapping(final String prefix, final String uri)
	    throws SAXException {
    }

}
