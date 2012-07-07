package ca.etsmtl.applets.etsmobile.tools.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDBHelper extends SQLiteOpenHelper {

	/**
	 * Columns name for the tables.
	 */
	public static final String BOTTIN_DATE_MODIF = "date_modif";
	public static final String BOTTIN_EMAIL = "email";
	public static final String BOTTIN_EMPLACEMENT = "emplacement";
	public static final String BOTTIN_ETS_ID = "ets_id";
	public static final String BOTTIN_ID = "_id";
	public static final String BOTTIN_NOM = "nom";
	public static final String BOTTIN_PRENOM = "prenom";
	public static final String BOTTIN_SERVICE = "service";
	/**
	 * Tables
	 */
	public final static String BOTTIN_TABLE = "bottinTable";
	public static final String BOTTIN_TABLE_QUERY = "CREATE TABLE "
			+ SQLDBHelper.BOTTIN_TABLE + "( " + SQLDBHelper.BOTTIN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,  " + SQLDBHelper.BOTTIN_NOM
			+ " TEXT NOT NULL, " + SQLDBHelper.BOTTIN_PRENOM
			+ " TEXT NOT NULL, " + SQLDBHelper.BOTTIN_TELBUREAU
			+ " TEXT NOT NULL, " + SQLDBHelper.BOTTIN_EMPLACEMENT
			+ " TEXT NOT NULL, " + SQLDBHelper.BOTTIN_EMAIL
			+ " TEXT NOT NULL, " + SQLDBHelper.BOTTIN_SERVICE
			+ " TEXT NOT NULL, " + SQLDBHelper.BOTTIN_TIRE + " TEXT NOT NULL, "
			+ SQLDBHelper.BOTTIN_DATE_MODIF + " TEXT NOT NULL,"
			+ SQLDBHelper.BOTTIN_ETS_ID + " TEXT NOT NULL" + ");";

	public static final String BOTTIN_TELBUREAU = "telBureau";
	public static final String BOTTIN_TIRE = "titre";
	public static final String DB_NAME = "app_ets_db.db";
	public final static String NEWS_DATE = "date";
	public final static String NEWS_DESCRIPTION = "description";
	public final static String NEWS_GUID = "guid";

	public final static String NEWS_ID = "id";
	public final static String NEWS_SOURCE = "source";
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
	public static final String PROGRAMMES_DESCRIPTION = "description";
	public final static String PROGRAMMES_ID = "id";

	public static final String PROGRAMMES_NAME = "name";
	public static final String PROGRAMMES_PROGRAMME_ID = "programme_id";
	public static final String PROGRAMMES_SHORT_NAME = "shortname";

	public final static String PROGRAMMES_TABLE = "programmes";

	private final static String PROGRAMMES_TABLE_QUERY =

	"CREATE TABLE " + SQLDBHelper.PROGRAMMES_TABLE + " ( "
			+ SQLDBHelper.PROGRAMMES_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ SQLDBHelper.PROGRAMMES_DESCRIPTION + " TEXT NOT NULL, "
			+ SQLDBHelper.PROGRAMMES_PROGRAMME_ID + " LONG NOT NULL, "
			+ SQLDBHelper.PROGRAMMES_SHORT_NAME + " TEXT NOT NULL, "
			+ SQLDBHelper.PROGRAMMES_URL + " TEXT NOT NULL, "
			+ SQLDBHelper.PROGRAMMES_URL_PDF + " TEXT NOT NULL);";

	public static final String PROGRAMMES_URL = "url";

	public static final String PROGRAMMES_URL_PDF = "url_pdf";

	public SQLDBHelper(final Context context, final String name,
			final CursorFactory factory, final int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL(SQLDBHelper.NEWS_TABLE_QUERY);
		db.execSQL(SQLDBHelper.PROGRAMMES_TABLE_QUERY);
		db.execSQL(SQLDBHelper.BOTTIN_TABLE_QUERY);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
			final int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + SQLDBHelper.NEWS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SQLDBHelper.PROGRAMMES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SQLDBHelper.BOTTIN_TABLE);

		onCreate(db);
	}

}
