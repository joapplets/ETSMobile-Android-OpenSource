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

	
	public String getDateOrigineString() {
		return dateOrigineString;
	}

	public void setDateOrigineString(String dateOrigine) {
		this.dateOrigineString = dateOrigine;
	}

	public String getDateRemplacementString() {
		return dateRemplacementString;
	}

	public void setDateRemplacementString(String dateRemplacement) {
		this.dateRemplacementString = dateRemplacement;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
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



}
