package com.applets.models;

import android.content.ContentValues;
import android.os.Parcel;

public class Channel extends Model {

	private String description;
	private String lang;
	private String link;
	private String title;

	@Override
	public int compareTo(final Model m) {
		final boolean c = title.equalsIgnoreCase(((Channel) m).getTitle());
		return c ? 0 : -1;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getDescription() {
		return description;
	}

	public String getLang() {
		return lang;
	}

	public String getLink() {
		return link;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setLang(final String lang) {
		this.lang = lang;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	ContentValues setValues() {
		_values.put("title", title);
		_values.put("link", link);
		_values.put("description", description);
		_values.put("lang", lang);
		return _values;
	}

	@Override
	public void writeToParcel(final Parcel arg0, final int arg1) {
		// TODO Auto-generated method stub

	}

}
