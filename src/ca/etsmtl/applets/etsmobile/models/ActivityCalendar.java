package ca.etsmtl.applets.etsmobile.models;

import android.content.ContentValues;
import android.database.Cursor;
import ca.etsmtl.applets.etsmobile.tools.db.ActivityCalendarTableHelper;

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
    private long session_id;

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
    }

    public ActivityCalendar(Cursor mActivityCursor) {
	this.cours = mActivityCursor.getString(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_COURS));
	this.startDate = mActivityCursor.getString(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_START_DATE));
	this.endDate = mActivityCursor.getString(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_END_DATE));
	this.titreCours = mActivityCursor.getString(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_TITRE));
	this.name = mActivityCursor.getString(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_NAME));
	this.groupe = mActivityCursor.getString(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_GROUP));
	this.codeActivite = mActivityCursor.getString(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_CODE));
	this.jour = mActivityCursor.getString(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_JOUR));
	this.journee = mActivityCursor.getString(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_JOURNEE));
	this.location = mActivityCursor.getString(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_LOCATION));
	this.eventDrawableResId = mActivityCursor.getInt(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_EVENT_DRAW_ID));
	this.session_id = mActivityCursor.getInt(mActivityCursor
		.getColumnIndex(ActivityCalendarTableHelper.ACTIVITY_SESSION_ID));

    }

    @Override
    public int compareTo(final ActivityCalendar another) {

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

    public void setSessionId(long session_id2) {
	this.session_id = session_id2;

    }

    public void print() {
	System.out.println(this.cours + " " + this.journee);
    }

    public ContentValues getContentValues() {
	final ContentValues cv = new ContentValues();
	cv.put(ActivityCalendarTableHelper.ACTIVITY_CODE, codeActivite);
	cv.put(ActivityCalendarTableHelper.ACTIVITY_COURS, cours);
	cv.put(ActivityCalendarTableHelper.ACTIVITY_END_DATE, endDate);
	cv.put(ActivityCalendarTableHelper.ACTIVITY_JOUR, jour);
	cv.put(ActivityCalendarTableHelper.ACTIVITY_JOURNEE, journee);
	cv.put(ActivityCalendarTableHelper.ACTIVITY_LOCATION, location);
	cv.put(ActivityCalendarTableHelper.ACTIVITY_NAME, name);
	cv.put(ActivityCalendarTableHelper.ACTIVITY_START_DATE, startDate);
	cv.put(ActivityCalendarTableHelper.ACTIVITY_TITRE, titreCours);
	cv.put(ActivityCalendarTableHelper.ACTIVITY_SESSION_ID, session_id);
	return cv;
    }

    public void setId(long id) {
	// TODO Auto-generated method stub

    }

}