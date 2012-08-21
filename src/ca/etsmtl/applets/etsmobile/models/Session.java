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
	
	public Date getDateDebut() {
		SimpleDateFormat formatter ; 
		Date date; 
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = (Date)formatter.parse(getDateDebutString());
		} catch (ParseException e) {
			date = null;
		}
		
		return date;
	}
	

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getDateDebutString() {
		return dateDebutString;
	}

	public void setDateDebutString(String dateDebutString) {
		this.dateDebutString = dateDebutString;
	}
	
	@Override
	public String toString() {
		return getLongName();
	}
}
