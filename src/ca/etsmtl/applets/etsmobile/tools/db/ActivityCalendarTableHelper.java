package ca.etsmtl.applets.etsmobile.tools.db;

public class ActivityCalendarTableHelper {

    private static final String TEXT = " TEXT DEFAULT 'n/a', ";

    // table name
    public static final String TABLE_NAME = "activitiesTable";

    // fields
    public static final String ACTIVITY_ID = "_id";
    public static final String ACTIVITY_START_DATE = "startDate";
    public static final String ACTIVITY_END_DATE = "endDate";
    public static final String ACTIVITY_NAME = "name";
    public static final String ACTIVITY_LOCATION = "location";
    public static final String ACTIVITY_JOUR = "jour";
    public static final String ACTIVITY_JOURNEE = "journee";
    public static final String ACTIVITY_CODE = "code";
    public static final String ACTIVITY_COURS = "cours";
    public static final String ACTIVITY_TITRE = "titre";
    public static final String ACTIVITY_SESSION_ID = "session_id";
    public static final String ACTIVITY_EVENT_DRAW_ID = "eventDrawId";
    public static final String ACTIVITY_GROUP = "groupe";

    // Database creation SQL statement
    public static final String CREATE_TABLE = "CREATE TABLE "
	    + ActivityCalendarTableHelper.TABLE_NAME + " ( "
	    + ActivityCalendarTableHelper.ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	    + ActivityCalendarTableHelper.ACTIVITY_START_DATE + TEXT
	    + ActivityCalendarTableHelper.ACTIVITY_END_DATE + TEXT
	    + ActivityCalendarTableHelper.ACTIVITY_NAME + TEXT
	    + ActivityCalendarTableHelper.ACTIVITY_LOCATION + TEXT
	    + ActivityCalendarTableHelper.ACTIVITY_JOURNEE + TEXT
	    + ActivityCalendarTableHelper.ACTIVITY_CODE + TEXT
	    + ActivityCalendarTableHelper.ACTIVITY_COURS + TEXT
	    + ActivityCalendarTableHelper.ACTIVITY_TITRE + TEXT
	    + ActivityCalendarTableHelper.ACTIVITY_SESSION_ID + TEXT
	    + ActivityCalendarTableHelper.ACTIVITY_GROUP + TEXT
	    + ActivityCalendarTableHelper.ACTIVITY_EVENT_DRAW_ID + " INTEGER, "
	    + ActivityCalendarTableHelper.ACTIVITY_JOUR
	    + " TEXT, UNIQUE (name) ON CONFLICT REPLACE);";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
	    + ActivityCalendarTableHelper.TABLE_NAME;

    public static final String[] AVAILABLE = new String[] {
	    ActivityCalendarTableHelper.ACTIVITY_ID,
	    ActivityCalendarTableHelper.ACTIVITY_START_DATE,
	    ActivityCalendarTableHelper.ACTIVITY_END_DATE,
	    ActivityCalendarTableHelper.ACTIVITY_NAME,
	    ActivityCalendarTableHelper.ACTIVITY_LOCATION,
	    ActivityCalendarTableHelper.ACTIVITY_JOURNEE,
	    ActivityCalendarTableHelper.ACTIVITY_JOUR, ACTIVITY_JOURNEE, ACTIVITY_CODE,
	    ACTIVITY_COURS, ACTIVITY_TITRE, ACTIVITY_GROUP, ACTIVITY_EVENT_DRAW_ID,
	    ACTIVITY_SESSION_ID };

}
