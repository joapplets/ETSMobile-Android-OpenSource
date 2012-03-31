package com.applets.adapters;

import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.applets.R;
import com.applets.adapters.wrappers.NewsRowWrapper;
import com.applets.utils.db.NewsDbAdapter;

public class NewsCursorAdapter extends CursorAdapter {

	private int i;
	private final LayoutInflater inflater;

	public NewsCursorAdapter(final Context context, final Cursor c) {
		super(context, c, true);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(final View view, final Context ctx, final Cursor cursor) {
		NewsRowWrapper wrapper = (NewsRowWrapper) view.getTag();

		if (wrapper == null) {
			wrapper = getWarpper(cursor, view);
		}
	}

	private String getDate(final Cursor cursor) {
		return cursor.getString(cursor
				.getColumnIndex(NewsDbAdapter.KEY_PUB_DATE));
	}

	private String getDescription(final Cursor cursor) {
		final String s = cursor.getString(cursor
				.getColumnIndex(NewsDbAdapter.KEY_DESCRIPTION));
		return s;
	}

	private String getImage(final Cursor cursor) {
		return cursor.getString(cursor.getColumnIndex(NewsDbAdapter.KEY_IMAGE));
	}

	private String getTitle(final Cursor cursor) {
		return cursor.getString(cursor.getColumnIndex(NewsDbAdapter.KEY_TITLE));
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
		try {
			wrapper.setTitle(getDescription(cursor));
			wrapper.setDescription(getTitle(cursor));
			wrapper.setDate(getDate(cursor));
			wrapper.setImage(getImage(cursor));
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return wrapper;
	}

	@Override
	public View newView(final Context ctx, final Cursor cursor,
			final ViewGroup parent) {

		final View v = inflater.inflate(R.layout.basic_row, null);

		int c = R.color.white;
		if (i++ % 2 == 0) {
			c = R.color.alt_row;
		}
		v.setBackgroundResource(c);
		v.setTag(getWarpper(cursor, v));
		return v;
	}

}
