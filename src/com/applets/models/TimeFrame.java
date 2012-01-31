package com.applets.models;

import java.sql.Time;

public abstract class TimeFrame {
	private Time endTime;
	private String room;
	private Time startTime;

	public Time getEndTime() {
		return endTime;
	}

	public String getRoom() {
		return room;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setEndTime(final Time endTime) {
		this.endTime = endTime;
	}

	public void setRoom(final String room) {
		this.room = room;
	}

	public void setStartTime(final Time startTime) {
		this.startTime = startTime;
	}
}
