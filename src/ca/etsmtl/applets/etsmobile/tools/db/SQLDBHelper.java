package ca.etsmtl.applets.etsmobile.tools.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDBHelper extends SQLiteOpenHelper{

	public static final String DB_NAME = "app_ets_db.db";
	
	/**
	 * Tables
	 */
	public final static String NEWS_TABLE = "newsTable";
	public final static String PROGRAMMES_TABLE = "programmes";
	
	/**
	 * Columns name for the tables.
	 */
	public final static String NEWS_ID = "id";
	public final static String NEWS_TITLE = "title";
	public final static String NEWS_DATE = "date";
	public final static String NEWS_DESCRIPTION = "description";
	public final static String NEWS_GUID = "guid";
	public final static String NEWS_SOURCE = "source";
	
	public final static String PROGRAMMES_ID = "id";
	public static final String PROGRAMMES_DESCRIPTION = "description";
	public static final String PROGRAMMES_NAME = "name";
	public static final String PROGRAMMES_PROGRAMME_ID = "programme_id";
	public static final String PROGRAMMES_SHORT_NAME = "shortname";
	public static final String PROGRAMMES_URL = "url";
	public static final String PROGRAMMES_URL_PDF = "url_pdf";
	
	/**
	 * SQL query to create the news table.
	 */
	private final static String NEWS_TABLE_QUERY = 

	"CREATE TABLE " + 
	NEWS_TABLE + 
	" ( " + NEWS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
	NEWS_TITLE + " TEXT NOT NULL, " +
	NEWS_DATE + " LONG NOT NULL, " +
	NEWS_DESCRIPTION + " TEXT NOT NULL, " +
	NEWS_GUID + " TEXT NOT NULL, " +
	NEWS_SOURCE + " TEXT NOT NULL);";
	
	private final static String PROGRAMMES_TABLE_QUERY = 

		"CREATE TABLE " + 
		PROGRAMMES_TABLE + 
		" ( " + PROGRAMMES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
		PROGRAMMES_DESCRIPTION + " TEXT NOT NULL, " +
		PROGRAMMES_PROGRAMME_ID + " LONG NOT NULL, " +
		PROGRAMMES_SHORT_NAME + " TEXT NOT NULL, " +
		PROGRAMMES_URL + " TEXT NOT NULL, " +
		PROGRAMMES_URL_PDF + " TEXT NOT NULL);";
	
	public SQLDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(NEWS_TABLE_QUERY);
		db.execSQL(PROGRAMMES_TABLE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + NEWS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + PROGRAMMES_TABLE_QUERY);
	}

}
