package ca.etsmtl.applets.etsmobile.tools.db;

import android.database.sqlite.SQLiteDatabase;

public class NewsTable {

	//table name
	public static final String NEWS_TABLE = "newsTable";
	
	//fields
	public static final String NEWS_ID = "_id";
	public static final String NEWS_TITLE = "title";
	public static final String NEWS_DATE = "date";
	public static final String NEWS_DESCRIPTION = "description";
	public static final String NEWS_GUID = "guid";
	public static final String NEWS_SOURCE = "source";
	public static final String NEWS_LINK = "link";
	
	// Database creation SQL statement
	private static final String DATABASE_CREATE = 	
		"CREATE TABLE " + 
		NEWS_TABLE + 
		" ( " + NEWS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
		NEWS_TITLE + " TEXT NOT NULL, " +
		NEWS_DATE + " LONG NOT NULL, " +
		NEWS_DESCRIPTION + " TEXT NOT NULL, " +
		NEWS_GUID + " TEXT NOT NULL, " +
		NEWS_LINK + " TEXT, " + 
		NEWS_SOURCE + " TEXT NOT NULL);";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + NEWS_TABLE);
		onCreate(database);
	}
}
