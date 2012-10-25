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
	@SerializedName("journee")
	private String journee;
	@SerializedName("codeActivite")
	private String codeActivite;
	@SerializedName("sigle")
	private String cours;
	@SerializedName("titreCours")
	private String titreCours;
	private int eventColor;

	public String getCodeActivite() {
		return codeActivite;
	}

	public String getCours() {
		return cours;
	}

	public String getEndDate() {
		return endDate;
	}

	/**
	 * # du jours Lundi = 1
	 * 
	 * @return Le # du jour
	 */
	public String getJour() {
		return jour;
	}

	/**
	 * Nom du jour
	 * 
	 * @return Le nom du jours
	 */
	public String getJournee() {
		return journee;
	}

	/**
	 * Local
	 * 
	 * @return
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Nom cours
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getTitreCours() {
		return titreCours;
	}

	public void setCodeActivite(final String codeActivite) {
		this.codeActivite = codeActivite;
	}

	public void setCours(final String cours) {
		this.cours = cours;
	}

	public void setEndDate(final String endDate) {
		this.endDate = endDate;
	}

	public void setJour(final String jour) {
		this.jour = jour;
	}

	public void setJournee(final String journee) {
		this.journee = journee;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setStartDate(final String startDate) {
		this.startDate = startDate;
	}

	public void setTitreCours(final String titreCours) {
		this.titreCours = titreCours;
	}

	public int getEventColor() {
		return eventColor;
	}

	public void setEventColor(int eventColor) {
		this.eventColor = eventColor;
	}

}
