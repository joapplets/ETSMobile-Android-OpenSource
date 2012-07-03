package ca.etsmtl.applets.etsmobile.tools.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;
import ca.etsmtl.applets.etsmobile.models.BottinEntry;
import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;

/**
 * Inspir� du tutoriel de la page suivante :
 * http://thibault-koprowski.fr/2010/10/15/tutoriel-parsing-xml-sous-android/
 * 
 * @author Rodrigo Manyari.
 * @version 1.0
 */
public class XMLParser {

	private XMLAppletsHandler handler;
	// L'inputStream est utilis� pour garder en m�moire le texte qu'on est all�s
	// chercher � l'aide de la m�thode openStream.
	private InputStream inputStream = null;
	private final String OPEN_STREAM_ERROR_MESSAGE = "Something wrong happened when trying to get the stream from the server";
	private final String PARSING_DONE_MESSAGE = "Parsing done.";
	private final String SAX_PARSER = "Couldn't get a new sax parser.";

	private final String SAX_PARSER_FACTORY_INSTANCE_ERROR_MESSAGE = "Couldn't get a new sax parser instance from factory.";

	// L'objet qui permet de parser le xml (RSS feed).
	private SAXParser saxParser = null;

	// Les messages d'erreur qui pourraient appara�tre dans logcat.
	private final String TAG = "com.manyari.xml.XMLPARSER";

	public XMLParser(final InputStream stream, final XMLAppletsHandler handler,
			final Context c) {

		this.handler = handler;

		try {

			// D'abord on instancie une factory de sax pour aller chercher un
			// parser.
			saxParser = SAXParserFactory.newInstance().newSAXParser();
		} catch (final ParserConfigurationException e) {

			// Il se peut que pour des raisons X �a plante (aller chercher la
			// factory),
			// dans ce cas l� on affiche un message dans le logcat.
			Log.e(TAG, SAX_PARSER_FACTORY_INSTANCE_ERROR_MESSAGE);
		} catch (final SAXException e) {

			// M�me chose au moment d'aller chercher l'instance de sax parser.
			Log.e(TAG, SAX_PARSER);
		}

		try {

			// On essaie d'aller chercher les donn�es � partir du URL.
			inputStream = stream;

			// Si �a ne plante pas, on v�rifie qu'il a de quoi � lire.
			if (inputStream != null) {
				// Si tout est Ok, on parse toutes les donn�es
				saxParser.parse(inputStream, handler);
			}

			inputStream.close();

		} catch (final IOException e) {

			// Encore une fois, pour des raisons X, il se peut qu'on ne soit pas
			// capables d'aller chercher
			// les donn�es.
			Log.e(TAG, OPEN_STREAM_ERROR_MESSAGE);
		} catch (final SAXException e) {

			// Ou que notre parsing fail!
			Log.e(TAG, PARSING_DONE_MESSAGE);
		}

	}

	public XMLParser(final URL url, final XMLAppletsHandler handler,
			final Context c) throws IOException {
		new XMLParser(url.openStream(), handler, c);
	}

	@SuppressWarnings("unchecked")
	public List<BottinEntry> getBottinEntries() {

		if (handler instanceof XMLBottinHandler) {
			return (List<BottinEntry>) handler.getData();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<News> getParsedNews() {
		if (handler instanceof XMLRssFbTwitterHandler) {
			return (ArrayList<News>) handler.getData();
		} else {
			return null;
		}
	}

	public StudentProfile getParsedStudentProfile() {
		if (handler instanceof XMLUserProfileParser) {
			return (StudentProfile) handler.getData();
		} else {
			return null;
		}
	}
}
