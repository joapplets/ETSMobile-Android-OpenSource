package ca.etsmtl.applets.etsmobile.tools.db;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import ca.etsmtl.applets.etsmobile.models.BottinEntry;

public class BottinDBAdapter {

	private static SQLDBHelper db;
	private static final String DB_NAME = SQLDBHelper.DB_NAME;

	// db info.
	private static final int DB_VERSION = 1;
	private static BottinDBAdapter instance;

	public static BottinDBAdapter getInstance(final Context c) {
		if (BottinDBAdapter.instance == null) {
			BottinDBAdapter.instance = new BottinDBAdapter(c);
		}
		return BottinDBAdapter.instance;
	}

	private BottinDBAdapter(Context c) {
		// create the db, if it's already in the filesystem it will not
		// recreate it
		BottinDBAdapter.db = new SQLDBHelper(c, BottinDBAdapter.DB_NAME, null,
				BottinDBAdapter.DB_VERSION);
		BottinDBAdapter.db.close();
		// db.onUpgrade(db.getWritableDatabase(), DB_VERSION, 2);
	}

	public List<BottinEntry> getAllEntries() {
		final List<BottinEntry> list = new ArrayList<BottinEntry>();

		// set the database to be readable
		final SQLiteDatabase readDB = BottinDBAdapter.db.getReadableDatabase();

		// Equivalent to : SELECT * FROM BOTTIN ORDER BY SERVICE DESC
		final Cursor c = readDB.query(SQLDBHelper.BOTTIN_TABLE,
				new String[] { "*" }, null, null, null, null,
				SQLDBHelper.BOTTIN_SERVICE + " DESC");

		BottinEntry entry;
		while (c.moveToNext()) {

			final long id = c.getLong(c.getColumnIndex(SQLDBHelper.BOTTIN_ID));
			final String nom = c.getString(c
					.getColumnIndex(SQLDBHelper.BOTTIN_NOM));
			final String prenom = c.getString(c
					.getColumnIndex(SQLDBHelper.BOTTIN_PRENOM));
			final String telBureau = c.getString(c
					.getColumnIndex(SQLDBHelper.BOTTIN_TELBUREAU));
			final String emplacement = c.getString(c
					.getColumnIndex(SQLDBHelper.BOTTIN_EMPLACEMENT));
			final String email = c.getString(c
					.getColumnIndex(SQLDBHelper.BOTTIN_EMAIL));
			final String service = c.getString(c
					.getColumnIndex(SQLDBHelper.BOTTIN_SERVICE));
			final String titre = c.getString(c
					.getColumnIndex(SQLDBHelper.BOTTIN_TIRE));
			final String date = c.getString(c
					.getColumnIndex(SQLDBHelper.BOTTIN_DATE_MODIF));

			Date date_modif = new Date(System.currentTimeMillis());
			try {
				date_modif = new Date(parseDateString(date));
			} catch (Exception e) {
				e.printStackTrace();
			}

			entry = new BottinEntry(id, nom, prenom, telBureau, emplacement,
					email, service, titre, date_modif);

			list.add(entry);
		}
		c.close();
		readDB.close();
		return list;

	}

	private long parseDateString(String date) {
		// format : 2012-01-01T12:00:00

		String[] split = date.split("T");
		String[] date_components = split[0].split("-");
		String[] time_components = split[1].split(":");

		int second = Integer.parseInt(time_components[2]);
		int minute = Integer.parseInt(time_components[1]);
		int hourOfDay = Integer.parseInt(time_components[0]);
		// hourOfDay = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;

		int day = Integer.parseInt(date_components[2]);
		int month = Integer.parseInt(date_components[1]);
		int year = Integer.parseInt(date_components[0]);

		Calendar c = Calendar.getInstance(Locale.CANADA_FRENCH);
		c.set(year, month, day, hourOfDay, minute, second);

		return c.getTime().getTime();
	}

