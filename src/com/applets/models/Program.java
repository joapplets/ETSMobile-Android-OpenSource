package com.applets.models;

import java.util.List;

import android.content.ContentValues;
import android.os.Parcel;

public class Program extends Model {
    
    private String name;
    private String shortName;
    private String description;
    private String url;
    private String urlPdf;
    private int programme_id;
    
    private List<Course> courses;

    public int getNumberOfCourses() {
	return courses.size();
    }

    public Program() {

    }

    public Program(String name, String shortName, String description,
	    String url, String url_pdf, int programme_id) {
	super();
	this.name = name;
	this.shortName = shortName;
	this.description = description;
	this.url = url;
	this.urlPdf = url_pdf;
	this.programme_id = programme_id;
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
	return programme_id;
    }

    public void setId(int id) {
	this.programme_id = id;
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

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(name);
	dest.writeString(shortName);
	dest.writeString(description);
	dest.writeString(url);
	dest.writeString(urlPdf);

    }
}
