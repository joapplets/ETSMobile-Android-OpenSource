package ca.etsmtl.applets.etsmobile.tools.db;

import android.content.ContentValues;
import android.database.Cursor;
import ca.etsmtl.applets.etsmobile.models.Model;

public abstract class BaseDBAdapter {

	public BaseDBAdapter() {
		// TODO Auto-generated constructor stub
	}

	abstract long insert(Model model);

	abstract boolean update(ContentValues cv1, ContentValues cv2);

	abstract boolean delete(long id);

	abstract Cursor getAll();

	abstract <T extends Model> T get(long id);

}
