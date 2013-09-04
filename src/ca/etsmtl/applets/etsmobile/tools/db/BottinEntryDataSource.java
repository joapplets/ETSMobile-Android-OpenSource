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