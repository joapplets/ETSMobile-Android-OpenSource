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
public class NewsDbAdapter extends BaseDbAdapter {
    // TABLE COLUMNS INFO
    public static final String TABLE_TITLE = "news";
    public static final String KEY_NEWS_ID = "news_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_FEED_ID = "feed_id";
    public static final String KEY_PUB_DATE = "pubDate";
    public static final String KEY_CREATOR = "creator";
    public static final String KEY_URL = "url";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_DESCRIPTION = "description";

    public NewsDbAdapter(Context context) {
	this.context = context;
    }

    /**
     * Create a new news If the news is successfully created return the new
     * rowId for that note, otherwise return a -1 to indicate failure.
     */
    @Override
    public long create(Model news) {
	return db.insert(TABLE_TITLE, null, news.getValues());
    }

    /**
     * Updates a news. If the news is successfully updated return true otherwise
     * return false to indicate failure.
     */
    @Override
    public int update(Model news) {
	return db.update(TABLE_TITLE, news.getValues(), KEY_NEWS_ID + "="
		+ news.getID(), null);
    }

    /**
     * Delete a news
     */
    @Override
    public int delete(Model news) {
	return db.delete(TABLE_TITLE, KEY_NEWS_ID + "=" + news.getID(), null);
    }

    /**
     * Return a Cursor positioned at the defined news
     */
    @Override
    public Cursor get(long rowId) throws SQLException {
	Cursor mCursor = db.query(true, TABLE_TITLE, new String[] {
		KEY_NEWS_ID, KEY_NAME, KEY_URL, KEY_IMAGE }, KEY_NEWS_ID + "="
		+ rowId, null, null, null, null, null);
	if (mCursor != null) {
	    mCursor.moveToFirst();
	}
	return mCursor;
    }

    /**
     * Return a Cursor over the list of all news in the database
     * 
     * @return Cursor over all news
     */
    @Override
    public Cursor getAll() {
	return db.query(TABLE_TITLE, new String[] { KEY_NEWS_ID, KEY_NAME,
		KEY_URL, KEY_IMAGE, KEY_FEED_ID, KEY_PUB_DATE, KEY_CREATOR },
		null, null, null, null, null);
    }
}