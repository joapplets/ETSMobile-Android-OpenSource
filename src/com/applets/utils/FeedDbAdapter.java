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
    // TABLE COLUMNS INFO
    public static final String TABLE_NAME = "feed";
    public static final String KEY_ROWID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_URL = "url";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_LANG = "lang";

    private SQLiteDatabase db;
    private AppEtsOpenHelper dbHelper;
    private Context context;

    public FeedDbAdapter(Context context) {
	this.context = context;
    }

    public FeedDbAdapter open() throws SQLException {
	dbHelper = new AppEtsOpenHelper(context, null);
	db = dbHelper.getWritableDatabase();
	return this;
    }

    /**
     * Create a new feed If the feed is successfully created return the new
     * rowId for that note, otherwise return a -1 to indicate failure.
     */
    public long createFeed(String name, String url, String image) {
	ContentValues values = createContentValues(name, url, image);
	return db.insert(TABLE_NAME, null, values);
    }

    /**
     * Create a new feed If the feed is successfully created return the new
     * rowId for that note, otherwise return a -1 to indicate failure.
     */
    public long createFeed(Feed feed) {
	return db.insert(TABLE_NAME, null, feed.getValues());
    }

    /**
     * Updates a feed. If the feed is successfully updated return true otherwise
     * return false to indicate failure.
     */
    public boolean updateFeed(long rowID, String name, String url, String image) {
	ContentValues values = createContentValues(name, url, image);
	return db.update(TABLE_NAME, values, KEY_ROWID + "=" + rowID, null) > 0;
    }

    /**
     * Delete a feed
     */
    public boolean deleteTodo(long rowId) {
	return db.delete(TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor positioned at the defined feed
     */
    public Cursor getFeed(long rowId) throws SQLException {
	Cursor mCursor = db.query(true, TABLE_NAME, new String[] { KEY_ROWID,
		KEY_NAME, KEY_URL, KEY_IMAGE }, KEY_ROWID + "=" + rowId, null,
		null, null, null, null);
	if (mCursor != null) {
	    mCursor.moveToFirst();
	}
	return mCursor;
    }

    /**
     * Return a Cursor over the list of all feed in the database
     * 
     * @return Cursor over all feeds
     */
    public Cursor getAllFeeds() {
	return db.query(TABLE_NAME, new String[] { KEY_ROWID, KEY_NAME,
		KEY_URL, KEY_IMAGE }, null, null, null, null, null);
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
    private ContentValues createContentValues(String name, String url,
	    String image) {
	ContentValues values = new ContentValues();
	values.put(KEY_NAME, name);
	values.put(KEY_URL, url);
	values.put(KEY_IMAGE, image);
	return values;
    }

    /**
     * Close current db and dbHelper
     */
    public void close() {
	db.close();
	dbHelper.close();
    }
}
