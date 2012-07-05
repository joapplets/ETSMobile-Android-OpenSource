package ca.etsmtl.applets.etsmobile.tools.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;

import android.util.Log;

/**
 * Inspiré du tutoriel de la page suivante :
 * http://thibault-koprowski.fr/2010/10/15/tutoriel-parsing-xml-sous-android/
 * @author Rodrigo Manyari.
 * @version 1.0
 */
public class XMLNewsParser extends XMLAppletsHandler{

	private static final String TAG = "ca.etsmtl.applets.etsmobile.tools.xml.XMLNewsParser";
	
	// Les champs d'une nouvelle (Selon le feed RSS).
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private static final String PUBDATE = "pubDate";
	private static final String GUID = "guid";
	
	// Des variables qui nous permettent de faire le traitement.
	private String source;
	private ArrayList<String> guids;
	private boolean inItem;
	private String title;
	private String date;
	private String description;
	private String guid;
	private boolean isThere;
	private ObservableBundle bundle;
	private News news;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
	
	public XMLNewsParser(String source, ArrayList<String> guids, ObservableBundle bundle) {
		this.source = source;
		this.guids = guids;
		this.bundle = bundle;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		// On reinitialise le buffer à chaque fois qu'on trouver un nouveau tag d'ouverture xml.
		buffer = new StringBuffer();
		
		// Si le tag est un "item", on crée un nouveau "news" vide et on indique qu'on est dans un "item"
		if(localName.equalsIgnoreCase("item")){
			inItem = true;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		// Quand on arrive à la fin d'un élément, la méthode characters a gardé en mémoire tout le 
		// texte entre les tags, à ce moment là, on fait juste prendre ce texte et le rentrer dans
		// les champs appropriés.
		
		if(localName.equalsIgnoreCase(TITLE) && inItem){
			title = buffer.toString();
		}
		
		// Même chose pour les champs description et pubdate.
		if(localName.equalsIgnoreCase(DESCRIPTION) && inItem){
			description = buffer.toString();
		}
		if(localName.equalsIgnoreCase(PUBDATE) && inItem){
			date = buffer.toString();
		}
		
		if(localName.equalsIgnoreCase(GUID) && inItem){
			guid = buffer.toString();
		}
		
		if(localName.equalsIgnoreCase("item") & inItem){
			isThere = false;
			for (String g : guids) {
				if(guid.equals(g)){
					isThere = true;	
				}
			}	
			if(!isThere){
				try {
					news = new News(title, description, guid, source, dateFormat.parse(date));
					bundle.setContent(news);
					inItem = false;
				}catch (ParseException e) {
					Log.e(TAG, e.toString());
				}
			}
		}
		buffer = null;
	}
}