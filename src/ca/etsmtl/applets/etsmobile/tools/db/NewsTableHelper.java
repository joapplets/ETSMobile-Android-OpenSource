package ca.etsmtl.applets.etsmobile.tools.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsTableHelper extends SQLiteOpenHelper{

	public static final String DB_NAME = "app_ets_db.db";
	public static final int VERSION = 1;
	
	public NewsTableHelper(Context content){
		super(content, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		NewsTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		NewsTable.onUpgrade(db, oldVersion, newVersion);
	}

}
