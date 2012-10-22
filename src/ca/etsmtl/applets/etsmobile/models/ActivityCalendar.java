package ca.etsmtl.applets.etsmobile.models;

import com.google.gson.annotations.SerializedName;

public class ActivityCalendar {
	
	@SerializedName("heureDebut")
	private String startDate;
	@SerializedName("heureFin")
	private String endDate;
	@SerializedName("nomActivite")
	private String name;
	@SerializedName("local")
	private String location;
	
	@SerializedName("jour")
	private String jour;
	
	public String getJour() {
		return jour;
	}

	public void setJour(String jour) {
		this.jour = jour;
	}

	public String getJournee() {
		return journee;
	}

	public void setJournee(String journee) {
		this.journee = journee;
	}

	public String getCodeActivite() {
		return codeActivite;
	}

	public void setCodeActivite(String codeActivite) {
		this.codeActivite = codeActivite;
	}

	public String getTitreCours() {
		return titreCours;
	}

	public void setTitreCours(String titreCours) {
		this.titreCours = titreCours;
	}


	@SerializedName("journee")
	private String journee; 
	
	@SerializedName("codeActivite")
	private String codeActivite;
	
	@SerializedName("sigle")
	private String cours;
	
	@SerializedName("titreCours")
	private String titreCours;
	
	
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCours() {
		return cours;
	}
	public void setCours(String cours) {
		this.cours = cours;
	}
	
	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}
	
}
