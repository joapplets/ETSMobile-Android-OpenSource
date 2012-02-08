package com.applets.models;

import android.content.ContentValues;
import android.os.Parcel;

public class Channel extends Model {

	private String title;
	private String link;
	private String description;
	private String lang;


	@Override
	public int compareTo(Model m) {
		boolean c = title.equalsIgnoreCase(((Channel) m).getTitle());
		return (c) ? 0 : -1;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	ContentValues setValues() {
		_values.put("title", title);
		_values.put("link", link);
		_values.put("description", description);
		_values.put("lang", lang);
		return _values;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
