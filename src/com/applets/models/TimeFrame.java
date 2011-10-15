package com.applets.models;

import android.text.format.Time;

public abstract class TimeFrame {
    private String room;
    private Time startTime;
    private Time endTime;

    public String getRoom() {
	return room;
    }

    public void setRoom(String room) {
	this.room = room;
    }

    public Time getStartTime() {
	return startTime;
    }

    public void setStartTime(Time startTime) {
	this.startTime = startTime;
    }

    public Time getEndTime() {
	return endTime;
    }

    public void setEndTime(Time endTime) {
	this.endTime = endTime;
    }
}
