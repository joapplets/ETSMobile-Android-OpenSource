package com.applets.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represent a Feed from witch we can retreive news
 * 
 * @author Philippe David
 * 
 */
public class Feed extends Model implements Parcelable {

    private long id;
    private String name;
    private String url;
    private String type;
    private String lang;
    private String image = "";

    public Feed() {
	// TODO Auto-generated constructor stub
    }

    public Feed(long id, String name, String url, String image) {
	this.id = id;
	this.name = name;
	this.url = url;
	this.image = image;
    }

    public void setId(long id) {
	this.id = id;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setUrl(String url) {
	this.url = "http://" + url;
    }

    public void setType(String type) {
	this.type = type;
    }

    public void setLang(String lang) {
	this.lang = lang;
    }

    public void setImage(String image) {
	this.image = image;
    }

    public long getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public String getUrl() {
	return url;
    }

    public String getType() {
	return type;
    }

    public String getImage() {
	return image;
    }

    public String getLang() {
	return lang;
    }

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(name);
	dest.writeString(type);
	dest.writeString(url);

    }

    public static final Parcelable.Creator<Feed> CREATOR = new Creator<Feed>() {

	@Override
	public Feed[] newArray(int size) {
	    return new Feed[size];
	}

	@Override
	public Feed createFromParcel(Parcel source) {
	    return new Feed(source);
	}
    };

    private Feed(Parcel in) {
	name = in.readString();
	type = in.readString();
	url = in.readString();
    }

    @Override
    public int compareTo(Model another) {
	return 0;
    }

    @Override
    ContentValues setValues() {

	this._values.put("name", getName());
	this._values.put("url", getUrl());
	this._values.put("type", getType());
	this._values.put("image", getImage());
	this._values.put("lang", getLang());
	return this._values;
    }
}
