package ca.etsmtl.applets.etsmobile.tools.db;

public class BottinTableHelper {

	public static final String BOTTIN_DATE_MODIF = "date_modif";
	public static final String BOTTIN_COURRIEL = "courriel";
	public static final String BOTTIN_EMPLACEMENT = "emplacement";
	public static final String BOTTIN_ETS_ID = "ets_id";
	public static final String BOTTIN__ID = "_id";
	public static final String BOTTIN_NOM = "nom";
	public static final String BOTTIN_PRENOM = "prenom";
	public static final String BOTTIN_SERVICE = "service";
	public static final String BOTTIN_TELBUREAU = "telBureau";
	public static final String BOTTIN_TIRE = "titre";

	public static final String TABLE_NAME = "bottin";

	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ "( " + BOTTIN__ID + " integer primary key autoincrement ,"
			+ BOTTIN_NOM + " TEXT DEFAULT NULL, " + BOTTIN_PRENOM
			+ " TEXT DEFAULT NULL, " + BOTTIN_TELBUREAU + " TEXT DEFAULT NULL, "
			+ BOTTIN_EMPLACEMENT + " TEXT DEFAULT NULL, " + BOTTIN_COURRIEL
			+ " TEXT DEFAULT NULL, " + BOTTIN_SERVICE + " TEXT DEFAULT NULL, "
			+ BOTTIN_TIRE + " TEXT DEFAULT NULL, " + BOTTIN_DATE_MODIF
			+ " TEXT DEFAULT NULL," + BOTTIN_ETS_ID + " TEXT DEFAULT NULL"
			+ ");";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;
	public static final String[] AVAILABLE = new String[] { BOTTIN__ID,
			BOTTIN_NOM, BOTTIN_PRENOM, BOTTIN_COURRIEL, BOTTIN_EMPLACEMENT,
			BOTTIN_TELBUREAU, BOTTIN_SERVICE, BOTTIN_DATE_MODIF, BOTTIN_TIRE,
			BOTTIN_ETS_ID };
	public static final String INSERT_STATEMENT = "";
		
}
