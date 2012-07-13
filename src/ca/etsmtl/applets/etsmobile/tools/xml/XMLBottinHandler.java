package ca.etsmtl.applets.etsmobile.tools.xml;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import ca.etsmtl.applets.etsmobile.models.BottinEntry;

public class XMLBottinHandler extends XMLAppletsHandler {

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
	private ArrayList<BottinEntry> list;
	private boolean newEntry;
	private String nom;
	private String prenom;
	private String service;
	private String tel_bureau;
	private String titre;

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {
		if (newEntry) {

			if (localName.equalsIgnoreCase(XMLBottinHandler.ID)) {
				id = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinHandler.NOM)) {
				nom = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinHandler.PRENOM)) {
				prenom = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinHandler.TEL_BUREAU)) {
				tel_bureau = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinHandler.EMPLACEMENT)) {
				emplacement = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinHandler.COURRIEL)) {
				courriel = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinHandler.SERVICE)) {
				service = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinHandler.TITRE)) {
				titre = buffer.toString();
			}
			if (localName.equalsIgnoreCase(XMLBottinHandler.DATE_MODIF)) {
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
				list.add(be);
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
	public void startDocument() throws SAXException {
		// super.startDocument();
		list = new ArrayList<BottinEntry>();
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
		if (localName.equalsIgnoreCase(XMLBottinHandler.ENTRY_TAG)) {
			newEntry = true;
		}
	}
}
