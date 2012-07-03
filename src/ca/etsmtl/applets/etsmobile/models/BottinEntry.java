package ca.etsmtl.applets.etsmobile.models;

public class BottinEntry {

	private String date_modif;
	private String email;
	private String emplacement;
	private long uuid;
	private String nom;
	private String prenom;
	private String service;
	private String telBureau;
	private String titre;
	private String id;

	public BottinEntry(final long id, final String nom, final String prenom,
			final String telBureau, final String emplacement,
			final String email, final String service, final String titre,
			final String date_modif) {
		super();
		this.uuid = id;
		this.nom = nom;
		this.prenom = prenom;
		this.telBureau = telBureau;
		this.emplacement = emplacement;
		this.email = email;
		this.service = service;
		this.titre = titre;
		this.date_modif = date_modif;
	}

	public BottinEntry(String id2, String nom2, String prenom2,
			String tel_bureau, String emplacement2, String courriel,
			String service2, String titre2, String date_modif2) {

		this(-1, nom2, prenom2, tel_bureau, emplacement2, courriel, service2,
				titre2, date_modif2);
		id = id2;
	}

	public String getDate_modif() {
		return date_modif;
	}

	public String getEmail() {
		return email;
	}

	public String getEmplacement() {
		return emplacement;
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

	public void setDate_modif(final String date_modif) {
		this.date_modif = date_modif;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setEmplacement(final String emplacement) {
		this.emplacement = emplacement;
	}

	public void setId(final int id) {
		this.uuid = id;
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
}
