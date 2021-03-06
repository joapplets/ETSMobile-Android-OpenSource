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

public class SessionTableHelper {

	// table name
	public static final String TABLE_NAME = "sessionsTable";

	// fields
	public static final String SESSIONS_ID = "_id";
	public static final String SESSIONS_SHORT_NAME = "shortName";
	public static final String SESSIONS_LONG_NAME = "longName";
	public static final String SESSIONS_DATE_DEBUT = "dateDebutString";
	public static final String SESSIONS_DATE_FIN = "dateFinString";
	public static final String SESSIONS_DATE_FIN_COURS = "DateFinCoursString";
	public static final String SESSIONS_MAX_ACT = "maxActivities";
	public static final String SESSIONS_USER_ID = "user_id";

	// Database creation SQL statement
	public static final String CREATE_TABLE = "CREATE TABLE "
			+ SessionTableHelper.TABLE_NAME + " ( "
			+ SessionTableHelper.SESSIONS_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ SessionTableHelper.SESSIONS_SHORT_NAME + " TEXT, "
			+ SessionTableHelper.SESSIONS_LONG_NAME + " TEXT, "
			+ SessionTableHelper.SESSIONS_DATE_DEBUT + " TEXT, "
			+ SessionTableHelper.SESSIONS_DATE_FIN + " TEXT, "
			+ SessionTableHelper.SESSIONS_USER_ID + " TEXT, "
			+ SessionTableHelper.SESSIONS_MAX_ACT + " INT, "
			+ SessionTableHelper.SESSIONS_DATE_FIN_COURS
			+ " TEXT, UNIQUE (shortName) ON CONFLICT REPLACE);";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ SessionTableHelper.TABLE_NAME;

	public static final String[] AVAILABLE = new String[] {
			SessionTableHelper.SESSIONS_ID,
			SessionTableHelper.SESSIONS_SHORT_NAME,
			SessionTableHelper.SESSIONS_LONG_NAME,
			SessionTableHelper.SESSIONS_DATE_DEBUT,
			SessionTableHelper.SESSIONS_DATE_FIN,
			SessionTableHelper.SESSIONS_MAX_ACT,
			SessionTableHelper.SESSIONS_DATE_FIN_COURS, SESSIONS_USER_ID };
}
