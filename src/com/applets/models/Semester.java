package com.applets.models;

import java.util.List;

public class Semester {
    private String season;
    private int year;
    private List<Course> courses;
    private List<CourseGroup> listCourseGroup;

    public void addCourseGroup(CourseGroup courseGroup) {
	listCourseGroup.add(courseGroup);
    }

    public boolean removeCourseGroup(CourseGroup courseGroup) {
	return listCourseGroup.remove(courseGroup);
    }

    public int getNumberOfCourses() {
	return courses.size();
    }

    public void addCourse(Course course) {
	courses.add(course);
    }

    public boolean removeCourse(Course course) {
	return courses.remove(course);
    }

    public String getSeason() {
	return season;
    }

    public void setSeason(String season) {
	this.season = season;
    }

    public int getYear() {
	return year;
    }

    public void setYear(int year) {
	this.year = year;
    }
}
