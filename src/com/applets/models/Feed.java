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
	private String image = "";
	private String lang;
	private String name;
	private String type;
	private String url;

	public Feed(final long id, final String name, final String url,
			final String image, final String lang) {
		this(name, url, image, lang);
		setId(id);
	}

	public Feed(final String name, final String url, final String image,
			final String lang) {
		this.name = name;
		this.url = url;
		this.image = image;
		this.lang = lang;
	}

	@Override
	public int compareTo(final Model another) {
		return 0;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public long getId() {
		return id;
	}

	public String getImage() {
		return image;
	}

	public String getLang() {
		return lang;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	public void setLang(final String lang) {
		this.lang = lang;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public void setUrl(final String url) {
		this.url = "http://" + url;
	}

	@Override
	public ContentValues setValues() {

		_values.put("name", getName());
		_values.put("url", getUrl());
		_values.put("type", getType());
		_values.put("image", getImage());
		_values.put("lang", getLang());
		return _values;
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		dest.writeString(name);
		dest.writeString(type);
		dest.writeString(url);
		dest.writeString(lang);
	}
}
