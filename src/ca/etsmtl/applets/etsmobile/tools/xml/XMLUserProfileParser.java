package ca.etsmtl.applets.etsmobile.tools.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.content.Context;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;

public class XMLUserProfileParser extends XMLAppletsHandler {

	private final String CODEPERM = "codePerm";
	private String currentCodePerm;
	private String currentNom;
	private String currentPrenom;

	private String currentSoldeTotal;
	private boolean inItem;
	private final String NOM = "nom";
	private final String PRENOM = "prenom";
	private StudentProfile profile;

	private final String SOLDETOTAL = "soldeTotal";

	public XMLUserProfileParser(final Context context) {
		super(context);
	}

	@Override
	public void endElement(final String uri, final String localName,
			final String qName) throws SAXException {

		if (localName.equalsIgnoreCase(NOM) && inItem) {
			currentNom = buffer.toString();
			buffer = null;
		}

		if (localName.equalsIgnoreCase(PRENOM) && inItem) {
			currentPrenom = buffer.toString();
			buffer = null;
		}
		if (localName.equalsIgnoreCase(CODEPERM) && inItem) {
			currentCodePerm = buffer.toString();
			buffer = null;
		}

		if (localName.equalsIgnoreCase(SOLDETOTAL) && inItem) {
			currentSoldeTotal = buffer.toString();
			buffer = null;
		}

		if (localName.equalsIgnoreCase("infoEtudiantResult") & inItem) {
			profile = new StudentProfile(currentNom, currentPrenom,
					currentCodePerm, currentSoldeTotal);
			inItem = false;
		}

	}

	@Override
	public Object getData() {
		return profile;
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
		if (localName.equalsIgnoreCase("infoEtudiantResult")) {
			inItem = true;
		}
	}

}
