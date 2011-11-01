package com.applets.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applets.R;
import com.applets.utils.db.NewsDbAdapter;

public class NewsCursorAdapter extends SimpleCursorAdapter {

    private Context context;

    public NewsCursorAdapter(Context context, int layout, Cursor c,
	    String[] from, int[] to, int flags) {
	super(context, layout, c, from, to, flags);
	this.context = context;
    }

    @Override
    public void bindView(View view, Context ctx, Cursor cursor) {
	String name = cursor.getString(cursor.getColumnIndex(NewsDbAdapter.KEY_NAME));
	setTitle(view, name);
    }

    @Override
    public View newView(Context ctx, Cursor cursor, ViewGroup parent) {

	View v = LayoutInflater.from(context)
		.inflate(R.layout.news_row, parent);
	String name = cursor.getString(cursor
		.getColumnIndex(NewsDbAdapter.KEY_NAME));
	setTitle(v, name);
	return v;
    }
    
    private void setTitle(View v, String name) {
	TextView title = (TextView)v.findViewById(R.id.news_row_title);
	title.setText(name);
    }
    
}
