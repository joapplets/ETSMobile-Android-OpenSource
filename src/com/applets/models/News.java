package com.applets.models;

import android.content.ContentValues;
import android.os.Parcel;

public class News extends Model {

    private String _id;
    private String title = "";
    private String url = "";
    private String description = "";
    private String image = "";
    private String creator = "";
    private int feed_id;
    private String pubDate = "";

    public News() {
    }

    public News(String _id, String title, String url, String description,
	    String image, String creator, int feed_id, String pubDate) {
	super();
	this._id = _id;
	this.title = title;
	this.url = url;
	this.description = description;
	this.image = image;
	this.creator = creator;
	this.feed_id = feed_id;
	this.pubDate = pubDate;
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

    /**
     * Returns the description of the news
     */
    public String getDescription() {
	return description;
    }

    public String getCreator() {
	return creator;
    }

    public String getPubDate() {
	return pubDate;
    }

    public int getFeedId() {
	return feed_id;
    }

    @Override
    public ContentValues setValues() {
	this._values.put("title", getTitle());
	this._values.put("url", getUrl());
	this._values.put("description", getDescription());
	this._values.put("url", getUrl());

	return this._values;
    }

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	// TODO Auto-generated method stub
    }

    public String get_id() {
	return _id;
    }

    public void set_id(String _id) {
	this._id = _id;
    }

    public void setCreator(String creator) {
	this.creator = creator;
    }

    public void setFeedId(String feed_id) {
	this.feed_id = Integer.parseInt(feed_id);

    }

    public void setPubDate(String pubDate) {
	this.pubDate = pubDate;

    }

}
