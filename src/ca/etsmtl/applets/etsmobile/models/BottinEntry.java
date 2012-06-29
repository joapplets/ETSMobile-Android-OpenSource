package ca.etsmtl.applets.etsmobile.models;

public class BottinEntry {

	private String date_modif;
	private String email;
	private String emplacement;
	private long id;
	private String nom;
	private String prenom;
	private String service;
	private String telBureau;
	private String titre;

	public BottinEntry(final long id, final String nom, final String prenom,
			final String telBureau, final String emplacement,
			final String email, final String service, final String titre,
			final String date_modif) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.telBureau = telBureau;
		this.emplacement = emplacement;
		this.email = email;
		this.service = service;
		this.titre = titre;
		this.date_modif = date_modif;
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
		return id;
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
		this.id = id;
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

}
