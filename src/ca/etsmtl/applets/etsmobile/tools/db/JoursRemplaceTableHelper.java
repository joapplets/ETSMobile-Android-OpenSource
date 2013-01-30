package ca.etsmtl.applets.etsmobile.tools.db;

public class JoursRemplaceTableHelper {
    // table name
    public static final String TABLE_NAME = "jourRemplaceTable";

    // fields
    public static final String ID = "_id";
    public static final String DATE_ORIGIN = "dateOrigin";
    public static final String DATE_REMPLACEMENT = "dateRemplacement";
    public static final String DESC = "desc";
    public static final String SESSION_ID = "session_id";

    // Database creation SQL statement
    public static final String CREATE_TABLE = "CREATE TABLE " + JoursRemplaceTableHelper.TABLE_NAME
	    + " ( " + JoursRemplaceTableHelper.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	    + JoursRemplaceTableHelper.DATE_ORIGIN + " TEXT, "
	    + JoursRemplaceTableHelper.DATE_REMPLACEMENT + " TEXT, "
	    + JoursRemplaceTableHelper.SESSION_ID + " INT, " + JoursRemplaceTableHelper.DESC
	    + " TEXT, UNIQUE (dateOrigin) ON CONFLICT REPLACE);";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
	    + JoursRemplaceTableHelper.TABLE_NAME;

    public static final String[] AVAILABLE = new String[] { JoursRemplaceTableHelper.ID,
	    JoursRemplaceTableHelper.DESC, JoursRemplaceTableHelper.DATE_REMPLACEMENT,
	    JoursRemplaceTableHelper.DATE_ORIGIN };

}
