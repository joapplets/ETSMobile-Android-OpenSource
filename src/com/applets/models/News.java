package com.applets.models;

import android.content.ContentValues;

public class News extends Model {

    private String title;
    private String url;
    private String description;
    private String image;

    public News() {
	title = "";
    }

    @Override
    public String toString() {
	return getTitle();
    }

    @Override
    public int compareTo(Model another) {
	final News art = (News) another;
	int result = 0;

	if (!art.url.equals(this.url)) {
	    result = -1;
	}

	return result;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public void setUrl(String link) {
	this.url = link;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public void setImage(String image) {
	this.image = image;
    }

    public String getImage() {
	return image;
    }

    public String getUrl() {
	return url;
    }

    public String getTitle() {
	return title;
    }

    public String getDescription() {
	return (description == null) ? "" : description;
    }

    @Override
    public ContentValues setValues() {
	this._values.put("title", getTitle());
	this._values.put("url", getUrl());
	this._values.put("description", getDescription());
	this._values.put("url", getUrl());

	return this._values;
    }

}
