package com.applets.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ProgramListAdapter extends CursorAdapter {

    public ProgramListAdapter(final Context context, final Cursor c) {
	super(context, c, true);
    }

    @Override
    public void bindView(final View view, final Context context,
	    final Cursor cursor) {
	// TODO Auto-generated method stub

    }

    @Override
    public View newView(final Context context, final Cursor cursor,
	    final ViewGroup parent) {
	// TODO Auto-generated method stub
	return null;
    }

    // @Override
    // public View getView(int position, View convertView, ViewGroup parent) {
    // View row = convertView;
    // ProgramWrapper wrapper;
    //
    // if (row == null) {
    // LayoutInflater inflater = context.getLayoutInflater();
    // row = inflater.inflate(R.layout.program_list_row, null);
    // wrapper = new ProgramWrapper(row);
    // row.setTag(wrapper);
    // } else {
    // wrapper = (ProgramWrapper) row.getTag();
    // }
    // Program program = list.get(position);
    // wrapper.setTitle(program.getName());
    // wrapper.setDescription(program.getDescription());
    //
    // return row;
    // }

}
