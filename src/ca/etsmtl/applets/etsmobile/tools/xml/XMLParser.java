package ca.etsmtl.applets.etsmobile.tools.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import ca.etsmtl.applets.etsmobile.models.News;

import android.content.Context;
import android.util.Log;

/**
 * Inspir� du tutoriel de la page suivante :
 * http://thibault-koprowski.fr/2010/10/15/tutoriel-parsing-xml-sous-android/
 * @author Rodrigo Manyari.
 * @version 1.0
 */
public class XMLParser {

	// Les messages d'erreur qui pourraient appara�tre dans logcat.
	private final String TAG = "com.manyari.xml.XMLPARSER";
	private final String OPEN_STREAM_ERROR_MESSAGE = "Something wrong happened when trying to get the stream from the server";
	private final String SAX_PARSER_FACTORY_INSTANCE_ERROR_MESSAGE = "Couldn't get a new sax parser instance from factory.";
	private final String SAX_PARSER = "Couldn't get a new sax parser.";
	private final String PARSING_DONE_MESSAGE = "Parsing done.";
	
	// L'objet qui permet de parser le xml (RSS feed).
	private SAXParser saxParser = null;
	
	// L'inputStream est utilis� pour garder en m�moire le texte qu'on est all�s
	// chercher � l'aide de la m�thode openStream.
	private InputStream inputStream = null; 
	
	private XMLAppletsHandler handler;
	
	public XMLParser(URL url, XMLAppletsHandler handler, Context c){
		
		this.handler = handler;
		
		try {
			
			// D'abord on instancie une factory de sax pour aller chercher un parser.
			saxParser = SAXParserFactory.newInstance().newSAXParser();
		} catch (ParserConfigurationException e) {
			
			// Il se peut que pour des raisons X �a plante (aller chercher la factory), 
			// dans ce cas l� on affiche un message dans le logcat.
			Log.e(TAG, SAX_PARSER_FACTORY_INSTANCE_ERROR_MESSAGE);
		} catch (SAXException e) {
			
			// M�me chose au moment d'aller chercher l'instance de sax parser. 
			Log.e(TAG, SAX_PARSER);
		}
		
		try {
			
			// On essaie d'aller chercher les donn�es � partir du URL.
			inputStream = url.openStream();
			
			// Si �a ne plante pas, on v�rifie qu'il a de quoi � lire.
			if(inputStream != null){			
				// Si tout est Ok, on parse toutes les donn�es
				saxParser.parse(inputStream, handler);
				
			}
		} catch (IOException e) {
			
			// Encore une fois, pour des raisons X, il se peut qu'on ne soit pas capables d'aller chercher
			// les donn�es.
			Log.e(TAG, OPEN_STREAM_ERROR_MESSAGE);
		} catch (SAXException e) {
			
			// Ou que notre parsing fail!
			Log.e(TAG, PARSING_DONE_MESSAGE);
		}
			
	}
	
	public ArrayList<News> getParsedNews(){
		return handler.getData();
	}
}
