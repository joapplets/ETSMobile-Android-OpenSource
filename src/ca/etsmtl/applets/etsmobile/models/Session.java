package ca.etsmtl.applets.etsmobile.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import ca.etsmtl.applets.etsmobile.tools.db.SessionTableHelper;

import com.google.gson.annotations.SerializedName;

@SuppressLint("SimpleDateFormat")
public class Session implements Serializable, Comparable<Session> {

    private static final long serialVersionUID = -632822145233952231L;
    @SerializedName("abrege")
    private String shortName;
    @SerializedName("auLong")
    private String longName;
    @SerializedName("dateDebut")
    private String dateDebutString;
    @SerializedName("dateFin")
    private final String dateFinString;
    @SerializedName("dateFinCours")
    private String dateFinCoursString;
    private int maxActivities = 0;
    private String user_id = "";
    private List<JoursRemplaces> joursRemplaces;
    private LinkedHashMap<String, List<ActivityCalendar>> activities;

    public Session(Cursor mCursor) {
	shortName = mCursor.getString(mCursor
		.getColumnIndex(SessionTableHelper.SESSIONS_SHORT_NAME));
	longName = mCursor.getString(mCursor.getColumnIndex(SessionTableHelper.SESSIONS_LONG_NAME));
	dateDebutString = mCursor.getString(mCursor
		.getColumnIndex(SessionTableHelper.SESSIONS_DATE_DEBUT));
	dateFinString = mCursor.getString(mCursor
		.getColumnIndex(SessionTableHelper.SESSIONS_DATE_FIN));
	dateFinCoursString = mCursor.getString(mCursor
		.getColumnIndex(SessionTableHelper.SESSIONS_DATE_FIN_COURS));
	maxActivities = mCursor.getInt(mCursor.getColumnIndex(SessionTableHelper.SESSIONS_MAX_ACT));
	user_id = mCursor.getString(mCursor.getColumnIndex(SessionTableHelper.SESSIONS_USER_ID));
    }

    @Override
    public int compareTo(final Session s) {
	return s.getDateDebut().compareTo(s.getDateFin());
    }

    public LinkedHashMap<String, List<ActivityCalendar>> getActivities() {
	return activities;
    }

    public List<ActivityCalendar> getActivities(String jour) {
	return (activities != null) ? this.activities.get(jour) : null;
    }

    public Date getDateDebut() {
	SimpleDateFormat formatter;
	Date date;
	formatter = new SimpleDateFormat("yyyy-MM-dd");
	try {
	    date = formatter.parse(dateDebutString);
	} catch (final ParseException e) {
	    date = null;
	}

	return date;
    }

    public String getDateDebutString() {
	return dateDebutString;
    }

    public Date getDateFin() {
	SimpleDateFormat formatter;
	Date date;
	formatter = new SimpleDateFormat("yyyy-MM-dd");
	try {
	    date = formatter.parse(dateFinString);
	    date.setHours(23);
	    date.setMinutes(59);
	    date.setSeconds(59);
	} catch (final ParseException e) {
	    date = null;
	}

	return date;
    }

    /**
     * yyyy-MM-dd
     * 
     * @return formated string
     */
    public Date getDateFinCours() {
	SimpleDateFormat formatter;
	Date date;
	formatter = new SimpleDateFormat("yyyy-MM-dd");
	try {
	    date = formatter.parse(dateFinCoursString);
	    date.setHours(23);
	    date.setMinutes(59);
	    date.setSeconds(59);
	} catch (final ParseException e) {
	    date = null;
	}

	return date;
    }

    public String getDateFinCoursString() {
	return dateFinCoursString;
    }

    public String getDateFinString() {
	return dateFinString;
    }

    public List<JoursRemplaces> getJoursRemplaces() {
	return joursRemplaces;
    }

    public String getLongName() {
	return longName;
    }

    public String getShortName() {
	return shortName;
    }

    public void setActivities(String jour, final List<ActivityCalendar> activities) {
	if (this.activities != null) {
	    this.activities.put(jour, activities);
	} else {
	    this.activities = new LinkedHashMap<String, List<ActivityCalendar>>();
	}
    }

    public void setDateDebutString(final String dateDebutString) {
	this.dateDebutString = dateDebutString;
    }

    public void setDateFinCoursString(final String dateFinCoursString) {
	this.dateFinCoursString = dateFinCoursString;
    }

    public void setJoursRemplaces(List<JoursRemplaces> joursRemplaces) {
	this.joursRemplaces = joursRemplaces;
    }

    public void setLongName(final String longName) {
	this.longName = longName;
    }

    public void setShortName(final String shortName) {
	this.shortName = shortName;
    }

    @Override
    public String toString() {
	return this.longName;
    }

    public int getMaxActivities() {
	return maxActivities;
    }

    public void setMaxActivities(int maxActivities) {
	this.maxActivities = maxActivities;
    }

    public ContentValues getContentValues() {

	final ContentValues cv = new ContentValues();
	cv.put(SessionTableHelper.SESSIONS_SHORT_NAME, shortName);
	cv.put(SessionTableHelper.SESSIONS_LONG_NAME, longName);
	cv.put(SessionTableHelper.SESSIONS_DATE_DEBUT, dateDebutString);
	cv.put(SessionTableHelper.SESSIONS_DATE_FIN, dateFinString);
	cv.put(SessionTableHelper.SESSIONS_DATE_FIN_COURS, dateFinCoursString);
	cv.put(SessionTableHelper.SESSIONS_USER_ID, user_id);
	return cv;
    }

    public void setId(long id) {
    }

    public void setUserId(String username) {
	this.user_id = username;

    }
}
