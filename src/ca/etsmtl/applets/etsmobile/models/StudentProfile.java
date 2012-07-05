package ca.etsmtl.applets.etsmobile.models;

public class StudentProfile {
	private String nom, prenom, codePerm, solde;
	
	public StudentProfile (String nom, String prenom, String codePerm, String solde){
		this.nom = nom;
		this.prenom = prenom;
		this.codePerm = codePerm;
		this.solde = solde;
	}
	
	public String getCodePerm(){
		return codePerm;
	}
	
	public String getSolde(){
		return solde;
	}
	
	public String getNom(){
		return nom;
	}
	
	public String getPrenom(){
		return prenom;
	}
}
