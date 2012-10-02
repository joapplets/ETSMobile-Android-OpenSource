package ca.etsmtl.applets.etsmobile.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Session implements Serializable {

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

	public String getDateFinString() {
		return dateFinString;
	}

	public String getLongName() {
		return longName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setDateDebutString(final String dateDebutString) {
		this.dateDebutString = dateDebutString;
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
