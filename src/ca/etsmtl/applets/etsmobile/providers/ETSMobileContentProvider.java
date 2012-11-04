package ca.etsmtl.applets.etsmobile.providers;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteMisuseException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import ca.etsmtl.applets.etsmobile.tools.db.BottinTableHelper;
import ca.etsmtl.applets.etsmobile.tools.db.ETSMobileOpenHelper;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;

public class ETSMobileContentProvider extends android.content.ContentProvider {

	private static final String AUTHORITY = "ca.etsmtl.applets.etsmobile.data";

	private static final String NEWS_PATH = "news";
	private static final String BOTTIN_PATH = "bottin";
	private static final int ALL_NEWS = 1;
	private static final int SINGLE_NEWS = 2;
	private static final int ALL_BOTTIN = 3;
	private static final int SINGLE_BOTTIN = 4;

	/**
	 * NEWS
	 */
	// --> content://ca.etsmtl.applets.etsmobile/news
	public static final Uri CONTENT_URI_NEWS = Uri.parse("content://"
			+ ETSMobileContentProvider.AUTHORITY + "/"
			+ ETSMobileContentProvider.NEWS_PATH);

	// --> vnd.android.cursor.dir/news
	public static final String CONTENT_MULTIPLE_ITEM_NEWS = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + ETSMobileContentProvider.NEWS_PATH;

	// --> vnd.android.cursor.item/news
	public static final String CONTENT_SINGLE_ITEM_NEWS = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + ETSMobileContentProvider.NEWS_PATH;

	/**
	 * BOTTIN
	 */
	// --> content://ca.etsmtl.applets.etsmobile/bottin
	public static final Uri CONTENT_URI_BOTTIN = Uri.parse("content://"
			+ ETSMobileContentProvider.AUTHORITY + "/"
			+ ETSMobileContentProvider.BOTTIN_PATH);

	// --> vnd.android.cursor.dir/bottin
	public static final String CONTENT_MULTIPLE_BOTTIN = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + ETSMobileContentProvider.BOTTIN_PATH;

	// --> vnd.android.cursor.item/bottin
	public static final String CONTENT_SINGLE_BOTTIN = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + ETSMobileContentProvider.BOTTIN_PATH;

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {

		// --> ca.etsmtl.applets.etsmobile - news - 1
		ETSMobileContentProvider.sURIMatcher.addURI(
				ETSMobileContentProvider.AUTHORITY,
				ETSMobileContentProvider.NEWS_PATH,
				ETSMobileContentProvider.ALL_NEWS);

		// --> ca.etsmtl.applets.etsmobile - news/* - 2
		ETSMobileContentProvider.sURIMatcher.addURI(
				ETSMobileContentProvider.AUTHORITY,
				ETSMobileContentProvider.NEWS_PATH + "/#",
				ETSMobileContentProvider.SINGLE_NEWS);

		// --> ca.etsmtl.applets.etsmobile - bottin - 3
		ETSMobileContentProvider.sURIMatcher.addURI(
				ETSMobileContentProvider.AUTHORITY,
				ETSMobileContentProvider.BOTTIN_PATH,
				ETSMobileContentProvider.ALL_BOTTIN);

		// --> ca.etsmtl.applets.etsmobile - bottin/* - 4
		ETSMobileContentProvider.sURIMatcher.addURI(
				ETSMobileContentProvider.AUTHORITY,
				ETSMobileContentProvider.BOTTIN_PATH + "/#",
				ETSMobileContentProvider.SINGLE_BOTTIN);
	}

	private ETSMobileOpenHelper helper;

	private String buildBottinQueryAll(final String[] columns,
			final String[] selectionArgs, final SQLiteQueryBuilder queryBuilder) {
		// String where =
		// " nom like ? OR prenom like ? OR service like ? or emplacement like ? or courriel like ? or titre like ?";
		String where = "";

		for (int i = 0; i < columns.length - 1; i++) {
			where += columns[i] + " like ? OR ";
		}
		where += columns[columns.length - 1] + " like ? ";
		// queryBuilder.appendWhere(where);

		// if (selectionArgs == null) {
		// selectionArgs = new String[columns.length];
		// for (int i = 0; i < columns.length; i++) {
		// selectionArgs[i] = "%";
		// }
		// } else if (selectionArgs.length == 1) {
		// final String sel = selectionArgs[0];
		// selectionArgs = new String[columns.length];
		// for (int i = 0; i < columns.length; i++) {
		// selectionArgs[i] = "%" + sel + "%";
		// }
		//
		// }
		return where;
	}

	private void buildBottinQuerySingle(final Uri uri,
			final SQLiteQueryBuilder queryBuilder) {
		queryBuilder.appendWhere(BottinTableHelper.BOTTIN__ID + "="
				+ uri.getLastPathSegment());
	}

