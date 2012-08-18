package ca.etsmtl.applets.etsmobile.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
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
}
