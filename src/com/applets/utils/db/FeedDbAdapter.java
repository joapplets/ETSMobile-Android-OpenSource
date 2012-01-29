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
    public static final String KEY_IMAGE = "image";
    public static final String KEY_LANG = "lang";
    public static final String KEY_NAME = "name";
    public static final String KEY_ROWID = "id";
    public static final String KEY_URL = "url";
    // TABLE COLUMNS INFO
    public static final String TABLE_NAME = "feed";

    public FeedDbAdapter(final Context context) {
	super(context);
    }

    /**
     * Close current db and dbHelper
     */

    @Override
    public void close() {
	db.close();
	dbHelper.close();
    }

    /**
     * Create a new feed If the feed is successfully created return the new
     * rowId for that note, otherwise return a -1 to indicate failure.
     */

    @Override
    public long create(final Model feed) {
	return db.insert(FeedDbAdapter.TABLE_NAME, null, feed.getValues());
    }

    @Override
    public int delete(final Model model) {
	return db.delete(FeedDbAdapter.TABLE_NAME, FeedDbAdapter.KEY_ROWID
		+ "=" + model.getID(), null);
    }

    /**
     * Return a Cursor positioned at the defined feed
     */

    @Override
    public Cursor get(final long rowId) throws SQLException {
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

    /**
     * Return a Cursor over the list of all feed in the database
     * 
     * @return Cursor over all feeds
     */

    @Override
    public Cursor getAll() {
	return db.query(FeedDbAdapter.TABLE_NAME, new String[] {
		FeedDbAdapter.KEY_ROWID, FeedDbAdapter.KEY_NAME,
		FeedDbAdapter.KEY_URL, FeedDbAdapter.KEY_IMAGE }, null, null,
		null, null, null);
    }

    @Override
    public int update(final Model model) {
	return db.update(FeedDbAdapter.TABLE_NAME, model.getValues(),
		FeedDbAdapter.KEY_ROWID + "=" + model.getID(), null);
    }
}
