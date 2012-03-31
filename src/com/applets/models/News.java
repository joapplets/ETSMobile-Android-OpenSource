package com.applets.models;

public class News {

	private Long pubDate;
	private String title, description, guid, source;

	public String getDescription() {
		return description;
	}

	public String getGuid() {
		return guid;
	}

	public long getPubDate() {
		return pubDate;
	}

	public String getSource() {
		return source;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setGuid(final String guid) {
		this.guid = guid;
	}

	public void setPubDate(final long date) {
		pubDate = date;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	public void setTitle(final String title) {
		this.title = title;
	}
}
