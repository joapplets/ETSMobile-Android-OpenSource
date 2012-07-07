package ca.etsmtl.applets.etsmobile.models;

import java.sql.Date;

import android.content.ContentValues;
import ca.etsmtl.applets.etsmobile.tools.db.SQLDBHelper;

public class BottinEntry {

	private String courriel;
	private Date date_modif;
	private String emplacement;
	private String nom;
	private String prenom;
	private String service;
	private String telBureau;
	private String titre;
	private long uuid;
	private String ets_id;

	public BottinEntry(final long id, final String nom, final String prenom,
			final String telBureau, final String emplacement,
			final String courriel, final String service, final String titre,
			final Date date_modif) {
		super();
		uuid = id;
		this.nom = nom;
		this.prenom = prenom;
		this.telBureau = telBureau;
		this.emplacement = emplacement;
		this.courriel = courriel;
		this.service = service;
		this.titre = titre;
		this.date_modif = date_modif;
	}

	public BottinEntry(final String id2, final String nom2,
			final String prenom2, final String tel_bureau,
			final String emplacement2, final String courriel,
			final String service2, final String titre2, final Date date_modif2) {

		this(-1, nom2, prenom2, tel_bureau, emplacement2, courriel, service2,
				titre2, date_modif2);
		this.ets_id = id2;
	}

	public String getCourriel() {
		return courriel;
	}

	public Date getDate_modif() {
		return date_modif;
	}

	public String getEmplacement() {
		return emplacement;
	}

	public long getId() {
		return uuid;
	}

	public String getEtsId() {
		return ets_id;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getService() {
		return service;
	}

	public String getTelBureau() {
		return telBureau;
	}

	public String getTitre() {
		return titre;
	}

	public void setCourriel(final String email) {
		courriel = email;
	}

	public void setDate_modif(final Date date_modif) {
		this.date_modif = date_modif;
	}

	public void setEmplacement(final String emplacement) {
		this.emplacement = emplacement;
	}

	public void setId(final long id) {
		uuid = id;
	}

	public void setNom(final String nom) {
		this.nom = nom;
	}

	public void setPrenom(final String prenom) {
		this.prenom = prenom;
	}

	public void setService(final String service) {
		this.service = service;
	}

	public void setTelBureau(final String telBureau) {
		this.telBureau = telBureau;
	}

	public void setTitre(final String titre) {
		this.titre = titre;
	}

	@Override
	public String toString() {
		return prenom + " " + nom;
	}

	@Override
	public boolean equals(Object o) {
		final BottinEntry b = (BottinEntry) o;
		return (b.getNom().equals(getNom())
				&& b.getPrenom().equals(getPrenom()) && b.getCourriel().equals(
				getCourriel()));
	}

	public ContentValues getContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(SQLDBHelper.BOTTIN_NOM, (nom == null) ? "" : nom);
		cv.put(SQLDBHelper.BOTTIN_PRENOM, (prenom == null) ? "" : prenom);
		cv.put(SQLDBHelper.BOTTIN_TELBUREAU, (telBureau) == null ? ""
				: telBureau);
		cv.put(SQLDBHelper.BOTTIN_EMPLACEMENT, (emplacement == null) ? ""
				: emplacement);
		cv.put(SQLDBHelper.BOTTIN_EMAIL, (courriel == null) ? "" : courriel);
		cv.put(SQLDBHelper.BOTTIN_SERVICE, (service == null) ? "" : service);
		cv.put(SQLDBHelper.BOTTIN_TIRE, (titre == null) ? "" : titre);
		cv.put(SQLDBHelper.BOTTIN_DATE_MODIF, date_modif.toGMTString());
		cv.put(SQLDBHelper.BOTTIN_ETS_ID, (ets_id == null) ? "" : ets_id);
		return cv;
	}
}
