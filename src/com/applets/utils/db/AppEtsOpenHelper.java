package com.applets.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Utility Class to ease the access to the SQLite Database
 * 
 * @author Philippe David
 */
public class AppEtsOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "appETS";
    private static final int DB_VERSION = 2;
    private static final String DB_CREATE_FEED_TABLE = "CREATE TABLE feed( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, url TEXT, type TEXT, image TEXT, lang TEXT)";
    private static final String DB_CREATE_NEWS_TABLE = "CREATE TABLE news( id INTEGER PRIMARY KEY AUTOINCREMENT, feed_id INTEGER, title TEXT, url TEXT, description TEXT, image TEXT)";

    public AppEtsOpenHelper(Context context, CursorFactory factory) {
	super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	db.execSQL(DB_CREATE_FEED_TABLE);
	db.execSQL(DB_CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS feed");
	db.execSQL("DROP TABLE IF EXISTS news");
	onCreate(db);
    }

}
