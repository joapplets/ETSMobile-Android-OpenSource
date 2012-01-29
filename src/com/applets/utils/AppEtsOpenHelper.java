package com.applets.utils;

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
    private static final String DB_CREATE_FEED_TABLE = "CREATE TABLE feed( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, url TEXT, type TEXT, image TEXT, lang TEXT)";
    private static final String DB_CREATE_NEWS_TABLE = "CREATE TABLE news( id INTEGER PRIMARY KEY AUTOINCREMENT, feed_id INTEGER, title TEXT, url TEXT, description TEXT, image TEXT)";
    private static final String DB_NAME = "appETS";
    private static final int DB_VERSION = 2;

    public AppEtsOpenHelper(final Context context, final CursorFactory factory) {
	super(context, AppEtsOpenHelper.DB_NAME, factory,
		AppEtsOpenHelper.DB_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
	db.execSQL(AppEtsOpenHelper.DB_CREATE_FEED_TABLE);
	db.execSQL(AppEtsOpenHelper.DB_CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
	    final int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS feed");
	db.execSQL("DROP TABLE IF EXISTS news");
	onCreate(db);
    }

}
