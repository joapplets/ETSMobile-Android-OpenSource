package com.applets.models;

import java.util.List;

public class CourseGroup {
    private int groupNumber;
    private String lecturer;
    private List<TimeFrame> listTimeFrame;

    public boolean removeTimeFrame(TimeFrame timeFrame) {
	return listTimeFrame.remove(timeFrame);
    }

    public void addTimeFrame(TimeFrame timeFrame) {
	listTimeFrame.add(timeFrame);
    }

    public int getGroupNumber() {
	return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
	this.groupNumber = groupNumber;
    }

    public String getLecturer() {
	return lecturer;
    }

    public void setLecturer(String lecturer) {
	this.lecturer = lecturer;
    }
}
