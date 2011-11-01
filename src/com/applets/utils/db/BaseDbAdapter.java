package com.applets.utils.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.applets.models.Model;

public abstract class BaseDbAdapter {

    protected SQLiteDatabase db;
    protected AppEtsOpenHelper dbHelper;
    protected Context context;

    public BaseDbAdapter open() throws SQLException {
	dbHelper = new AppEtsOpenHelper(context, null);
	db = dbHelper.getWritableDatabase();
	return this;
    }

    public abstract long create(Model model);

    public abstract int update(Model model);
    
    public abstract int delete(Model model);

    public abstract Cursor get(long rowId) throws SQLException;

    public abstract Cursor getAll();
    
    /**
     * Close current db and dbHelper
     */
    public void close() {
	db.close();
	dbHelper.close();
    }
}
