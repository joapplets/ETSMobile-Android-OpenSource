package com.applets.utils.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.applets.models.Model;

/**
 * Base Adapter for querying the SQLite database. CRUD is definied here
 * 
 * @author Philippe David
 * 
 */
public abstract class BaseDbAdapter {

	protected static final String KEY_ROW_ID = "_id";
	protected Context context;
	protected SQLiteDatabase db;

	protected AppEtsOpenHelper dbHelper;

	public BaseDbAdapter(final Context context) {
		this.context = context;
	}

	/**
	 * Close current db and dbHelper
	 */
	public void close() {
		db.close();
		dbHelper.close();
	}

	public abstract long create(Model model);

	public abstract int delete(Model model);

	public abstract Cursor get(long rowId) throws SQLException;

	public abstract Cursor getAll();

	public BaseDbAdapter open() throws SQLException {
		dbHelper = new AppEtsOpenHelper(context, null);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public abstract int update(Model model);
}