	private void buildNewsQueryAll(final String[] selectionArgs,
			final SQLiteQueryBuilder queryBuilder) {
		String where = "";
		for (int i = 0; i < selectionArgs.length - 1; i++) {
			where += NewsTableHelper.NEWS_SOURCE + " LIKE ? OR ";
		}
		where += NewsTableHelper.NEWS_SOURCE + " LIKE ? ";

		if (where.equals("")) {
			where = NewsTableHelper.NEWS_SOURCE + " LIKE \"nothing\"";
		}
		queryBuilder.appendWhere(where);
	}

	private void buildNewsQuerySingle(final Uri uri,
			final SQLiteQueryBuilder queryBuilder) {
		queryBuilder.appendWhere(NewsTableHelper.NEWS_ID + "="
				+ uri.getLastPathSegment());
	}

	@Override
	public int bulkInsert(final Uri uri, final ContentValues[] values) {

		int numInserted = 0;
		final SQLiteDatabase writable = helper.getWritableDatabase();
		writable.beginTransaction();
		try {
			for (final ContentValues value : values) {
				writable.insert(getRequestedTable(uri.getPath()), null, value);
			}
			writable.setTransactionSuccessful();
			numInserted = values.length;
		} finally {
			writable.endTransaction();
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return numInserted;
	}

	@Override
	public int delete(final Uri uri, final String selection,
			final String[] selectionArgs) {
		return 0;
	}

	private String getRequestedTable(final String path) {
		String tableName = "";
		if (path.contains(ETSMobileContentProvider.NEWS_PATH)) {
			tableName = NewsTableHelper.TABLE_NAME;
		} else {
			tableName = BottinTableHelper.TABLE_NAME;
		}
		return tableName;
	}

	@Override
	public String getType(final Uri uri) {
		final int uriType = ETSMobileContentProvider.sURIMatcher.match(uri);
		switch (uriType) {
		case ALL_NEWS:
			return ETSMobileContentProvider.CONTENT_MULTIPLE_ITEM_NEWS;
		case SINGLE_NEWS:
			return ETSMobileContentProvider.CONTENT_SINGLE_ITEM_NEWS;
		case ALL_BOTTIN:
			return ETSMobileContentProvider.CONTENT_MULTIPLE_BOTTIN;
		case SINGLE_BOTTIN:
			return ETSMobileContentProvider.CONTENT_SINGLE_BOTTIN;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {

		if (ETSMobileContentProvider.sURIMatcher.match(uri) == ETSMobileContentProvider.ALL_NEWS) {
			final SQLiteDatabase writable = helper.getWritableDatabase();
			try {
				if (writable.isOpen()) {
					writable.beginTransaction();
					writable.insert(getRequestedTable(uri.getPath()), null,
							values);
					writable.setTransactionSuccessful();
				}
			} catch (final IllegalStateException e) {
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
	public boolean onCreate() {
		helper = new ETSMobileOpenHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(final Uri uri, final String[] columns,
			String selection, final String[] selectionArgs,
			final String sortOrder) {

		final int requestType = ETSMobileContentProvider.sURIMatcher.match(uri);
		final String path = uri.getPath();
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		verifyColumnExists(columns, path);

		switch (requestType) {
		case ALL_NEWS:
			queryBuilder.setTables(NewsTableHelper.TABLE_NAME);
			buildNewsQueryAll(selectionArgs, queryBuilder);
			break;
		case SINGLE_NEWS:
			queryBuilder.setTables(NewsTableHelper.TABLE_NAME);
			buildNewsQuerySingle(uri, queryBuilder);
			break;
		case ALL_BOTTIN:
			queryBuilder.setTables(BottinTableHelper.TABLE_NAME);
			selection = buildBottinQueryAll(columns, selectionArgs,
					queryBuilder);
			break;
		case SINGLE_BOTTIN:
			queryBuilder.setTables(BottinTableHelper.TABLE_NAME);
			buildBottinQuerySingle(uri, queryBuilder);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		Cursor cursor = null;
		try {
			final SQLiteDatabase db = helper.getWritableDatabase();
			cursor = queryBuilder.query(db, columns, selection, selectionArgs,
					null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		} catch (SQLiteMisuseException e) {
			e.printStackTrace();
			return cursor;
		}
		return cursor;
	}

	@Override
	public int update(final Uri uri, final ContentValues values,
			final String selection, final String[] selectionArgs) {
		return 0;
	}

	// Source : http://www.vogella.com/articles/AndroidSQLite/article.html
	private void verifyColumnExists(final String[] columns, final String path) {
		if (columns != null) {
			final HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(columns));

			HashSet<String> availableColumns = null;
			if (path.contains(ETSMobileContentProvider.NEWS_PATH)) {
				availableColumns = new HashSet<String>(
						Arrays.asList(NewsTableHelper.AVAILABLE));
			} else {
				availableColumns = new HashSet<String>(
						Arrays.asList(BottinTableHelper.AVAILABLE));
			}
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				String avail = "{\t";
				for (final String string : availableColumns) {
					avail += string + "\t";
				}
				avail += "}";

				String request = "{\t";
				for (final String string : requestedColumns) {
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
