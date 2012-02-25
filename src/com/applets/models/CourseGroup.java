package com.applets.models;

import java.util.List;

public class CourseGroup {
	private int groupNumber;
	private String lecturer;
	private List<TimeFrame> listTimeFrame;

	public void addTimeFrame(final TimeFrame timeFrame) {
		listTimeFrame.add(timeFrame);
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public String getLecturer() {
		return lecturer;
	}

	public boolean removeTimeFrame(final TimeFrame timeFrame) {
		return listTimeFrame.remove(timeFrame);
	}

	public void setGroupNumber(final int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public void setLecturer(final String lecturer) {
		this.lecturer = lecturer;
	}
}
