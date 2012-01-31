package com.applets.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.applets.models.Feed;

/**
 * Adapter for SQLite database
 * 
 * @author Philippe David
 */
public class FeedDbAdapter {
	public static final String KEY_IMAGE = "image";
	public static final String KEY_LANG = "lang";
	public static final String KEY_NAME = "name";
	public static final String KEY_ROWID = "id";
	public static final String KEY_URL = "url";
	// TABLE COLUMNS INFO
	public static final String TABLE_NAME = "feed";

	private final Context context;
	private SQLiteDatabase db;
	private AppEtsOpenHelper dbHelper;

	public FeedDbAdapter(final Context context) {
		this.context = context;
	}

	/**
	 * Close current db and dbHelper
	 */
	public void close() {
		db.close();
		dbHelper.close();
	}

	/**
	 * Simple key->value object for SQLite
	 * 
	 * @param name
	 *            Name of the Feed
	 * @param url
	 *            Url to the news list
	 * @param image
	 *            Link to an image of the feed
	 * @return The new Contentvalue representing the Feed
	 */
	private ContentValues createContentValues(final String name,
			final String url, final String image) {
		final ContentValues values = new ContentValues();
		values.put(FeedDbAdapter.KEY_NAME, name);
		values.put(FeedDbAdapter.KEY_URL, url);
		values.put(FeedDbAdapter.KEY_IMAGE, image);
		return values;
	}

	/**
	 * Create a new feed If the feed is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */
	public long createFeed(final Feed feed) {
		return db.insert(FeedDbAdapter.TABLE_NAME, null, feed.getValues());
	}

	/**
	 * Create a new feed If the feed is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */
	public long createFeed(final String name, final String url,
			final String image) {
		final ContentValues values = createContentValues(name, url, image);
		return db.insert(FeedDbAdapter.TABLE_NAME, null, values);
	}

	/**
	 * Delete a feed
	 */
	public boolean deleteTodo(final long rowId) {
		return db.delete(FeedDbAdapter.TABLE_NAME, FeedDbAdapter.KEY_ROWID
				+ "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all feed in the database
	 * 
	 * @return Cursor over all feeds
	 */
	public Cursor getAllFeeds() {
		return db.query(FeedDbAdapter.TABLE_NAME, new String[] {
				FeedDbAdapter.KEY_ROWID, FeedDbAdapter.KEY_NAME,
				FeedDbAdapter.KEY_URL, FeedDbAdapter.KEY_IMAGE }, null, null,
				null, null, null);
	}

	/**
	 * Return a Cursor positioned at the defined feed
	 */
	public Cursor getFeed(final long rowId) throws SQLException {
		final Cursor mCursor = db.query(true, FeedDbAdapter.TABLE_NAME,
				new String[] { FeedDbAdapter.KEY_ROWID, FeedDbAdapter.KEY_NAME,
						FeedDbAdapter.KEY_URL, FeedDbAdapter.KEY_IMAGE },
				FeedDbAdapter.KEY_ROWID + "=" + rowId, null, null, null, null,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public FeedDbAdapter open() throws SQLException {
		dbHelper = new AppEtsOpenHelper(context, null);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Updates a feed. If the feed is successfully updated return true otherwise
	 * return false to indicate failure.
	 */
	public boolean updateFeed(final long rowID, final String name,
			final String url, final String image) {
		final ContentValues values = createContentValues(name, url, image);
		return db.update(FeedDbAdapter.TABLE_NAME, values,
				FeedDbAdapter.KEY_ROWID + "=" + rowID, null) > 0;
	}
}
