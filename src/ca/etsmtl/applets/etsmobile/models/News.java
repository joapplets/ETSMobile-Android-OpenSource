package ca.etsmtl.applets.etsmobile.models;

import java.util.Date;

public class News {
	
	private String title, description, guid, source;
	private Date date;
	
	public News(){}
	
	public News(String title, String description, String guid, String source, Date date){
		this.title = title;
		this.description = description;
		this.guid = guid;
		this.source = source;
		this.date = date;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setPubDate(Date date) {
		this.date = date;
	}
	
	public void setPubDate(long date){
		this.date = new Date(date);
	}

	public Date getPubDate() {
		return date;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getGuid() {
		return guid;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
