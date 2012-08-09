package ca.etsmtl.applets.etsmobile.tools.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ETSMobileOpenHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 3;
	public static final String DB_NAME = "app_ets_db";

	public ETSMobileOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(BottinTableHelper.CREATE_TABLE);
		db.execSQL(NewsTableHelper.CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(NewsTableHelper.DROP_TABLE);
		db.execSQL(BottinTableHelper.DROP_TABLE);
		onCreate(db);

	}

}
