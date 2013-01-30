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

    public static final String CREATE_TABLE = "CREATE TABLE " + BottinTableHelper.TABLE_NAME + "( "
	    + BottinTableHelper.BOTTIN__ID + " integer primary key autoincrement ,"
	    + BottinTableHelper.BOTTIN_NOM + " TEXT DEFAULT NULL, "
	    + BottinTableHelper.BOTTIN_PRENOM + " TEXT DEFAULT NULL, "
	    + BottinTableHelper.BOTTIN_TELBUREAU + " TEXT DEFAULT NULL, "
	    + BottinTableHelper.BOTTIN_EMPLACEMENT + " TEXT DEFAULT NULL, "
	    + BottinTableHelper.BOTTIN_COURRIEL + " TEXT DEFAULT NULL, "
	    + BottinTableHelper.BOTTIN_SERVICE + " TEXT DEFAULT NULL, "
	    + BottinTableHelper.BOTTIN_TIRE + " TEXT DEFAULT NULL, "
	    + BottinTableHelper.BOTTIN_DATE_MODIF + " TEXT DEFAULT NULL,"
	    + BottinTableHelper.BOTTIN_ETS_ID + " TEXT DEFAULT NULL" + ", UNIQUE (ets_id) ON CONFLICT REPLACE);";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + BottinTableHelper.TABLE_NAME;
    public static final String[] AVAILABLE = new String[] { BottinTableHelper.BOTTIN__ID,
	    BottinTableHelper.BOTTIN_NOM, BottinTableHelper.BOTTIN_PRENOM,
	    BottinTableHelper.BOTTIN_COURRIEL, BottinTableHelper.BOTTIN_EMPLACEMENT,
	    BottinTableHelper.BOTTIN_TELBUREAU, BottinTableHelper.BOTTIN_SERVICE,
	    BottinTableHelper.BOTTIN_DATE_MODIF, BottinTableHelper.BOTTIN_TIRE,
	    BottinTableHelper.BOTTIN_ETS_ID };
    public static final String INSERT_STATEMENT = "";

}
