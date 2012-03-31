package com.applets.tools.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.content.Context;

import com.applets.models.News;

/**
 * Inspiré du tutoriel de la page suivante :
 * http://thibault-koprowski.fr/2010/10/15/tutoriel-parsing-xml-sous-android/
 * 
 * @author Rodrigo Manyari.
 * @version 1.0
 */
public class XMLRSSAndFacebookHandler extends XMLAppletsHandler {

	private String currentGuid;

	private String currentNewsDate;

	private String currentNewsDescription;
	private String currentNewsTitle;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"EEE, d MMM yyyy HH:mm:ss z");
	private final String DESCRIPTION = "description";

	private final String GUID = "guid";
	// Des variables qui nous permettent de faire le traitement.
	private boolean inItem;
	private final String PUBDATE = "pubDate";
	private final String source;
	// Les champs d'une nouvelle (Selon le feed RSS).
	private final String TITLE = "title";

	public XMLRSSAndFacebookHandler(final Context context, final String source) {
		super(context);
		this.source = source;
	}

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {

		// Quand on arrive à la fin d'un élément, la méthode characters a gardé
		// en mémoire tout le
		// texte entre les tags, à ce moment là, on fait juste prendre ce texte
		// et le rentrer dans
		// les champs appropriés.

		if (localName.equalsIgnoreCase(TITLE) && inItem) {
			currentNewsTitle = buffer.toString();

			// On reinitialise le buffer à nouveau pour ne pas avoir des titres
			// dans la
			// description, des descriptions dans les pubdate, etc.
			buffer = null;
		}

		// Même chose pour les champs description et pubdate.
		if (localName.equalsIgnoreCase(DESCRIPTION) && inItem) {
			currentNewsDescription = buffer.toString();
			buffer = null;
		}
		if (localName.equalsIgnoreCase(PUBDATE) && inItem) {
			currentNewsDate = buffer.toString();
			buffer = null;
		}

		if (localName.equalsIgnoreCase(GUID) && inItem) {
			currentGuid = buffer.toString();
			buffer = null;
		}

		// Quand on arrive à la fin d'un champs item, on ajoute la "currentNews"
		// dans la liste
		// et on dit qu'on n'est plus dans un item.
		if (localName.equalsIgnoreCase("item") & inItem) {

			if (news.size() != 0) {
				for (final News n : news) {
					if (currentGuid.equals(n.getGuid())) {
						throw new SAXException("No more new news");
					}
				}
			}

			final News n = new News();
			n.setGuid(currentGuid);
			n.setTitle(currentNewsTitle);

			try {
				final Date date = dateFormat.parse(currentNewsDate);
				n.setPubDate(date.getTime());
			} catch (final ParseException e) {
				e.printStackTrace();
			}

			n.setDescription(currentNewsDescription);
			n.setSource(source);

			newNews.add(n);

			inItem = false;
		}

	}

	@Override
	public void startElement(final String uri, final String localName,
			final String qName, final Attributes attributes)
			throws SAXException {

		// On reinitialise le buffer à chaque fois qu'on trouver un nouveau tag
		// d'ouverture xml.
		buffer = new StringBuffer();

		// Si le tag est un "item", on crée un nouveau "news" vide et on indique
		// qu'on est dans un "item"
		if (localName.equalsIgnoreCase("item")) {
			inItem = true;
		}
	}

}
