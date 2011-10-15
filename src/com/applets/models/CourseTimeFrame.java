package com.applets.models;

public class CourseTimeFrame extends TimeFrame {
    private String type;
    private String day;

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getDay() {
	return day;
    }

    public void setDay(String day) {
	this.day = day;
    }
}
