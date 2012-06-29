package ca.etsmtl.applets.etsmobile.models;

public class StudentProfile {
	private final String nom, prenom, codePerm, solde;

	public StudentProfile(final String nom, final String prenom,
			final String codePerm, final String solde) {
		this.nom = nom;
		this.prenom = prenom;
		this.codePerm = codePerm;
		this.solde = solde;
	}

	public String getCodePerm() {
		return codePerm;
	}

	public String getNomComplet() {
		return prenom + " " + nom;
	}

	public String getSolde() {
		return solde;
	}
}
