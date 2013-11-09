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
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import ca.etsmtl.applets.etsmobile.models.BottinEntry;

public class BottinEntryDataSource {

	// Database fields
	private SQLiteDatabase database;
	private final ETSMobileOpenHelper dbHelper;
	public static final String[] allColumns = { BottinTableHelper.BOTTIN__ID,
			BottinTableHelper.BOTTIN_NOM, BottinTableHelper.BOTTIN_PRENOM };

	public BottinEntryDataSource(Context context) {
		dbHelper = new ETSMobileOpenHelper(context);
		open();
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void deleteBottinEntry(BottinEntry BottinEntry) {
		final long id = BottinEntry.getId();
		System.out.println("BottinEntry deleted with id: " + id);
		database.delete(BottinTableHelper.TABLE_NAME,
				BottinTableHelper.BOTTIN__ID + " = " + id, null);
	}

	public Cursor getAllAsCursor() {

		final Cursor cursor = database.query(BottinTableHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);
		return cursor;
	}

	public Cursor getAllAsCursor(String[] projection) {
		return getAllAsCursor();
	}

	public Cursor getAllAsCursor(String where, String[] args) {
		return getAllAsCursor(null, where, args, null, null, null);
	}

	private Cursor getAllAsCursor(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {

		return database.query(BottinTableHelper.TABLE_NAME, columns, selection,
				selectionArgs, groupBy, having, orderBy);
	}
}
