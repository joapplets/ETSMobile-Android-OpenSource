/*******************************************************************************
 * Copyright 2013 Club ApplETS
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
				// Log.d("XMLProfileParser", buffer.toString());
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
		super.startElement(uri, localName, qName, attributes);
		buffer = new StringBuffer();
		if (localName.equalsIgnoreCase(XMLProfileParser.ENTRY_TAG)) {
			newEntry = true;
			values = new ContentValues();
		}
		Log.d("XMLProfileParser", localName);
	}
}
