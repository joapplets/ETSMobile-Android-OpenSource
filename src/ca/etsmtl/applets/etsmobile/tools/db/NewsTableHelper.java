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
package ca.etsmtl.applets.etsmobile.tools.db;

public class NewsTableHelper {

	// table name
	public static final String TABLE_NAME = "newsTable";

	// fields
	public static final String NEWS_ID = "_id";
	public static final String NEWS_TITLE = "title";
	public static final String NEWS_DATE = "date";
	public static final String NEWS_DESCRIPTION = "description";
	public static final String NEWS_GUID = "guid";
	public static final String NEWS_SOURCE = "source";
	public static final String NEWS_LINK = "link";

	// Database creation SQL statement
	public static final String CREATE_TABLE = "CREATE TABLE "
			+ NewsTableHelper.TABLE_NAME + " ( " + NewsTableHelper.NEWS_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ NewsTableHelper.NEWS_TITLE + " TEXT NOT NULL, "
			+ NewsTableHelper.NEWS_DATE + " LONG NOT NULL, "
			+ NewsTableHelper.NEWS_DESCRIPTION + " TEXT NOT NULL, "
			+ NewsTableHelper.NEWS_GUID + " TEXT NOT NULL, "
			+ NewsTableHelper.NEWS_LINK + " TEXT , "
			+ NewsTableHelper.NEWS_SOURCE
			+ " TEXT NOT NULL, UNIQUE (title) ON CONFLICT REPLACE);";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ NewsTableHelper.TABLE_NAME;

	public static final String[] AVAILABLE = new String[] {
			NewsTableHelper.NEWS_ID, NewsTableHelper.NEWS_TITLE,
			NewsTableHelper.NEWS_DATE, NewsTableHelper.NEWS_DESCRIPTION,
			NewsTableHelper.NEWS_GUID, NewsTableHelper.NEWS_LINK,
			NewsTableHelper.NEWS_SOURCE };

}
