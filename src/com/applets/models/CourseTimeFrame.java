package com.applets.models;

public class CourseTimeFrame extends TimeFrame {
	private String day;
	private String type;

	public String getDay() {
		return day;
	}

	public String getType() {
		return type;
	}

	public void setDay(final String day) {
		this.day = day;
	}

	public void setType(final String type) {
		this.type = type;
	}
}
