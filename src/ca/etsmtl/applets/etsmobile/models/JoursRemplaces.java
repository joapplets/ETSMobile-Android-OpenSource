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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import ca.etsmtl.applets.etsmobile.tools.db.JoursRemplaceTableHelper;

import com.google.gson.annotations.SerializedName;

public class JoursRemplaces {

	@SerializedName("dateOrigine")
	String dateOrigineString;

	@SerializedName("dateRemplacement")
	String dateRemplacementString;

	@SerializedName("description")
	String description;
	long session_id;

	public JoursRemplaces(Cursor mJourCursor) {
		dateOrigineString = mJourCursor.getString(mJourCursor
				.getColumnIndex(JoursRemplaceTableHelper.DATE_ORIGIN));
		dateRemplacementString = mJourCursor.getString(mJourCursor
				.getColumnIndex(JoursRemplaceTableHelper.DATE_REMPLACEMENT));
		description = mJourCursor.getString(mJourCursor
				.getColumnIndex(JoursRemplaceTableHelper.DESC));
		session_id = mJourCursor.getInt(mJourCursor
				.getColumnIndex(JoursRemplaceTableHelper.SESSION_ID));
	}

	public Date getDateOrigine() {
		SimpleDateFormat formatter;
		Date date;
		formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA_FRENCH);
		try {
			date = formatter.parse(this.dateOrigineString);
		} catch (final ParseException e) {
			date = null;
		}

		return date;
	}

	public String getDateOrigineString() {
		return dateOrigineString;
	}

	public Date getDateRemplacement() {
		SimpleDateFormat formatter;
		Date date;
		formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA_FRENCH);
		try {
			date = formatter.parse(this.dateRemplacementString);
		} catch (final ParseException e) {
			date = null;
		}

		return date;
	}

	public String getDateRemplacementString() {
		return dateRemplacementString;
	}

	public String getDescription() {
		return description;
	}

	public void setDateOrigineString(String dateOrigine) {
		this.dateOrigineString = dateOrigine;
	}

	public void setDateRemplacementString(String dateRemplacement) {
		this.dateRemplacementString = dateRemplacement;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ContentValues getContentValues() {
		final ContentValues cv = new ContentValues();
		cv.put(JoursRemplaceTableHelper.DATE_ORIGIN, dateOrigineString);
		cv.put(JoursRemplaceTableHelper.DATE_REMPLACEMENT,
				dateRemplacementString);
		cv.put(JoursRemplaceTableHelper.DESC, description);
		cv.put(JoursRemplaceTableHelper.SESSION_ID, session_id);
		return cv;
	}

	public void setSessionId(long id) {
		this.session_id = id;

	}

	public void setId(long id) {

	}
}
