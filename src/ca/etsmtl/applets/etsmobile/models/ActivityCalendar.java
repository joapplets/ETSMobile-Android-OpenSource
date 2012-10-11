package ca.etsmtl.applets.etsmobile.models;

import java.sql.Date;

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
	
	@SerializedName("sigle")
	private String cours;
	
	
	
	public String getStartDate() {
		return startDate;
	}
	
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCours() {
		return cours;
	}
	public void setCours(String cours) {
		this.cours = cours;
	}
	
	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}
	
}
