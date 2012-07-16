package ca.etsmtl.applets.etsmobile.tools.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import ca.etsmtl.applets.etsmobile.models.StudentProfile;

public class XMLUserProfileParser extends XMLAppletsHandler{

	private final String NOM = "nom";
	private final String PRENOM = "prenom";
	private final String CODEPERM = "codePerm";
	private final String SOLDETOTAL = "soldeTotal";
	
	private boolean inItem;
	private String currentNom;
	private String currentPrenom;
	private String currentCodePerm;
	private String currentSoldeTotal;
	private StudentProfile profile;
		
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		// On reinitialise le buffer à chaque fois qu'on trouver un nouveau tag d'ouverture xml.
		buffer = new StringBuffer();
		
		// Si le tag est un "item", on crée un nouveau "news" vide et on indique qu'on est dans un "item"
		if(localName.equalsIgnoreCase("infoEtudiantResult")){
			inItem = true;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		if(localName.equalsIgnoreCase(NOM) && inItem){
			currentNom = buffer.toString();
			buffer = null;
		}
		
		if(localName.equalsIgnoreCase(PRENOM) && inItem){
			currentPrenom = buffer.toString();
			buffer = null;
		}
		if(localName.equalsIgnoreCase(CODEPERM) && inItem){
			currentCodePerm = buffer.toString();
			buffer = null;
		}
		
		if(localName.equalsIgnoreCase(SOLDETOTAL) && inItem){
			currentSoldeTotal = buffer.toString();
			buffer = null;
		}
		
		if(localName.equalsIgnoreCase("infoEtudiantResult") & inItem){
			profile = new StudentProfile(currentNom, currentPrenom, currentCodePerm, currentSoldeTotal);
			inItem = false;
		}
			
	}
	
	public StudentProfile getStudentProfile(){
		return profile;
	}

}
