package com.applets.utils.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.applets.models.Model;

public class ProgrammesDbAdapter extends BaseDbAdapter {

    public static final String TABLE_NAME = "programmes";
    public static final String KEY_NAME = "name";
    public static final String KEY_SHORT_NAME = "shortname";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PROGRAMME_ID = "programme_id";
    public static final String KEY_URL = "url";
    public static final String KEY_URL_PDF = "url_pdf";

    public ProgrammesDbAdapter(Context context) {
	this.context = context;
    }

    @Override
    public long create(Model model) {
	return db.insert(TABLE_NAME, null, model.getValues());
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
	return db
		.query(TABLE_NAME, new String[] { KEY_PROGRAMME_ID, KEY_NAME,
			KEY_SHORT_NAME, KEY_DESCRIPTION }, null, null, null,
			null, null);
    }

    @Override
    public int delete(Model model) {
	// TODO Auto-generated method stub
	return 0;
    }

}
