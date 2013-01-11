package ca.etsmtl.applets.etsmobile.models;

import com.google.gson.annotations.SerializedName;

public class ActivityCalendar implements Comparable<ActivityCalendar> {

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

    @SerializedName("groupe")
    private String groupe;

    private int eventDrawableResId;

    public ActivityCalendar(String cours, String startDate, String endDate, String titreCours,
	    String name, String groupe, String codeActivite, String jour, String journee,
	    String location, int eventDrawableResId) {
	this.cours = cours;
	this.startDate = startDate;
	this.endDate = endDate;
	this.titreCours = titreCours;
	this.name = name;
	this.groupe = groupe;
	this.codeActivite = codeActivite;
	this.jour = jour;
	this.journee = journee;
	this.location = location;
	this.eventDrawableResId = eventDrawableResId;

    }

    public ActivityCalendar() {
		// TODO Auto-generated constructor stub
	}

	@Override
    public int compareTo(final ActivityCalendar another) {
	// TODO Auto-generated method stub

	return cours.compareTo(another.getCours()) == 0
		&& groupe.compareTo(another.getGroupe()) == 0
		&& jour.compareTo(another.getJour()) == 0 ? 0 : -1;
    }

    public String getCodeActivite() {
	return codeActivite;
    }

    public String getCours() {
	return cours;
    }

    public int getDrawableResId() {
	return eventDrawableResId;
    }

    public String getEndDate() {
	return endDate;
    }

    public String getGroupe() {
	return groupe;
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

    public void setDrawableResId(final int resID) {
	eventDrawableResId = resID;
    }

    public void setEndDate(final String endDate) {
	this.endDate = endDate;
    }

    public void setGroupe(final String groupe) {
	this.groupe = groupe;
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

    public void print() {
	System.out.println(this.cours + " " + this.journee);
    }
}