	/**
	 * Insert full list with a transaction, should be faster than multiple
	 * single insert
	 * 
	 * @param list
	 *            To Insert
	 * @return true if all went ok
	 */
	public boolean insertAll(List<BottinEntry> list) {
		SQLiteDatabase wDB = db.getWritableDatabase();
		try {
			wDB.beginTransaction();

			for (BottinEntry bottinEntry : list) {
				if (mustInsertNewEntry(bottinEntry)) {
					ContentValues cv = bottinEntry.getContentValues();
					wDB.insert(SQLDBHelper.BOTTIN_TABLE, null, cv);
				}
			}
			wDB.setTransactionSuccessful();
			wDB.endTransaction();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	/**
	 * Check if date_modif is after today
	 * 
	 * @param o
	 *            the entry to check
	 * @return true if needed to insert
	 */
	private boolean mustInsertNewEntry(final BottinEntry o) {
		return o.getDate_modif().after(new Date(System.currentTimeMillis()));
	}

	/**
	 * Returns a cursor containing all elements 
	 * Ordered by Service, Tite, Nom
	 * 
	 * @return
	 */
	public Cursor getAll() {
		// set the database to be readable
		final SQLiteDatabase readDB = BottinDBAdapter.db.getReadableDatabase();

		// Equivalent to : SELECT * FROM BOTTIN ORDER BY  TITRE ASC SERVICE ASC NOM ASC
		final Cursor c = readDB.query(SQLDBHelper.BOTTIN_TABLE,
				new String[] { "*" }, null, null, null, null,
				SQLDBHelper.BOTTIN_TIRE + " ASC, " + SQLDBHelper.BOTTIN_SERVICE + " ASC, " + SQLDBHelper.BOTTIN_NOM + " ASC");
		return c;
	}

	public Cursor getAllWhere(String constraint) {
		final SQLiteDatabase readDB = BottinDBAdapter.db.getReadableDatabase();

		String filter_txt = "%" + constraint + "%";
		final Cursor c = readDB.query(SQLDBHelper.BOTTIN_TABLE,
				new String[] { "*" },
				"nom like ? OR prenom like ? OR service like ?", new String[] {
						filter_txt, filter_txt, filter_txt }, null, null,
				SQLDBHelper.BOTTIN_NOM + " ASC");
		return c;
	}

	public BottinEntry getEntry(long id) {
		SQLiteDatabase rDB = db.getReadableDatabase();

		Cursor result = rDB.query(SQLDBHelper.BOTTIN_TABLE,
				new String[] { "*" }, SQLDBHelper.BOTTIN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		BottinEntry b = null;
		while (result.moveToNext()) {

			// final long id =
			// c.getLong(c.getColumnIndex(SQLDBHelper.BOTTIN_ID));
			final String nom = result.getString(result
					.getColumnIndex(SQLDBHelper.BOTTIN_NOM));
			final String prenom = result.getString(result
					.getColumnIndex(SQLDBHelper.BOTTIN_PRENOM));
			final String telBureau = result.getString(result
					.getColumnIndex(SQLDBHelper.BOTTIN_TELBUREAU));
			final String emplacement = result.getString(result
					.getColumnIndex(SQLDBHelper.BOTTIN_EMPLACEMENT));
			final String email = result.getString(result
					.getColumnIndex(SQLDBHelper.BOTTIN_EMAIL));
			final String service = result.getString(result
					.getColumnIndex(SQLDBHelper.BOTTIN_SERVICE));
			final String titre = result.getString(result
					.getColumnIndex(SQLDBHelper.BOTTIN_TIRE));
			final String date = result.getString(result
					.getColumnIndex(SQLDBHelper.BOTTIN_DATE_MODIF));

			Date date_modif = new Date(System.currentTimeMillis());
			try {
				date_modif = new Date(parseDateString(date));
			} catch (Exception e) {
				e.printStackTrace();
			}

			b = new BottinEntry(id, nom, prenom, telBureau, emplacement, email,
					service, titre, date_modif);
		}

		return b;
	}
}
