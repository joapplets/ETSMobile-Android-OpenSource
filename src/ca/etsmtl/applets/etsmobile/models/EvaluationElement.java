package ca.etsmtl.applets.etsmobile.models;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
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
	
	@Override
	public String toString() {
		return nom + " - " + note;
	}
}
