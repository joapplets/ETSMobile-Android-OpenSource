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
    private static final int DB_VERSION = 6;
    private static final String DB_CREATE_FEED_TABLE = "CREATE TABLE feed( _id INTEGER PRIMARY KEY AUTOINCREMENT, feed_id INTEGER, name TEXT, url TEXT, type TEXT, image TEXT, lang TEXT)";
    private static final String DB_CREATE_NEWS_TABLE = "CREATE TABLE news( _id INTEGER PRIMARY KEY AUTOINCREMENT, news_id INTEGER, feed_id INTEGER, name TEXT, url TEXT, description TEXT, image TEXT, creator TEXT, pubDate TEXT )";
    private static final String DB_CREATE_COURS_TABLE = "CREATE TABLE cours( _id INTEGER PRIMARY KEY AUTOINCREMENT, cours_id INTEGER, name TEXT, shortname TEXT, description TEXT, professor TEXT, coursplan TEXT, website TEXT, credits INTEGER, prerequisites TEXT, level TEXT, workoad INTEGER)";
    private static final String DB_CREATE_PROGRAMMES_TABLE = "CREATE TABLE programmes( _id INTEGER PRIMARY KEY AUTOINCREMENT, programme_id INTEGER, name TEXT, shortname TEXT, description TEXT, url TEXT, url_pdf TEXT)";
    private static final String DB_CREATE_DIRECTORY_ENTRY_TABLE = "CREATE TABLE directory( _id INTEGER PRIMARY KEY AUTOINCREMENT, directory_id INTEGER, fax TEXT, phone TEXT, room TEXT )";
    private static final String DB_CREATE_PROFILE_TABLE = "CREATE TABLE profile( _id INTEGER PRIMARY KEY AUTOINCREMENT, profile_id INTEGER, title TEXT, url TEXT, description TEXT, image TEXT, creator TEXT, pubDate TEXT )";

    public AppEtsOpenHelper(Context context, CursorFactory factory) {
	super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	db.execSQL(DB_CREATE_FEED_TABLE);
	db.execSQL(DB_CREATE_NEWS_TABLE);
	db.execSQL(DB_CREATE_PROGRAMMES_TABLE);
	db.execSQL(DB_CREATE_COURS_TABLE);
	db.execSQL(DB_CREATE_DIRECTORY_ENTRY_TABLE);
	db.execSQL(DB_CREATE_PROFILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL("DROP TABLE feed");
	db.execSQL("DROP TABLE news");
	db.execSQL("DROP TABLE programmes");
	db.execSQL("DROP TABLE cours");
	db.execSQL("DROP TABLE directory");
	db.execSQL("DROP TABLE profile");

	onCreate(db);
    }

}
