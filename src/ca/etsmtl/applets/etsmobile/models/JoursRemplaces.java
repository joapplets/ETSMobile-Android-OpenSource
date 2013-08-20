package ca.etsmtl.applets.etsmobile.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class JoursRemplaces {

	@SerializedName("dateOrigine")
	String dateOrigineString;

	@SerializedName("dateRemplacement")
	String dateRemplacementString;

	@SerializedName("description")
	String description;

	public Date getDateOrigine() {
		SimpleDateFormat formatter;
		Date date;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = formatter.parse(this.dateOrigineString);
		} catch (final ParseException e) {
			date = null;
		}

		return date;
	}

	public String getDateOrigineString() {
		return dateOrigineString;
	}

	public Date getDateRemplacement() {
		SimpleDateFormat formatter;
		Date date;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = formatter.parse(this.dateRemplacementString);
		} catch (final ParseException e) {
			date = null;
		}

		return date;
	}

	public String getDateRemplacementString() {
		return dateRemplacementString;
	}

	public String getDescription() {
		return description;
	}

	public void setDateOrigineString(String dateOrigine) {
		this.dateOrigineString = dateOrigine;
	}

	public void setDateRemplacementString(String dateRemplacement) {
		this.dateRemplacementString = dateRemplacement;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
