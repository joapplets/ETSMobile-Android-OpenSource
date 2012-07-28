package ca.etsmtl.applets.etsmobile.providers;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import ca.etsmtl.applets.etsmobile.tools.db.BottinTableHelper;
import ca.etsmtl.applets.etsmobile.tools.db.ETSMobileOpenHelper;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;

public class ETSMobileContentProvider extends android.content.ContentProvider {

	private static final String AUTHORITY = "ca.etsmtl.applets.etsmobile.data";

	private static final String NEWS_PATH = "news";
	private static final String BOTTIN_PATH = "bottin";
	private static final int ALL = 1;
	private static final int SINGLE = 2;

	/**
	 * NEWS
	 */
	// --> content://ca.etsmtl.applets.etsmobile/news
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + NEWS_PATH);

	// --> vnd.android.cursor.dir/news
	public static final String CONTENT_MULTIPLE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + NEWS_PATH;

	// --> vnd.android.cursor.item/news
	public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + NEWS_PATH;

	/**
	 * BOTTIN
	 */
	// --> content://ca.etsmtl.applets.etsmobile/bottin
	public static final Uri CONTENT_URI_BOTTIN = Uri.parse("content://"
			+ AUTHORITY + "/" + BOTTIN_PATH);

	// --> vnd.android.cursor.dir/bottin
	public static final String CONTENT_MULTIPLE_BOTTIN = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + BOTTIN_PATH;

	// --> vnd.android.cursor.item/bottin
	public static final String CONTENT_SINGLE_BOTTIN = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + BOTTIN_PATH;

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {

		// --> ca.etsmtl.applets.etsmobile - news - 1
		sURIMatcher.addURI(AUTHORITY, NEWS_PATH, ALL);

		// --> ca.etsmtl.applets.etsmobile - news/* - 2
		sURIMatcher.addURI(AUTHORITY, NEWS_PATH + "/#", SINGLE);

		// --> ca.etsmtl.applets.etsmobile - bottin - 3
		sURIMatcher.addURI(AUTHORITY, BOTTIN_PATH, ALL);

		// --> ca.etsmtl.applets.etsmobile - bottin/* - 4
		sURIMatcher.addURI(AUTHORITY, BOTTIN_PATH + "/#", SINGLE);
	}

	private ETSMobileOpenHelper helper;

	@Override
	public synchronized boolean onCreate() {
		helper = new ETSMobileOpenHelper(getContext());
		return false;
	}

	@Override
	public synchronized Cursor query(Uri uri, String[] columns,
			String selection, String[] selectionArgs, String sortOrder) {

		final int requestType = sURIMatcher.match(uri);
		final String path = uri.getPath();
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		verifyColumnExists(columns, path);

		switch (requestType) {
		case ALL:
			if (path.equals(NEWS_PATH)) {
				queryBuilder.setTables(NewsTableHelper.TABLE_NAME);
				buildNewsQueryAll(selectionArgs, queryBuilder);
			} else {
				queryBuilder.setTables(BottinTableHelper.TABLE_NAME);
				buildBottinQueryAll(columns, selectionArgs, queryBuilder);
			}

			break;
		case SINGLE:
			if (path.equals(NEWS_PATH)) {
				queryBuilder.setTables(NewsTableHelper.TABLE_NAME);
				buildNewsQuerySingle(uri, queryBuilder);
			} else {
				queryBuilder.setTables(BottinTableHelper.TABLE_NAME);
				buildBottinQuerySingle(uri, queryBuilder);
			}

			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		final SQLiteDatabase db = helper.getWritableDatabase();
		final Cursor cursor = queryBuilder.query(db, columns, selection, null,
				null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	private void buildBottinQuerySingle(Uri uri, SQLiteQueryBuilder queryBuilder) {
		queryBuilder.appendWhere(BottinTableHelper.BOTTIN__ID + "="
				+ uri.getLastPathSegment());
	}

	private void buildNewsQuerySingle(Uri uri, SQLiteQueryBuilder queryBuilder) {
		queryBuilder.appendWhere(NewsTableHelper.NEWS_ID + "="
				+ uri.getLastPathSegment());
	}

	private void buildNewsQueryAll(String[] selectionArgs,
			SQLiteQueryBuilder queryBuilder) {
		String where = "";
		for (int i = 0; i < selectionArgs.length; i++) {
			where += NewsTableHelper.NEWS_SOURCE + " LIKE \"" + selectionArgs[i]
					+ "\"";
			if (i < selectionArgs.length - 1) {
				where += " OR ";
			}
		}
		if (where.equals("")) {
			where = NewsTableHelper.NEWS_SOURCE + " LIKE \"nothing\"";
		}
		queryBuilder.appendWhere(where);
	}

	private void buildBottinQueryAll(String[] columns, String[] selectionArgs,
			SQLiteQueryBuilder queryBuilder) {
		String where = " nom like ? OR prenom like ? OR service like ? or emplacement like ? or courriel like ? or titre like ?";
		queryBuilder.appendWhere(where);
		if (selectionArgs == null) {
			selectionArgs = new String[columns.length];
			for (int i = 0; i < columns.length; i++) {
				selectionArgs[i] = "%";
			}
		} else if (selectionArgs.length == 1) {
			String sel = selectionArgs[0];
			selectionArgs = new String[columns.length];
			for (int i = 0; i < columns.length; i++) {
				selectionArgs[i] = "%" + sel + "%";
			}

		}
	}

	@Override
	public synchronized int delete(Uri uri, String selection,
			String[] selectionArgs) {
		return 0;
	}

	@Override
	public synchronized String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ALL:
			return CONTENT_MULTIPLE_ITEM;
		case SINGLE:
			return CONTENT_SINGLE_ITEM;
		default:
			return null;
		}
	}

	@Override
	public synchronized Uri insert(Uri uri, ContentValues values) {

		if (sURIMatcher.match(uri) == ALL) {
			SQLiteDatabase writable = helper.getWritableDatabase();
			try {
				if (writable.isOpen()) {
					writable.beginTransaction();
					writable.insert(getRequestedTable(uri.getPath()), null,
							values);
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
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if (uri.getPath().equals(BOTTIN_PATH)) {
			int numInserted = 0;
			SQLiteDatabase writable = helper.getWritableDatabase();
			writable.beginTransaction();
			try {
				for (ContentValues value : values) {

					writable.insert(BottinTableHelper.TABLE_NAME, null, value);
				}
				writable.setTransactionSuccessful();
				numInserted = values.length;
			} finally {
				writable.endTransaction();
				getContext().getContentResolver().notifyChange(uri, null);
			}
			return numInserted;
		}
		return 0;
	}

	private String getRequestedTable(String path) {
		String tableName = "";
		if (path.equals(NEWS_PATH)) {
			tableName = NewsTableHelper.TABLE_NAME;
		} else {
			tableName = BottinTableHelper.TABLE_NAME;
		}
		return tableName;
	}

	@Override
	public synchronized int update(Uri uri, ContentValues values,
			String selection, String[] selectionArgs) {
		return 0;
	}

	// Source : http://www.vogella.com/articles/AndroidSQLite/article.html
	private synchronized void verifyColumnExists(String[] columns, String path) {
		if (columns != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(columns));

			HashSet<String> availableColumns = null;
			if (path.equals(NEWS_PATH)) {
				availableColumns = new HashSet<String>(
						Arrays.asList(BottinTableHelper.AVAILABLE));
			} else {
				availableColumns = new HashSet<String>(
						Arrays.asList(NewsTableHelper.AVAILABLE));
			}
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				String avail = "{\t";
				for (String string : availableColumns) {
					avail += string + "\t";
				}
				avail += "}";

				String request = "{\t";
				for (String string : requestedColumns) {
					request += string + "\t";
				}
				request += "}";

				throw new IllegalArgumentException(
						"L'un des champs demandés n'existe pas\n Available="
								+ avail + "\nRequested=" + request);
			}
		}
	}

}
