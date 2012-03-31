package com.applets.tools.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.applets.models.News;

public class NewsDB {

	// db helper
	private static SQLDBHelper db = null;
	private static final String DB_NAME = SQLDBHelper.DB_NAME;
	// db info.
	private static final int DB_VERSION = 1;
	// the unique instance of the db
	private static NewsDB instance = null;

	// Here I applied the singleton pattern because I only want
	// one instance of the db (which contains all the data of the app)
	public static NewsDB getInstance(final Context c) {

		// if instance is null, which means the we are opening the app
		if (NewsDB.instance == null) {

			// create one instance
			NewsDB.instance = new NewsDB();

			// create the db, if it's already in the filesystem it will not
			// recreate it
			NewsDB.db = new SQLDBHelper(c, NewsDB.DB_NAME, null,
					NewsDB.DB_VERSION);
		}

		// return the instance.
		NewsDB.db.close();
		return NewsDB.instance;
	}

	private final String NEWS_DATE = SQLDBHelper.NEWS_DATE;

	private final String NEWS_DESCRIPTION = SQLDBHelper.NEWS_DESCRIPTION;
	private final String NEWS_GUID = SQLDBHelper.NEWS_GUID;

	private final String NEWS_SOURCE = SQLDBHelper.NEWS_SOURCE;

	/**
	 * Everything related to a news.
	 */
	private final String NEWS_TABLE = SQLDBHelper.NEWS_TABLE;

	private final String NEWS_TITLE = SQLDBHelper.NEWS_TITLE;

	// private constructor so we can only have one intance of the db
	private NewsDB() {
	}

	public ArrayList<News> getAllNews() {

		// the vector that will contain the selected notes
		final ArrayList<News> v = new ArrayList<News>();

		// set the database to be readable
		final SQLiteDatabase readDB = NewsDB.db.getReadableDatabase();

		// Equivalent to : SELECT * FROM NOTES ORDER BY NEWS_DATE DESC
		final Cursor c = readDB.query(NEWS_TABLE, new String[] { "*" }, null,
				null, null, null, NEWS_DATE + " DESC");

		News news;
		while (c.moveToNext()) {

			news = new News();

			news.setTitle(c.getString(c.getColumnIndex(NEWS_TITLE)));
			news.setPubDate(c.getLong(c.getColumnIndex(NEWS_DATE)));
			news.setDescription(c.getString(c.getColumnIndex(NEWS_DESCRIPTION)));
			news.setGuid(c.getString(c.getColumnIndex(NEWS_GUID)));
			news.setSource(c.getString(c.getColumnIndex(NEWS_SOURCE)));

			v.add(news);
		}

		c.close();
		readDB.close();

		return v;
	}

	public News getNewsByGUID(final String guid) {

		News n = null;

		// set the database to be readable
		final SQLiteDatabase readDB = NewsDB.db.getReadableDatabase();

		final Cursor c = readDB.query(NEWS_TABLE, new String[] { "*" },
				NEWS_GUID + " LIKE \"" + guid + "\"", null, null, null, null);
		if (c.moveToNext()) {
			n = new News();
			n.setTitle(c.getString(c.getColumnIndex(NEWS_TITLE)));
			n.setPubDate(c.getLong(c.getColumnIndex(NEWS_DATE)));
			n.setDescription(c.getString(c.getColumnIndex(NEWS_DESCRIPTION)));
			n.setGuid(c.getString(c.getColumnIndex(NEWS_GUID)));
		}

		c.close();
		readDB.close();

		return n;
	}

	public ArrayList<News> getNewsBySource(final String source) {

		final ArrayList<News> list = new ArrayList<News>();

		// set the database to be readable
		final SQLiteDatabase readDB = NewsDB.db.getReadableDatabase();

		final Cursor c = readDB.query(NEWS_TABLE, new String[] { "*" },
				NEWS_SOURCE + " LIKE \"" + source + "\"", null, null, null,
				null);
		while (c.moveToNext()) {
			final News n = new News();
			n.setTitle(c.getString(c.getColumnIndex(NEWS_TITLE)));
			n.setPubDate(c.getLong(c.getColumnIndex(NEWS_DATE)));
			n.setDescription(c.getString(c.getColumnIndex(NEWS_DESCRIPTION)));
			n.setGuid(c.getString(c.getColumnIndex(NEWS_GUID)));
			n.setSource(c.getString(c.getColumnIndex(NEWS_SOURCE)));

			list.add(n);
		}

		c.close();
		readDB.close();

		return list;
	}

	public void insertNews(final String title, final long l,
			final String description, final String guid, final String source) {
		final ContentValues cv = new ContentValues();

		// put into the date column the String date
		cv.put(NEWS_TITLE, title);

		// same here...
		cv.put(NEWS_DATE, l);

		// same...
		cv.put(NEWS_DESCRIPTION, description);

		// same...
		cv.put(NEWS_GUID, guid);

		// same...
		cv.put(NEWS_SOURCE, source);

		// set the db into writable mode, then insert the values into the table
		NewsDB.db.getWritableDatabase().insert(NEWS_TABLE, null, cv);

		// close the db
		NewsDB.db.close();
	}
}
