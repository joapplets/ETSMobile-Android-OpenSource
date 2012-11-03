package ca.etsmtl.applets.etsmobile.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Session implements Serializable, Comparable<Session> {

	private static final long serialVersionUID = -632822145233952231L;

	@SerializedName("abrege")
	private String shortName;

	@SerializedName("auLong")
	private String longName;

	@SerializedName("dateDebut")
	private String dateDebutString;

	@SerializedName("dateFin")
	private String dateFinString;

	@SerializedName("dateFinCours")
	private String dateFinCoursString;

	private List<ActivityCalendar> activities;

	@Override
	public int compareTo(final Session s) {
		// TODO Auto-generated method stu

		return s.getDateDebut().compareTo(s.getDateFin());
	}

	public List<ActivityCalendar> getActivities() {
		return activities;
	}

	public Date getDateDebut() {
		SimpleDateFormat formatter;
		Date date;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = formatter.parse(getDateDebutString());
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
			date = formatter.parse(getDateFinString());
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
			date = formatter.parse(getDateFinCoursString());
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

	public String getLongName() {
		return longName;
	}

	public String getShortName() {
		return shortName;
	}

	public void removeDuplicates() {
		final List<ActivityCalendar> removed = new ArrayList<ActivityCalendar>();

		ActivityCalendar activity, anotherActivity;

		for (int i = 0; i < activities.size() - 1; i++) {
			activity = activities.get(i);
			anotherActivity = activities.get(i + 1);

			if (activity.compareTo(anotherActivity) == 0) {
				if (activity.getStartDate().compareTo(
						anotherActivity.getStartDate()) == 0
						&& activity.getEndDate().compareTo(
								anotherActivity.getEndDate()) == 0
						&& activity.getLocation().compareTo(
								anotherActivity.getLocation()) != 0) {
					activity.setLocation(activity.getLocation() + "; "
							+ anotherActivity.getLocation());
					removed.add(anotherActivity);
				}

			}

		}

		activities.removeAll(removed);

	}

	public void setActivities(final List<ActivityCalendar> activities) {
		this.activities = activities;
	}

	public void setDateDebutString(final String dateDebutString) {
		this.dateDebutString = dateDebutString;
	}

	public void setDateFinCoursString(final String dateFinCoursString) {
		this.dateFinCoursString = dateFinCoursString;
	}

	public void setLongName(final String longName) {
		this.longName = longName;
	}

	public void setShortName(final String shortName) {
		this.shortName = shortName;
	}

	@Override
	public String toString() {
		return getLongName();
	}
}
