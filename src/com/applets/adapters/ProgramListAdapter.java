package com.applets.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class ProgramListAdapter extends CursorAdapter {

    private Context context;

    public ProgramListAdapter(Context context, Cursor c) {
	super(context, c, true);
	this.context = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
	// TODO Auto-generated method stub

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
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
