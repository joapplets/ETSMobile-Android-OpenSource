package ca.etsmtl.applets.etsmobile.models;

import ca.etsmtl.applets.etsmobile.tools.db.SQLDBHelper;

public class Programme {

	/**
	 * 	private final String PROGRAMMES_TABLE = SQLDBHelper.PROGRAMMES_TABLE;
	private final String PROGRAMMES_ID = SQLDBHelper.PROGRAMMES_ID;
	private final String PROGRAMMES_DESCRIPTION = SQLDBHelper.PROGRAMMES_DESCRIPTION;
	private final String PROGRAMMES_NAME = SQLDBHelper.PROGRAMMES_NAME;
	private final String PROGRAMMES_PROGRAMME_ID = SQLDBHelper.PROGRAMMES_PROGRAMME_ID;
	private final String PROGRAMMES_SHORT_NAME = SQLDBHelper.PROGRAMMES_SHORT_NAME;
	private final String PROGRAMMES_URL = SQLDBHelper.PROGRAMMES_URL;
	private final String PROGRAMMES_URL_PDF = SQLDBHelper.PROGRAMMES_URL_PDF;
	 * 
	 */
	
	private String id, description, nom, nomCourt, url, urlToPDF;
	
	public Programme(){
		
	}
	
	public Programme(String id, String description, String nom, String nomCourt, String url, String urlToPdf){
		this.id = id;
		this.description = description;
		this.nom = nom;
		this.nomCourt = nomCourt;
		this.url = url;
		this.urlToPDF = urlToPdf;
	}
	
	public void setID(String id){
		this.id = id;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setNom(String nom){
		this.nom = nom;
	}
	
	public void setNomCourt(String nomCourt){
		this.nomCourt = nomCourt;
	}
	
	public void setURL(String url){
		this.url = url;
	}
	
	public void setURLtoPDF(String urlToPDF){
		this.urlToPDF = urlToPDF;
	}
}
