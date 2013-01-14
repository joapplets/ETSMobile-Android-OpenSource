package ca.etsmtl.applets.etsmobile.tools.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.content.ContentValues;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;

public class XMLBottinParser extends XMLAbstractHandler {

    // xml fields
    private static final String COURRIEL = "courriel";
    private static final String DATE_MODIF = "datemodif";
    private static final String EMPLACEMENT = "emplacement";
    private static final String ENTRY_TAG = "FicheEmploye";
    private static final String ID = "id";
    private static final String NOM = "nom";
    private static final String PRENOM = "prenom";
    private static final String SERVICE = "service";
    private static final String TEL_BUREAU = "telbureau";
    private static final String TITRE = "titre";

    private boolean newEntry;
    private ContentValues values;
    private final List<ContentValues> cvalues = new ArrayList<ContentValues>();

    public XMLBottinParser(final ObservableBundle b) {
	super(b);
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName)
	    throws SAXException {
	if (newEntry) {
	    String key = null;
	    if (localName.equalsIgnoreCase(XMLBottinParser.ID)) {
		key = "ets_id";
	    }
	    if (localName.equalsIgnoreCase(XMLBottinParser.NOM)) {
		key = XMLBottinParser.NOM;
	    }
	    if (localName.equalsIgnoreCase(XMLBottinParser.PRENOM)) {
		key = XMLBottinParser.PRENOM;
	    }
	    if (localName.equalsIgnoreCase(XMLBottinParser.TEL_BUREAU)) {
		key = XMLBottinParser.TEL_BUREAU;
	    }
	    if (localName.equalsIgnoreCase(XMLBottinParser.EMPLACEMENT)) {
		key = XMLBottinParser.EMPLACEMENT;
	    }
	    if (localName.equalsIgnoreCase(XMLBottinParser.COURRIEL)) {
		key = XMLBottinParser.COURRIEL;
	    }
	    if (localName.equalsIgnoreCase(XMLBottinParser.SERVICE)) {
		key = XMLBottinParser.SERVICE;
	    }
	    if (localName.equalsIgnoreCase(XMLBottinParser.TITRE)) {
		key = XMLBottinParser.TITRE;
	    }
	    if (localName.equalsIgnoreCase(XMLBottinParser.DATE_MODIF)) {
		key = "date_modif";
	    }

	    if (key != null) {
		String value = buffer.toString();
		if (value == null) {
		    value = "";
		}
		values.put(key, value);
	    }

	    if (localName.equalsIgnoreCase(XMLBottinParser.ENTRY_TAG)) {
		cvalues.add(values);
		newEntry = false;
	    }

	    buffer = null;
	}
	if (localName.equals("RechercheResult")) {
	    bundle.setContent(cvalues.toArray(new ContentValues[] {}));
	}
    }

    /**
     * keep
     */
    // private long parseDateString(String date) {
    // // format : 2012-01-01T12:00:00
    //
    // String[] split = date.split("T");
    // String[] date_components = split[0].split("-");
    // String[] time_components = split[1].split(":");
    //
    // int second = Integer.parseInt(time_components[2]);
    // int minute = Integer.parseInt(time_components[1]);
    // int hourOfDay = Integer.parseInt(time_components[0]);
    // // hourOfDay = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
    //
    // int day = Integer.parseInt(date_components[2]);
    // int month = Integer.parseInt(date_components[1]);
    // int year = Integer.parseInt(date_components[0]);
    //
    // Calendar c = Calendar.getInstance(Locale.CANADA_FRENCH);
    // c.set(year, month, day, hourOfDay, minute, second);
    //
    // return c.getTime().getTime();
    // }

    @Override
    public void startElement(final String uri, final String localName, final String qName,
	    final Attributes attributes) throws SAXException {
	// super.startElement(uri, localName, qName, attributes);
	// On reinitialise le buffer à chaque fois qu'on trouver un nouveau tag
	// d'ouverture xml.
	buffer = new StringBuffer();

	// Si le tag est un "item", on crée un nouveau "news" vide et on indique
	// qu'on est dans un "item"
	if (localName.equalsIgnoreCase(XMLBottinParser.ENTRY_TAG)) {
	    // Log.d("XMLBottinParser", localName);
	    newEntry = true;
	    values = new ContentValues();
	}
    }
}
