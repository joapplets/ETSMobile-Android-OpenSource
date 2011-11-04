package com.applets.utils.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.applets.models.Model;

public class CoursDbAdapter extends BaseDbAdapter {

    public CoursDbAdapter(Context context) {
	super(context);
    }

    @Override
    public long create(Model model) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int update(Model model) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public Cursor get(long rowId) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Cursor getAll() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public int delete(Model model) {
	// TODO Auto-generated method stub
	return 0;
    }

}
