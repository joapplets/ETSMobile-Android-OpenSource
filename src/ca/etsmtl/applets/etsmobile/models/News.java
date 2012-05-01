package ca.etsmtl.applets.etsmobile.models;

public class News {
	
	private String title, description, guid, source;
	private Long pubDate;
	
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

	public void setPubDate(long date) {
		pubDate = date;
	}

	public long getPubDate() {
		return pubDate;
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
