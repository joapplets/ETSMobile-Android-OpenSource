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
	public static final String CREATE_TABLE = "CREATE TABLE "
			+ NewsTableHelper.TABLE_NAME + " ( " + NewsTableHelper.NEWS_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ NewsTableHelper.NEWS_TITLE + " TEXT NOT NULL, "
			+ NewsTableHelper.NEWS_DATE + " LONG NOT NULL, "
			+ NewsTableHelper.NEWS_DESCRIPTION + " TEXT NOT NULL, "
			+ NewsTableHelper.NEWS_GUID + " TEXT NOT NULL, "
			+ NewsTableHelper.NEWS_LINK + " TEXT , "
			+ NewsTableHelper.NEWS_SOURCE + " TEXT NOT NULL);";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ NewsTableHelper.TABLE_NAME;

	public static final String[] AVAILABLE = new String[] {
			NewsTableHelper.NEWS_ID, NewsTableHelper.NEWS_TITLE,
			NewsTableHelper.NEWS_DATE, NewsTableHelper.NEWS_DESCRIPTION,
			NewsTableHelper.NEWS_GUID, NewsTableHelper.NEWS_LINK,
			NewsTableHelper.NEWS_SOURCE };

}
