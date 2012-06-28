package ca.etsmtl.applets.etsmobile.models;

public class BottinEntry {

	private int id;
	private String nom;
	private String prenom;
	private String telBureau;
	private String emplacement;
	private String email;
	private String service;
	private String titre;
	private String date_modif;

	public BottinEntry(int id, String nom, String prenom, String telBureau,
			String emplacement, String email, String service, String titre,
			String date_modif) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getTelBureau() {
		return telBureau;
	}

	public void setTelBureau(String telBureau) {
		this.telBureau = telBureau;
	}

	public String getEmplacement() {
		return emplacement;
	}

	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDate_modif() {
		return date_modif;
	}

	public void setDate_modif(String date_modif) {
		this.date_modif = date_modif;
	}

}
