package ca.etsmtl.applets.etsmobile.tools.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.content.ContentValues;
import android.util.Log;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;

public class XMLProfileParser extends XMLAbstractHandler {

	private static final String ENTRY_TAG = "infoEtudiantResult";
	private static final String NOM = "nom";
	private static final String PRENOM = "prenom";
	private static final String CODE = "codePerm";
	private static final String SOLDE = "soldeTotal";
	private static final String ERR = "erreur";
	private boolean newEntry;
	private ContentValues values;

	public XMLProfileParser(final ObservableBundle b) {
		super(b);
	}

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {
		// TODO Auto-generated method stub
		if (newEntry) {
			String key = null;
			if (localName.equalsIgnoreCase(XMLProfileParser.NOM)) {
				key = XMLProfileParser.NOM;
			}
			if (localName.equalsIgnoreCase(XMLProfileParser.PRENOM)) {
				key = XMLProfileParser.PRENOM;
			}
			if (localName.equalsIgnoreCase(XMLProfileParser.CODE)) {
				key = XMLProfileParser.CODE;
			}
			if (localName.equalsIgnoreCase(XMLProfileParser.SOLDE)) {
				key = XMLProfileParser.SOLDE;
			}

			if (localName.equalsIgnoreCase(XMLProfileParser.ERR)) {
				key = XMLProfileParser.ERR;
			}

			if (key != null) {
				Log.d("XMLProfileParser", buffer.toString());
				values.put(key, buffer.toString());
			}

			if (localName.equalsIgnoreCase(XMLProfileParser.ENTRY_TAG)) {
				bundle.setContent(values);
				newEntry = false;
			}
			buffer = null;
		}
	}

	@Override
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes attributes)
			throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		buffer = new StringBuffer();
		if (localName.equalsIgnoreCase(XMLProfileParser.ENTRY_TAG)) {
			// Log.d("XMLBottinParser", localName);
			newEntry = true;
			values = new ContentValues();
		}
		Log.d("XMLProfileParser", localName);
	}
}
