package com.applets.utils.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.applets.models.Model;

public class ProgrammesDbAdapter extends BaseDbAdapter {

	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_NAME = "name";
	public static final String KEY_PROGRAMME_ID = "programme_id";
	public static final String KEY_SHORT_NAME = "shortname";
	public static final String KEY_URL = "url";
	public static final String KEY_URL_PDF = "url_pdf";
	public static final String TABLE_NAME = "programmes";

	public ProgrammesDbAdapter(final Context context) {
		super(context);
	}

	@Override
	public long create(final Model model) {
		return db.insert(ProgrammesDbAdapter.TABLE_NAME, null,
				model.getValues());
	}

	@Override
	public int delete(final Model model) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Cursor get(final long rowId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor getAll() {
		return db.query(ProgrammesDbAdapter.TABLE_NAME, new String[] {
				BaseDbAdapter.KEY_ROW_ID, ProgrammesDbAdapter.KEY_PROGRAMME_ID,
				ProgrammesDbAdapter.KEY_NAME,
				ProgrammesDbAdapter.KEY_SHORT_NAME,
				ProgrammesDbAdapter.KEY_DESCRIPTION }, null, null, null, null,
				null);
	}

	@Override
	public int update(final Model model) {
		// TODO Auto-generated method stub
		return 0;
	}

}
