package ca.etsmtl.applets.etsmobile.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

import com.google.gson.annotations.SerializedName;

@Data
public class Session {
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
	
	@Override
	public String toString() {
		return getLongName();
	}
}
