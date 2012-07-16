package ca.etsmtl.applets.etsmobile.tools.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseTableHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "app_ets_db.db";
	public static final int VERSION = 1;

	public BaseTableHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

}
