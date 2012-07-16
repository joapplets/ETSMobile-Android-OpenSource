package ca.etsmtl.applets.etsmobile.models;

import java.util.Date;

import android.content.ContentValues;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTable;

public class News extends Model {

	private String title, description, guid, source, link;
	private Date date;

	public News() {
	}

	public News(String title, String description, String guid, String source,
			Date date, String link) {
		this.title = title;
		this.description = description;
		this.guid = guid;
		this.source = source;
		this.date = date;
		this.link = link;
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

	public void setPubDate(long date) {
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

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues values = new ContentValues();
		values.put(NewsTable.NEWS_TITLE, getTitle());
		values.put(NewsTable.NEWS_DATE, getPubDate().getTime());
		values.put(NewsTable.NEWS_DESCRIPTION, getDescription());
		values.put(NewsTable.NEWS_GUID, getGuid());
		values.put(NewsTable.NEWS_SOURCE, getSource());
		values.put(NewsTable.NEWS_LINK, getLink());

		return values;

	}
}
