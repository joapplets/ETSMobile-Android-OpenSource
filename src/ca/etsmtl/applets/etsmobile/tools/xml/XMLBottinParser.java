package ca.etsmtl.applets.etsmobile.tools.xml;

import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import ca.etsmtl.applets.etsmobile.models.BottinEntry;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;

public class XMLBottinParser extends XMLAppletsHandler {

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
	private String courriel;
	private String date_modif;
	private String emplacement;
	private String id;
	private boolean newEntry;
	private String nom;
	private String prenom;
	private String service;
	private String tel_bureau;
	private String titre;

	public XMLBottinParser(ObservableBundle b) {
		super(b);
	}

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {
		if (newEntry) {

			if (localName.equalsIgnoreCase(XMLBottinParser.ID)) {
				id = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinParser.NOM)) {
				nom = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinParser.PRENOM)) {
				prenom = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinParser.TEL_BUREAU)) {
				tel_bureau = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinParser.EMPLACEMENT)) {
				emplacement = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinParser.COURRIEL)) {
				courriel = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinParser.SERVICE)) {
				service = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinParser.TITRE)) {
				titre = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinParser.DATE_MODIF)) {
				date_modif = buffer.toString();
			}

			if (localName.equalsIgnoreCase(ENTRY_TAG)) {
				Date date = new Date(System.currentTimeMillis());
				try {
					date = new Date(parseDateString(date_modif));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final BottinEntry be = new BottinEntry(id, nom, prenom,
						tel_bureau, emplacement, courriel, service, titre, date);
				bundle.setContent(be);
				newEntry = false;
			}

			buffer = null;
			// super.endElement(uri, localName, qName);
		}
	}

	private long parseDateString(String date) {
		// format : 2012-01-01T12:00:00

		String[] split = date.split("T");
		String[] date_components = split[0].split("-");
		String[] time_components = split[1].split(":");

		int second = Integer.parseInt(time_components[2]);
		int minute = Integer.parseInt(time_components[1]);
		int hourOfDay = Integer.parseInt(time_components[0]);
		// hourOfDay = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;

		int day = Integer.parseInt(date_components[2]);
		int month = Integer.parseInt(date_components[1]);
		int year = Integer.parseInt(date_components[0]);

		Calendar c = Calendar.getInstance(Locale.CANADA_FRENCH);
		c.set(year, month, day, hourOfDay, minute, second);

		return c.getTime().getTime();
	}

	@Override
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes attributes)
			throws SAXException {
		// super.startElement(uri, localName, qName, attributes);
		// On reinitialise le buffer à chaque fois qu'on trouver un nouveau tag
		// d'ouverture xml.
		buffer = new StringBuffer();

		// Si le tag est un "item", on crée un nouveau "news" vide et on indique
		// qu'on est dans un "item"
		if (localName.equalsIgnoreCase(XMLBottinParser.ENTRY_TAG)) {
			newEntry = true;
		}
	}
}
