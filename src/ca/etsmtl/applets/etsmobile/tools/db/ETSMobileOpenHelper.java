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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ETSMobileOpenHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 3;
	public static final String DB_NAME = "app_ets_db";

	public ETSMobileOpenHelper(final Context context) {
		super(context, ETSMobileOpenHelper.DB_NAME, null,
				ETSMobileOpenHelper.DB_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL(BottinTableHelper.CREATE_TABLE);
		db.execSQL(NewsTableHelper.CREATE_TABLE);
		db.execSQL(ActivityCalendarTableHelper.CREATE_TABLE);
		db.execSQL(JoursRemplaceTableHelper.CREATE_TABLE);
		db.execSQL(SessionTableHelper.CREATE_TABLE);

	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
			final int newVersion) {
		db.execSQL(NewsTableHelper.DROP_TABLE);
		db.execSQL(BottinTableHelper.DROP_TABLE);
		db.execSQL(ActivityCalendarTableHelper.DROP_TABLE);
		db.execSQL(JoursRemplaceTableHelper.DROP_TABLE);
		db.execSQL(SessionTableHelper.DROP_TABLE);
		onCreate(db);

	}

}
