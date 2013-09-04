package ca.etsmtl.applets.etsmobile.models;

import java.sql.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import ca.etsmtl.applets.etsmobile.tools.db.BottinTableHelper;

public class BottinEntry extends Model {

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
		ets_id = id2;
	}

	public BottinEntry(Cursor cursor) {
		this(cursor
				.getLong(cursor.getColumnIndex(BottinTableHelper.BOTTIN__ID)),
				cursor.getString(cursor
						.getColumnIndex(BottinTableHelper.BOTTIN_NOM)),
				cursor.getString(cursor
						.getColumnIndex(BottinTableHelper.BOTTIN_PRENOM)),
				cursor.getString(cursor
						.getColumnIndex(BottinTableHelper.BOTTIN_TELBUREAU)),
				cursor.getString(cursor
						.getColumnIndex(BottinTableHelper.BOTTIN_EMPLACEMENT)),
				cursor.getString(cursor
						.getColumnIndex(BottinTableHelper.BOTTIN_COURRIEL)),
				cursor.getString(cursor
						.getColumnIndex(BottinTableHelper.BOTTIN_SERVICE)),
				cursor.getString(cursor
						.getColumnIndex(BottinTableHelper.BOTTIN_TIRE)),
				new Date(System.currentTimeMillis()));
	}

	@Override
	public boolean equals(final Object o) {
		final BottinEntry b = (BottinEntry) o;
		return b.nom.equals(nom) && b.prenom.equals(prenom)
				&& b.courriel.equals(courriel);
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues cv = new ContentValues();
		cv.put(BottinTableHelper.BOTTIN_NOM, nom == null ? "" : nom);
		cv.put(BottinTableHelper.BOTTIN_PRENOM, prenom == null ? "" : prenom);
		cv.put(BottinTableHelper.BOTTIN_TELBUREAU, telBureau == null ? ""
				: telBureau);
		cv.put(BottinTableHelper.BOTTIN_EMPLACEMENT, emplacement == null ? ""
				: emplacement);
		cv.put(BottinTableHelper.BOTTIN_COURRIEL, courriel == null ? ""
				: courriel);
		cv.put(BottinTableHelper.BOTTIN_SERVICE, service == null ? "" : service);
		cv.put(BottinTableHelper.BOTTIN_TIRE, titre == null ? "" : titre);
		cv.put(BottinTableHelper.BOTTIN_DATE_MODIF, date_modif.toGMTString());
		cv.put(BottinTableHelper.BOTTIN_ETS_ID, ets_id == null ? "" : ets_id);
		return cv;
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

	public String getEtsId() {
		return ets_id;
	}

	public long getId() {
		return uuid;
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
	public void writeToParcel(final Parcel dest, final int flags) {

	}

}
