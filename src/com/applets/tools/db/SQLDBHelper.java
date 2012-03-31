package com.applets.tools.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDBHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "app_ets_db.db";

	public final static String NEWS_DATE = "date";

	public final static String NEWS_DESCRIPTION = "description";
	public final static String NEWS_GUID = "guid";
	/**
	 * Columns name for the news table.
	 */
	public final static String NEWS_ID = "id";
	public final static String NEWS_SOURCE = "source";
	/**
	 * News table.
	 */
	public final static String NEWS_TABLE = "newsTable";
	/**
	 * SQL query to create the news table.
	 */
	private final static String NEWS_TABLE_QUERY =

	"CREATE TABLE " + SQLDBHelper.NEWS_TABLE + " ( " + SQLDBHelper.NEWS_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + SQLDBHelper.NEWS_TITLE
			+ " TEXT NOT NULL, " + SQLDBHelper.NEWS_DATE + " LONG NOT NULL, "
			+ SQLDBHelper.NEWS_DESCRIPTION + " TEXT NOT NULL, "
			+ SQLDBHelper.NEWS_GUID + " TEXT NOT NULL, "
			+ SQLDBHelper.NEWS_SOURCE + " TEXT NOT NULL);";

	public final static String NEWS_TITLE = "title";

	public SQLDBHelper(final Context context, final String name,
			final CursorFactory factory, final int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL(SQLDBHelper.NEWS_TABLE_QUERY);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
			final int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + SQLDBHelper.NEWS_TABLE);
	}

}
