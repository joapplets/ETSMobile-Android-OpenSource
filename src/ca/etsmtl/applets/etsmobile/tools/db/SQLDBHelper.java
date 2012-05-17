package ca.etsmtl.applets.etsmobile.tools.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDBHelper extends SQLiteOpenHelper{

	public static final String DB_NAME = "ETSMobile_db.db";
	
	/**
	 * News table.
	 */
	public final static String NEWS_TABLE = "NewsTable";
	
	/**
	 * Columns name for the news table.
	 */
	public final static String NEWS_ID = "id";
	public final static String NEWS_TITLE = "title";
	public final static String NEWS_DATE = "date";
	public final static String NEWS_DESCRIPTION = "description";
	public final static String NEWS_GUID = "guid";
	public final static String NEWS_SOURCE = "source";
	
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
	
	public SQLDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(NEWS_TABLE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + NEWS_TABLE);
	}

}
