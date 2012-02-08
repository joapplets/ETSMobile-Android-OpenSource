package com.applets.utils.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.applets.models.Model;
import com.applets.models.News;

/**
 * Adapter for SQLite database
 * 
 * @author Philippe David
 */
public class NewsDbAdapter extends BaseDbAdapter {
    public static final String KEY_CREATOR = "creator";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_FEED_ID = "channel_id";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_NAME = "name";
    // public static final String KEY_NEWS_ID = "news_id";
    public static final String KEY_PUB_DATE = "pubdate";
    public static final String KEY_URL = "url";
    // TABLE COLUMNS INFO
    public static final String TABLE_TITLE = "news";
    final String[] columns = new String[] { BaseDbAdapter.KEY_ROW_ID,
	    NewsDbAdapter.KEY_NAME, NewsDbAdapter.KEY_DESCRIPTION,
	    NewsDbAdapter.KEY_URL, NewsDbAdapter.KEY_IMAGE,
	    NewsDbAdapter.KEY_CREATOR, NewsDbAdapter.KEY_PUB_DATE };

    public NewsDbAdapter(final Context context) {
	super(context);
    }

    /**
     * Create a new news If the news is successfully created return the new
     * rowId for that note, otherwise return a -1 to indicate failure.
     */
    @Override
    public long create(final Model news) {
	return db.insert(NewsDbAdapter.TABLE_TITLE, null, news.getValues());
    }

    /**
     * Delete a news
     */

    @Override
    public int delete(final Model news) {
	return db.delete(NewsDbAdapter.TABLE_TITLE, BaseDbAdapter.KEY_ROW_ID
		+ "=" + news.getID(), null);
    }

    public News get(final int rowId) throws SQLException {

	final Cursor mCursor = db.query(true, NewsDbAdapter.TABLE_TITLE,
		columns, BaseDbAdapter.KEY_ROW_ID + "=" + rowId, null, null,
		null, null, null);
	if (mCursor != null) {
	    mCursor.moveToFirst();
	} else {
	    return null;
	}
	return new News(mCursor.getLong(0), mCursor.getString(1),
		mCursor.getString(2), mCursor.getString(3),
		mCursor.getString(4), mCursor.getString(5),
		mCursor.getString(6));
    }

    /**
     * Return a Cursor positioned at the defined news
     */

    @Override
    public Cursor get(final long rowId) throws SQLException {
	final Cursor mCursor = db.query(true, NewsDbAdapter.TABLE_TITLE,
		columns, BaseDbAdapter.KEY_ROW_ID + "=" + rowId, null, null,
		null, null, null);
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
	return db.query(NewsDbAdapter.TABLE_TITLE, columns, null, null, null,
		null, null);
    }

    /**
     * Updates a news. If the news is successfully updated return true otherwise
     * return false to indicate failure.
     */
    @Override
    public int update(final Model news) {
	return db.update(NewsDbAdapter.TABLE_TITLE, news.getValues(),
		BaseDbAdapter.KEY_ROW_ID + "=" + news.getID(), null);
    }
}