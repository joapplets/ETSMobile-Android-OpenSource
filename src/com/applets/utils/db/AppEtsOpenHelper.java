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
	// private static final String DB_CREATE_COURS_TABLE =
	// "CREATE TABLE cours( _id INTEGER PRIMARY KEY AUTOINCREMENT, cours_id INTEGER, name TEXT, shortname TEXT, description TEXT, professor TEXT, coursplan TEXT, website TEXT, credits INTEGER, prerequisites TEXT, level TEXT, workoad INTEGER)";
	// private static final String DB_CREATE_DIRECTORY_ENTRY_TABLE =
	// "CREATE TABLE directory( _id INTEGER PRIMARY KEY AUTOINCREMENT, directory_id INTEGER, fax TEXT, phone TEXT, room TEXT )";
	private static final String DB_CREATE_NEWS_TABLE = "CREATE TABLE news( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, url TEXT, image TEXT, creator TEXT, pubdate TEXT )";
	// private static final String DB_CREATE_PROGRAMMES_TABLE =
	// "CREATE TABLE programmes( _id INTEGER PRIMARY KEY AUTOINCREMENT, programme_id INTEGER, name TEXT, shortname TEXT, description TEXT, url TEXT, url_pdf TEXT)";
	private static final String DB_NAME = "ETSMobile";
	private static final int DB_VERSION = 34;

	public AppEtsOpenHelper(final Context context, final CursorFactory factory) {
		super(context, AppEtsOpenHelper.DB_NAME, factory,
				AppEtsOpenHelper.DB_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		// db.execSQL(AppEtsOpenHelper.DB_CREATE_FEED_TABLE);
		db.execSQL(AppEtsOpenHelper.DB_CREATE_NEWS_TABLE);
		// db.execSQL(AppEtsOpenHelper.DB_CREATE_PROGRAMMES_TABLE);
		// db.execSQL(AppEtsOpenHelper.DB_CREATE_COURS_TABLE);
		// db.execSQL(AppEtsOpenHelper.DB_CREATE_DIRECTORY_ENTRY_TABLE);
		// db.execSQL(AppEtsOpenHelper.DB_CREATE_PROFILE_TABLE);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
			final int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS channel");
		db.execSQL("DROP TABLE IF EXISTS news");
		db.execSQL("DROP TABLE IF EXISTS programmes");
		db.execSQL("DROP TABLE IF EXISTS cours");
		db.execSQL("DROP TABLE IF EXISTS directory");
		db.execSQL("DROP TABLE IF EXISTS profile");

		onCreate(db);
	}

}
