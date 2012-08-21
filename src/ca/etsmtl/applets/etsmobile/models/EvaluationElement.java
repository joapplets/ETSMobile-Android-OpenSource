package ca.etsmtl.applets.etsmobile.models;

import com.google.gson.annotations.SerializedName;

public class EvaluationElement {
	
	@SerializedName("coursGroupe")
	private String coursGroupe;
	
	@SerializedName("nom")
	private String nom;
	
	@SerializedName("equipe")
	private String equipe;
	
	@SerializedName("dateCible")
	private String dateCible;
	
	@SerializedName("note")
	private String note;
	
	@SerializedName("corrigeSur")
	private String corrigeSur;
	
	@SerializedName("ponderation")
	private String ponderation;
	
	@SerializedName("moyenne")
	private String moyenne;
	
	@SerializedName("ecartType")
	private String ecartType;
	
	@SerializedName("mediane")
	private String mediane;
	
	@SerializedName("rangCentile")
	private String rangCentile;
	
	@SerializedName("publie")
	private String publie;
	
	@SerializedName("messageDuProf")
	private String messageDuProf;
	
	@SerializedName("ignoreDuCalcul")
	private String ignoreDuCalcul;
	
	public String getCoursGroupe() {
		return coursGroupe;
	}

	public void setCoursGroupe(String coursGroupe) {
		this.coursGroupe = coursGroupe;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getEquipe() {
		return equipe;
	}

	public void setEquipe(String equipe) {
		this.equipe = equipe;
	}

	public String getDateCible() {
		return dateCible;
	}

	public void setDateCible(String dateCible) {
		this.dateCible = dateCible;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCorrigeSur() {
		return corrigeSur;
	}

	public void setCorrigeSur(String corrigeSur) {
		this.corrigeSur = corrigeSur;
	}

	public String getPonderation() {
		return ponderation;
	}

	public void setPonderation(String ponderation) {
		this.ponderation = ponderation;
	}

	public String getMoyenne() {
		return moyenne;
	}

	public void setMoyenne(String moyenne) {
		this.moyenne = moyenne;
	}

	public String getEcartType() {
		return ecartType;
	}

	public void setEcartType(String ecartType) {
		this.ecartType = ecartType;
	}

	public String getMediane() {
		return mediane;
	}

	public void setMediane(String mediane) {
		this.mediane = mediane;
	}

	public String getRangCentile() {
		return rangCentile;
	}

	public void setRangCentile(String rangCentile) {
		this.rangCentile = rangCentile;
	}

	public String getPublie() {
		return publie;
	}

	public void setPublie(String publie) {
		this.publie = publie;
	}

	public String getMessageDuProf() {
		return messageDuProf;
	}

	public void setMessageDuProf(String messageDuProf) {
		this.messageDuProf = messageDuProf;
	}

	public String getIgnoreDuCalcul() {
		return ignoreDuCalcul;
	}

	public void setIgnoreDuCalcul(String ignoreDuCalcul) {
		this.ignoreDuCalcul = ignoreDuCalcul;
	}

	@Override
	public String toString() {
		return nom + " - " + note;
	}
}
