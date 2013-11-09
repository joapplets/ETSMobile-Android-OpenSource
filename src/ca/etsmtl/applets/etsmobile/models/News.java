/*******************************************************************************
 * Copyright 2013 Club ApplETS
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ca.etsmtl.applets.etsmobile.models;

import java.util.Date;

import android.content.ContentValues;
import android.os.Parcel;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;

public class News extends Model {

	private String title, description, guid, source, link;
	private Date date;

	public News() {
	}

	public News(final String title, final String description,
			final String guid, final String source, final Date date,
			final String link) {
		this.title = title;
		this.description = description;
		this.guid = guid;
		this.source = source;
		this.date = date;
		this.link = link;
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues values = new ContentValues();
		values.put(NewsTableHelper.NEWS_TITLE, title);
		values.put(NewsTableHelper.NEWS_DATE, date.getTime());
		values.put(NewsTableHelper.NEWS_DESCRIPTION, description);
		values.put(NewsTableHelper.NEWS_GUID, guid);
		values.put(NewsTableHelper.NEWS_SOURCE, source);
		values.put(NewsTableHelper.NEWS_LINK, link);
		return values;
	}

	public String getDescription() {
		return description;
	}

	public String getGuid() {
		return guid;
	}

	public String getLink() {
		return link;
	}

	public Date getPubDate() {
		return date;
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

	public void setLink(final String link) {
		this.link = link;
	}

	public void setPubDate(final Date date) {
		this.date = date;
	}

	public void setPubDate(final long date) {
		this.date = new Date(date);
	}

	public void setSource(final String source) {
		this.source = source;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {

	}
}
