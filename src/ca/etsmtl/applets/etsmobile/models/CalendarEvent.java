package ca.etsmtl.applets.etsmobile.models;

import java.util.Date;

public class CalendarEvent {
	
	private Date startDate;
	private Date endDate;
	private String name;
	private String location;
	private String cours;
	private String  notes;
	private int  eventColor;
	private String  eventType;
	private boolean userMade;
	
	public CalendarEvent(String name, String eventType, String location,  int eventColor, Date startDate, Date endDate)
	{
		this.name = name;
		this.eventType = eventType;
		this.location = location;
		this.eventColor = eventColor;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCours() {
		return cours;
	}
	public void setCours(String cours) {
		this.cours = cours;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public int getEventColor() {
		return eventColor;
	}
	public void setEventColor(int eventColor) {
		this.eventColor = eventColor;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public boolean isUserMade() {
		return userMade;
	}
	public void setUserMade(boolean userMade) {
		this.userMade = userMade;
	}
	
	

}
