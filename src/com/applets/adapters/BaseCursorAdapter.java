package com.applets.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applets.adapters.wrappers.NewsRowWrapper;
import com.applets.utils.db.NewsDbAdapter;
import com.markupartist.android.widget.actionbar.R;

public class BaseCursorAdapter extends CursorAdapter {

	private final LayoutInflater inflater;

	public BaseCursorAdapter(final Context context, final Cursor c) {
		super(context, c, true);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(final View view, final Context ctx, final Cursor cursor) {
		NewsRowWrapper wrapper = (NewsRowWrapper) view.getTag();

		if (wrapper == null) {
			wrapper = getWarpper(cursor, view);
		}
		wrapper.setTitle(getName(cursor));
		wrapper.setDescription(getDescription(cursor));
	}

	private String getDescription(final Cursor cursor) {
		return cursor.getString(cursor
				.getColumnIndex(NewsDbAdapter.KEY_DESCRIPTION));
	}

	private String getName(final Cursor cursor) {
		return cursor.getString(cursor.getColumnIndex(NewsDbAdapter.KEY_NAME));
	}

	/**
	 * Creates a new RowWrapper and inits the values
	 * 
	 * @param cursor
	 *            The row information
	 * @param v
	 *            The inflated view
	 * @return A wrapper methods to manipulate the layout
	 */
	private NewsRowWrapper getWarpper(final Cursor cursor, final View v) {
		final NewsRowWrapper wrapper = new NewsRowWrapper(v);
		wrapper.setTitle(getName(cursor));
		wrapper.setDescription(getDescription(cursor));
		return wrapper;
	}

	@Override
	public View newView(final Context ctx, final Cursor cursor,
			final ViewGroup parent) {

		final View v = inflater.inflate(R.layout.basic_row, null);
		v.setTag(getWarpper(cursor, v));
		return v;
	}

}
