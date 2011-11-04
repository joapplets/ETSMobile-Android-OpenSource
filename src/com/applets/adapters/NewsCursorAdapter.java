package com.applets.adapters;

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

    private LayoutInflater inflater;

    public NewsCursorAdapter(Context context, Cursor c) {
	super(context, c, true);
	this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, Context ctx, Cursor cursor) {
	NewsRowWrapper wrapper = (NewsRowWrapper) view.getTag();

	if (wrapper == null) {
	    wrapper = getWarpper(cursor, view);
	}
	wrapper.setTitle(getName(cursor));
	wrapper.setDescription(getDescription(cursor));
	wrapper.setImage(getImage(cursor));
    }

    @Override
    public View newView(Context ctx, Cursor cursor, ViewGroup parent) {

	final View v = inflater.inflate(R.layout.news_row, parent);
	v.setTag(getWarpper(cursor, v));
	
	return v;
    }
    /**
     * Creates a new RowWrapper and inits the values
     * @param cursor The row information
     * @param v The inflated view
     * @return A wrapper methods to manipulate the layout
     */
    private NewsRowWrapper getWarpper(Cursor cursor, View v) {
	NewsRowWrapper wrapper = new NewsRowWrapper(v);
	wrapper.setTitle(getName(cursor));
	wrapper.setDescription(getDescription(cursor));
	wrapper.setImage(getImage(cursor));
	return wrapper;
    }

    private String getImage(Cursor cursor) {
	return cursor.getString(cursor.getColumnIndex(NewsDbAdapter.KEY_IMAGE));
    }

    private String getDescription(Cursor cursor) {
	return cursor.getString(cursor
		.getColumnIndex(NewsDbAdapter.KEY_DESCRIPTION));
    }

    private String getName(Cursor cursor) {
	return cursor.getString(cursor.getColumnIndex(NewsDbAdapter.KEY_NAME));
    }

}
