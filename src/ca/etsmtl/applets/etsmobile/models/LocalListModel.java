package ca.etsmtl.applets.etsmobile.models;

import android.util.Log;

public class LocalListModel {
	

	public String[] getListLocaux(){
		
		// devrait lire dans un fichier xml et créer un array de tout les locaux
		// en attendant
	     String[] tabLocaux = new String[] {
	   	         "B-1502", "B-1708", "A-1300", "B-3428", "A-3412"
	   	     };
	
		return tabLocaux;
	}
	
	
}
