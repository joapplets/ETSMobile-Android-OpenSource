package com.applets.models;

import java.util.List;

import android.content.ContentValues;

public class Program extends Model {
    private String name;
    private String shortName;
    private String description;
    private List<Course> courses;
    private String url;
    private String urlPdf;
    private int id;

    public int getNumberOfCourses() {
	return courses.size();
    }

    public Program() {

    }

    public Program(String name, String shortName, String description) {
	super();
	this.name = name;
	this.shortName = shortName;
	this.description = description;
    }

    public void addCourse(Course course) {
	courses.add(course);
    }

    public boolean removeCourse(Course course) {
	return courses.remove(course);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getShortName() {
	return shortName;
    }

    public void setShortName(String shortName) {
	this.shortName = shortName;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public int compareTo(Model arg0) {
	// TODO Auto-generated method stub
	return 0;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getUrlPdf() {
	return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
	this.urlPdf = urlPdf;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    @Override
    ContentValues setValues() {
	this._values.put("name", getName());
	this._values.put("shortName", getShortName());
	this._values.put("description", getDescription());
	this._values.put("url", getUrl());
	this._values.put("urlPdf", getUrlPdf());
	return this._values;
    }

}
