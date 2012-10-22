package ca.etsmtl.applets.etsmobile.models;

import java.util.Date;

public class CalendarEvent {

	private Date startDate;
	private Date endDate;
	private String name;
	private String location;
	private String cours;
	private String notes;
	private int eventColor;
	private String eventType;
	private boolean userMade;

	public CalendarEvent(final String name, final String eventType,
			final String location, final int eventColor, final Date startDate,
			final Date endDate) {
		this.name = name;
		this.eventType = eventType;
		this.location = location;
		this.eventColor = eventColor;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getCours() {
		return cours;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getEventColor() {
		return eventColor;
	}

	public String getEventType() {
		return eventType;
	}

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public String getNotes() {
		return notes;
	}

	public Date getStartDate() {
		return startDate;
	}

	public boolean isUserMade() {
		return userMade;
	}

	public void setCours(final String cours) {
		this.cours = cours;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	public void setEventColor(final int eventColor) {
		this.eventColor = eventColor;
	}

	public void setEventType(final String eventType) {
		this.eventType = eventType;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setNotes(final String notes) {
		this.notes = notes;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public void setUserMade(final boolean userMade) {
		this.userMade = userMade;
	}

}
