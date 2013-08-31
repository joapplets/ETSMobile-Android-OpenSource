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
import ca.etsmtl.applets.etsmobile.tools.db.ActivityCalendarTableHelper;
import ca.etsmtl.applets.etsmobile.tools.db.BottinTableHelper;
import ca.etsmtl.applets.etsmobile.tools.db.ETSMobileOpenHelper;
import ca.etsmtl.applets.etsmobile.tools.db.JoursRemplaceTableHelper;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;
import ca.etsmtl.applets.etsmobile.tools.db.SessionTableHelper;

public class ETSMobileContentProvider extends android.content.ContentProvider {

	private static final String AUTHORITY = "ca.etsmtl.applets.etsmobile.data";

	private static final String NEWS_PATH = "news";
	private static final String BOTTIN_PATH = "bottin";
	private static final int ALL_NEWS = 1;
	private static final int SINGLE_NEWS = 2;
	private static final int ALL_BOTTIN = 3;
	private static final int SINGLE_BOTTIN = 4;
	private static final String SESSION_PATH = "session";
	private static final String ACT_PATH = "activity";
	private static final String JOUR_PATH = "jours";
	private static final int ALL_NEWS = 1;
	private static final int SINGLE_NEWS = 2;
	private static final int ALL_BOTTIN = 3;
	private static final int SINGLE_BOTTIN = 4;
	private static final int ALL_SESSION = 5;
	private static final int SINGLE_SESSION = 6;
	private static final int ALL_ACT = 7;
	private static final int SINGLE_ACT = 8;
	private static final int ALL_JOUR = 9;
	private static final int SINGLE_JOUR = 10;

	/**
	 * NEWS
	 */
	// --> content://ca.etsmtl.applets.etsmobile/news
	public static final Uri CONTENT_URI_NEWS = Uri.parse("content://"
			+ ETSMobileContentProvider.AUTHORITY + "/"
			+ ETSMobileContentProvider.NEWS_PATH);

	// --> cursor.dir/news
	public static final String CONTENT_MULTIPLE_ITEM_NEWS = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + ETSMobileContentProvider.NEWS_PATH;

	// --> cursor.item/news
	public static final String CONTENT_SINGLE_ITEM_NEWS = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + ETSMobileContentProvider.NEWS_PATH;

	/**
	 * BOTTIN
	 */
	// --> content://ca.etsmtl.applets.etsmobile/bottin
	public static final Uri CONTENT_URI_BOTTIN = Uri.parse("content://"
			+ ETSMobileContentProvider.AUTHORITY + "/"
			+ ETSMobileContentProvider.BOTTIN_PATH);

	// --> cursor.dir/bottin
	public static final String CONTENT_MULTIPLE_BOTTIN = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + ETSMobileContentProvider.BOTTIN_PATH;

	// --> cursor.item/session
	public static final String CONTENT_SINGLE_BOTTIN = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + ETSMobileContentProvider.BOTTIN_PATH;

	/**
	 * Sessions
	 */
	// --> content://ca.etsmtl.applets.etsmobile/session
	public static final Uri CONTENT_URI_SESSION = Uri.parse("content://"
			+ ETSMobileContentProvider.AUTHORITY + "/"
			+ ETSMobileContentProvider.SESSION_PATH);

	// --> cursor.dir/session
	public static final String CONTENT_MULTIPLE_SESSION = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + ETSMobileContentProvider.SESSION_PATH;

	// --> cursor.item/session
	public static final String CONTENT_SINGLE_SESSION = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + ETSMobileContentProvider.SESSION_PATH;

	/**
	 * Activity Calendar
	 */
	public static final Uri CONTENT_URI_ACTIVITY_CALENDAR = Uri
			.parse("content://" + ETSMobileContentProvider.AUTHORITY + "/"
					+ ETSMobileContentProvider.ACT_PATH);
	// --> cursor.dir/act
	public static final String CONTENT_MULTIPLE_ACT = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + ETSMobileContentProvider.ACT_PATH;

	// --> cursor.item/act
	public static final String CONTENT_SINGLE_ACT = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + ETSMobileContentProvider.ACT_PATH;

	/**
	 * Jours Remplace
	 */

	// --> cursor.dir/act
	public static final String CONTENT_MULTIPLE_JOUR = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/" + ETSMobileContentProvider.JOUR_PATH;

	// --> cursor.item/act
	public static final String CONTENT_SINGLE_JOUR = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/" + ETSMobileContentProvider.JOUR_PATH;

	public static final Uri CONTENT_URI_JOURS_REMPLACE = Uri.parse("content://"
			+ ETSMobileContentProvider.AUTHORITY + "/"
			+ ETSMobileContentProvider.JOUR_PATH);

