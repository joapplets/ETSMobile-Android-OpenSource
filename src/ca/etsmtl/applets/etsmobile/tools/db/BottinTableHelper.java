package ca.etsmtl.applets.etsmobile.tools.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BottinTableHelper extends BaseTableHelper {

	public static final String BOTTIN_DATE_MODIF = "date_modif";
	public static final String BOTTIN_EMAIL = "email";
	public static final String BOTTIN_EMPLACEMENT = "emplacement";
	public static final String BOTTIN_ETS_ID = "ets_id";
	public static final String BOTTIN_ID = "_id";
	public static final String BOTTIN_NOM = "nom";
	public static final String BOTTIN_PRENOM = "prenom";
	public static final String BOTTIN_SERVICE = "service";
	public static final String BOTTIN_TELBUREAU = "telBureau";
	public static final String BOTTIN_TIRE = "titre";

	public static final String TABLE_NAME = "bottin";

	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ "( " + BOTTIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,  "
			+ BOTTIN_NOM + " TEXT NOT NULL, " + BOTTIN_PRENOM
			+ " TEXT NOT NULL, " + BOTTIN_TELBUREAU + " TEXT NOT NULL, "
			+ BOTTIN_EMPLACEMENT + " TEXT NOT NULL, " + BOTTIN_EMAIL
			+ " TEXT NOT NULL, " + BOTTIN_SERVICE + " TEXT NOT NULL, "
			+ BOTTIN_TIRE + " TEXT NOT NULL, " + BOTTIN_DATE_MODIF
			+ " TEXT NOT NULL," + BOTTIN_ETS_ID + " TEXT NOT NULL" + ");";

	private static final String DROP_TABLE = "DROP TABLE " + TABLE_NAME;
	public static final String[] AVAILABLE = new String[] { BOTTIN_ID,
			BOTTIN_NOM, BOTTIN_PRENOM, BOTTIN_EMAIL, BOTTIN_EMPLACEMENT,
			BOTTIN_TELBUREAU, BOTTIN_SERVICE, BOTTIN_DATE_MODIF, BOTTIN_TIRE,
			BOTTIN_ETS_ID };

	public BottinTableHelper(Context content) {
		super(content);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}
}
