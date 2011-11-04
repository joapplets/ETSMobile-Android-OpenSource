package com.applets.models;

import android.content.ContentValues;
import android.os.Parcel;

/**
 * Represent a Feed from witch we can retreive news
 * 
 * @author Philippe David
 * 
 */
public class Feed extends Model {

    private long id;
    private String name;
    private String url;
    private String type;
    private String lang;
    private String image = "";

    public Feed(String name, String url, String image, String lang) {
	this.name = name;
	this.url = url;
	this.image = image;
	this.lang = lang;
    }

    public Feed(long id, String name, String url, String image, String lang) {
	this(name, url, image, lang);
	setId(id);
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
	dest.writeString(lang);
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
    /**
     * save this code
     */
    // @Override
    // public Feed createFromParcel(Parcel source) {
    // return initFromParcel(source);
    // }
    //
    // @Override
    // public Feed[] newArray(int size) {
    // return new Feed[size];
    // }
    //
    // @Override
    // public Feed initFromParcel(Parcel source) {
    // name = source.readString();
    // type = source.readString();
    // url = source.readString();
    // lang = source.readString();
    // return new Feed(name, url, image, lang);
    // }
}
