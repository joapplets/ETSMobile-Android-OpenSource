package ca.etsmtl.applets.etsmobile.providers;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import ca.etsmtl.applets.etsmobile.tools.db.BottinTableHelper;

public class BottinContentProvider extends ContentProvider {

	private static final String AUTHORITY = "ca.etsmtl.applets.etsmobile";

	private static final String BASE_PATH = "bottin";
	private static final int ALL_ENTRIES = 1;
	private static final int SINGLE_ENTRY = 2;

	// --> content://ca.etsmtl.applets.etsmobile/news
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	// --> vnd.android.cursor.dir/news
	public static final String CONTENT_MULTIPLE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + BASE_PATH;

	// --> vnd.android.cursor.item/news
	public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + BASE_PATH;

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {

		// --> ca.etsmtl.applets.etsmobile - news - 1
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, ALL_ENTRIES);

		// --> ca.etsmtl.applets.etsmobile - news/* - 2
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SINGLE_ENTRY);
	}

	private BottinTableHelper helper;

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		return 0;
	}

	@Override
	public synchronized String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ALL_ENTRIES:
			return CONTENT_MULTIPLE_ITEM;
		case SINGLE_ENTRY:
			return CONTENT_SINGLE_ITEM;
		default:
			return null;
		}
	}

	@Override
	public synchronized Uri insert(Uri uri, ContentValues values) {

		if (sURIMatcher.match(uri) == ALL_ENTRIES) {
			SQLiteDatabase writable = helper.getWritableDatabase();
			try {
				if (writable.isOpen()) {
					writable.beginTransaction();
					writable.insert(BottinTableHelper.TABLE_NAME, null, values);
					writable.setTransactionSuccessful();
				}
			} catch (IllegalStateException e) {
			} finally {
				writable.endTransaction();
				writable.close();
				getContext().getContentResolver().notifyChange(uri, null);
			}
			return uri;
		}
		return null;
	}

	@Override
	public synchronized boolean onCreate() {
		helper = new BottinTableHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] columns, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		verifyColumnExists(columns);

		queryBuilder.setTables(BottinTableHelper.TABLE_NAME);

		int requestType = sURIMatcher.match(uri);

		switch (requestType) {
		case ALL_ENTRIES:
			String where = "nom like ? OR prenom like ? OR service like ?";
//			for (int i = 0; i < selectionArgs.length; i++) {
//				where += BottinTableHelper.BOTTIN_NOM + " LIKE \""
//						+ selectionArgs[i] + "\"";
//				if (i < selectionArgs.length - 1) {
//					where += " OR ";
//				}
//			}
//			if (where.equals("")) {
//				where = BottinTableHelper.BOTTIN_NOM + " LIKE \"nothing\"";
//			}
			queryBuilder.appendWhere(where);
			break;
		case SINGLE_ENTRY:
			queryBuilder.appendWhere(BottinTableHelper.BOTTIN_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, columns, selection, null, null,
				null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		return 0;
	}

	// Source : http://www.vogella.com/articles/AndroidSQLite/article.html
	private synchronized void verifyColumnExists(String[] columns) {


		if (columns != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(columns));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(BottinTableHelper.AVAILABLE));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"L'un des champs demandés n'existe pas");
			}
		}
	}
}
