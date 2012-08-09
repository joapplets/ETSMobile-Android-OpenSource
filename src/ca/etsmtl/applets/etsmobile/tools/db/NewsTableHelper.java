package ca.etsmtl.applets.etsmobile.tools.db;

public class NewsTableHelper {

	// table name
	public static final String TABLE_NAME = "newsTable";

	// fields
	public static final String NEWS_ID = "_id";
	public static final String NEWS_TITLE = "title";
	public static final String NEWS_DATE = "date";
	public static final String NEWS_DESCRIPTION = "description";
	public static final String NEWS_GUID = "guid";
	public static final String NEWS_SOURCE = "source";
	public static final String NEWS_LINK = "link";

	// Database creation SQL statement
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ " ( " + NEWS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ NEWS_TITLE + " TEXT NOT NULL, " + NEWS_DATE + " LONG NOT NULL, "
			+ NEWS_DESCRIPTION + " TEXT NOT NULL, " + NEWS_GUID
			+ " TEXT NOT NULL, " + NEWS_LINK + " TEXT , " + NEWS_SOURCE
			+ " TEXT NOT NULL);";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	public static final String[] AVAILABLE = new String[] { NEWS_ID,
			NEWS_TITLE, NEWS_DATE, NEWS_DESCRIPTION, NEWS_GUID, NEWS_LINK,
			NEWS_SOURCE };

}