	//
	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

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
		// --> ca.etsmtl.applets.etsmobile - session - 5
		ETSMobileContentProvider.sURIMatcher.addURI(
				ETSMobileContentProvider.AUTHORITY,
				ETSMobileContentProvider.SESSION_PATH,
				ETSMobileContentProvider.ALL_SESSION);
		// --> ca.etsmtl.applets.etsmobile - session/* - 6
		ETSMobileContentProvider.sURIMatcher.addURI(
				ETSMobileContentProvider.AUTHORITY,
				ETSMobileContentProvider.SESSION_PATH + "/#",
				ETSMobileContentProvider.SINGLE_SESSION);
		// --> ca.etsmtl.applets.etsmobile - session - 7
		ETSMobileContentProvider.sURIMatcher.addURI(
				ETSMobileContentProvider.AUTHORITY,
				ETSMobileContentProvider.ACT_PATH,
				ETSMobileContentProvider.ALL_ACT);
		// --> ca.etsmtl.applets.etsmobile - session/* - 8
		ETSMobileContentProvider.sURIMatcher.addURI(
				ETSMobileContentProvider.AUTHORITY,
				ETSMobileContentProvider.ACT_PATH + "/#",
				ETSMobileContentProvider.SINGLE_ACT);

		// --> ca.etsmtl.applets.etsmobile - session - 9
		ETSMobileContentProvider.sURIMatcher.addURI(
				ETSMobileContentProvider.AUTHORITY,
				ETSMobileContentProvider.JOUR_PATH,
				ETSMobileContentProvider.ALL_JOUR);

		// --> ca.etsmtl.applets.etsmobile - session/* - 10
		ETSMobileContentProvider.sURIMatcher.addURI(
				ETSMobileContentProvider.AUTHORITY,
				ETSMobileContentProvider.JOUR_PATH + "/#",
				ETSMobileContentProvider.SINGLE_JOUR);
	}

	private ETSMobileOpenHelper helper;

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
				if (value != null) {
					writable.insert(getRequestedTable(uri.getPath()), null,
							value);
				}
			}
			writable.setTransactionSuccessful();
			numInserted = values.length;
		} finally {
			writable.endTransaction();
			getContext().getContentResolver().notifyChange(uri, null);
		}
		writable.close();
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
		} else if (path.contains(ETSMobileContentProvider.BOTTIN_PATH)) {
			tableName = BottinTableHelper.TABLE_NAME;
		} else if (path.contains(ETSMobileContentProvider.SESSION_PATH)) {
			tableName = SessionTableHelper.TABLE_NAME;
		} else if (path.contains(ETSMobileContentProvider.JOUR_PATH)) {
			tableName = JoursRemplaceTableHelper.TABLE_NAME;
		} else if (path.contains(ETSMobileContentProvider.ACT_PATH)) {
			tableName = ActivityCalendarTableHelper.TABLE_NAME;
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
		case ALL_SESSION:
			return ETSMobileContentProvider.CONTENT_MULTIPLE_SESSION;
		case SINGLE_SESSION:
			return ETSMobileContentProvider.CONTENT_SINGLE_SESSION;
		case ALL_ACT:
			return ETSMobileContentProvider.CONTENT_MULTIPLE_ACT;
		case SINGLE_ACT:
			return ETSMobileContentProvider.CONTENT_SINGLE_ACT;
		case ALL_JOUR:
			return ETSMobileContentProvider.CONTENT_MULTIPLE_JOUR;
		case SINGLE_JOUR:
			return ETSMobileContentProvider.CONTENT_SINGLE_JOUR;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {

		final SQLiteDatabase writable = helper.getWritableDatabase();
		try {
			if (writable.isOpen()) {
				writable.beginTransaction();
				writable.insert(getRequestedTable(uri.getPath()), null, values);
				writable.setTransactionSuccessful();
			}
			return uri;
		} catch (final IllegalStateException e) {
		} finally {
			writable.endTransaction();
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return uri;
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
			break;
		case SINGLE_BOTTIN:
			queryBuilder.setTables(BottinTableHelper.TABLE_NAME);
			buildBottinQuerySingle(uri, queryBuilder);
			break;
		case ALL_SESSION:
			queryBuilder.setTables(SessionTableHelper.TABLE_NAME);
			break;
		case SINGLE_SESSION:
			break;
		case ALL_ACT:
			queryBuilder.setTables(ActivityCalendarTableHelper.TABLE_NAME);
			break;
		case SINGLE_ACT:
			// TODO:
			break;
		case ALL_JOUR:
			queryBuilder.setTables(JoursRemplaceTableHelper.TABLE_NAME);
			break;
		case SINGLE_JOUR:
			// TODO:
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		Cursor cursor = null;

		try {
			final SQLiteDatabase db = helper.getWritableDatabase();
			if (db.isOpen()) {
				cursor = queryBuilder.query(db, columns, selection,
						selectionArgs, null, null, sortOrder);
				cursor.setNotificationUri(getContext().getContentResolver(),
						uri);
			}
		} catch (final SQLiteMisuseException e) {
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
