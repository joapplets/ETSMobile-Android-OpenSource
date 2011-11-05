package com.applets.utils.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.applets.models.Model;

/**
 * Adapter for SQLite database
 * 
 * @author Philippe David
 */
public class FeedDbAdapter extends BaseDbAdapter {
    // TABLE COLUMNS INFO
    public static final String TABLE_NAME = "feed";
    public static final String KEY_ROWID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_URL = "url";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_LANG = "lang";

    public FeedDbAdapter(Context context) {
	super(context);
    }

    /**
     * Create a new feed If the feed is successfully created return the new
     * rowId for that note, otherwise return a -1 to indicate failure.
     */
    @Override
    public long create(Model feed) {
	return db.insert(TABLE_NAME, null, feed.getValues());
    }

    @Override
    public int update(Model model) {
	return db.update(TABLE_NAME, model.getValues(),
		KEY_ROWID + "=" + model.getID(), null);
    }

    /**
     * Return a Cursor positioned at the defined feed
     */
    @Override
    public Cursor get(long rowId) throws SQLException {
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
    @Override
    public Cursor getAll() {
	return db.query(TABLE_NAME, new String[] { KEY_ROWID, KEY_NAME,
		KEY_URL, KEY_IMAGE }, null, null, null, null, null);
    }

    /**
     * Close current db and dbHelper
     */
    @Override
    public void close() {
	db.close();
	dbHelper.close();
    }

    @Override
    public int delete(Model model) {
	return db.delete(TABLE_NAME, KEY_ROWID + "=" + model.getID(), null);
    }
}
