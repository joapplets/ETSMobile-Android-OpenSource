package com.applets.models;

import java.util.List;

public class Semester {
    private List<Course> courses;
    private List<CourseGroup> listCourseGroup;
    private String season;
    private int year;

    public void addCourse(final Course course) {
	courses.add(course);
    }

    public void addCourseGroup(final CourseGroup courseGroup) {
	listCourseGroup.add(courseGroup);
    }

    public int getNumberOfCourses() {
	return courses.size();
    }

    public String getSeason() {
	return season;
    }

    public int getYear() {
	return year;
    }

    public boolean removeCourse(final Course course) {
	return courses.remove(course);
    }

    public boolean removeCourseGroup(final CourseGroup courseGroup) {
	return listCourseGroup.remove(courseGroup);
    }

    public void setSeason(final String season) {
	this.season = season;
    }

    public void setYear(final int year) {
	this.year = year;
    }
}
