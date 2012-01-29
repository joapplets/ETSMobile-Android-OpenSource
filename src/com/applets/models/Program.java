package com.applets.models;

import java.util.List;

import android.content.ContentValues;
import android.os.Parcel;

import com.applets.utils.db.ProgrammesDbAdapter;

public class Program extends Model {

    private List<Course> courses;
    private String description;
    private String name;
    private int programme_id;
    private String shortName;
    private String url;

    private String urlPdf;

    public Program() {

    }

    public Program(final String name, final String shortName,
	    final String description, final String url, final String url_pdf,
	    final int programme_id) {
	super();
	this.name = name;
	this.shortName = shortName;
	this.description = description;
	this.url = url;
	urlPdf = url_pdf;
	this.programme_id = programme_id;
    }

    public void addCourse(final Course course) {
	courses.add(course);
    }

    @Override
    public int compareTo(final Model arg0) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int describeContents() {
	return 0;
    }

    public String getDescription() {
	return description;
    }

    public int getId() {
	return programme_id;
    }

    public String getName() {
	return name;
    }

    public int getNumberOfCourses() {
	return courses.size();
    }

    public String getShortName() {
	return shortName;
    }

    public String getUrl() {
	return url;
    }

    public String getUrlPdf() {
	return urlPdf;
    }

    public boolean removeCourse(final Course course) {
	return courses.remove(course);
    }

    public void setDescription(final String description) {
	this.description = description;
    }

    public void setId(final int id) {
	programme_id = id;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public void setShortName(final String shortName) {
	this.shortName = shortName;
    }

    public void setUrl(final String url) {
	this.url = url;
    }

    public void setUrlPdf(final String urlPdf) {
	this.urlPdf = urlPdf;
    }

    @Override
    ContentValues setValues() {
	_values.put(ProgrammesDbAdapter.KEY_NAME, getName());
	_values.put(ProgrammesDbAdapter.KEY_SHORT_NAME, getShortName());
	_values.put(ProgrammesDbAdapter.KEY_DESCRIPTION, getDescription());
	_values.put(ProgrammesDbAdapter.KEY_URL, getUrl());
	_values.put(ProgrammesDbAdapter.KEY_URL_PDF, getUrlPdf());
	return _values;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
	dest.writeString(name);
	dest.writeString(shortName);
	dest.writeString(description);
	dest.writeString(url);
	dest.writeString(urlPdf);

    }
}
