package ca.etsmtl.applets.etsmobile.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class CourseEvaluation implements Serializable {
	
	private static final long serialVersionUID = 7314596338980655733L;

	@SerializedName("noteACeJour")
	private String noteACeJour;
	
	@SerializedName("scoreFinalSur100")
	private String scoreFinalSur100;
	
	@SerializedName("moyenneClasse")
	private String moyenneClasse;
	
	@SerializedName("ecartTypeClasse")
	private String ecartTypeClasse;
	
	@SerializedName("medianeClasse")
	private String medianeClasse;
	
	@SerializedName("rangCentileClasse")
	private String rangCentileClasse;
	
	@SerializedName("noteACeJourElementsIndividuels")
	private String noteACeJourElementsIndividuels;
	
	@SerializedName("noteSur100PourElementsIndividuels")
	private String noteSur100PourElementsIndividuels;
	
	private String cote;
	
	@SerializedName("liste")
	private ArrayList<EvaluationElement> evaluationElements;

	public String getNoteACeJour() {
		return noteACeJour;
	}

	public void setNoteACeJour(String noteACeJour) {
		this.noteACeJour = noteACeJour;
	}

	public String getScoreFinalSur100() {
		return scoreFinalSur100;
	}

	public void setScoreFinalSur100(String scoreFinalSur100) {
		this.scoreFinalSur100 = scoreFinalSur100;
	}

	public String getMoyenneClasse() {
		return moyenneClasse;
	}

	public void setMoyenneClasse(String moyenneClasse) {
		this.moyenneClasse = moyenneClasse;
	}

	public String getEcartTypeClasse() {
		return ecartTypeClasse;
	}

	public void setEcartTypeClasse(String ecartTypeClasse) {
		this.ecartTypeClasse = ecartTypeClasse;
	}

	public String getMedianeClasse() {
		return medianeClasse;
	}

	public void setMedianeClasse(String medianeClasse) {
		this.medianeClasse = medianeClasse;
	}

	public String getRangCentileClasse() {
		return rangCentileClasse;
	}

	public void setRangCentileClasse(String rangCentileClasse) {
		this.rangCentileClasse = rangCentileClasse;
	}

	public String getNoteACeJourElementsIndividuels() {
		return noteACeJourElementsIndividuels;
	}

	public void setNoteACeJourElementsIndividuels(
			String noteACeJourElementsIndividuels) {
		this.noteACeJourElementsIndividuels = noteACeJourElementsIndividuels;
	}

	public String getNoteSur100PourElementsIndividuels() {
		return noteSur100PourElementsIndividuels;
	}

	public void setNoteSur100PourElementsIndividuels(
			String noteSur100PourElementsIndividuels) {
		this.noteSur100PourElementsIndividuels = noteSur100PourElementsIndividuels;
	}

	public String getCote() {
		return cote;
	}

	public void setCote(String cote) {
		this.cote = cote;
	}

	public ArrayList<EvaluationElement> getEvaluationElements() {
		return evaluationElements;
	}

	public void setEvaluationElements(
			ArrayList<EvaluationElement> evaluationElements) {
		this.evaluationElements = evaluationElements;
	}
}
