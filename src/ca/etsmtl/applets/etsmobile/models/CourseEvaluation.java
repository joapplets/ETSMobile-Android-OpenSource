package ca.etsmtl.applets.etsmobile.models;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class CourseEvaluation {
	
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
	
	@SerializedName("liste")
	private ArrayList<EvaluationElement> evaluationElements;
}
