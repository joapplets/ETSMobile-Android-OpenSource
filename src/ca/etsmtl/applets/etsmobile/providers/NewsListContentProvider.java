package ca.etsmtl.applets.etsmobile.providers;

import java.util.Arrays;
import java.util.HashSet;

import ca.etsmtl.applets.etsmobile.tools.db.NewsTable;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class NewsListContentProvider extends android.content.ContentProvider{

	private static final String AUTHORITY = "ca.etsmtl.applets.etsmobile";
	
	private static final String BASE_PATH = "news";
	private static final int ALL_NEWS = 1;
	private static final int SINGLE_NEWS = 2;
	
	// --> content://ca.etsmtl.applets.etsmobile/news
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	
	// --> vnd.android.cursor.dir/news
	public static final String CONTENT_MULTIPLE_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BASE_PATH;
	
	// --> vnd.android.cursor.item/news
	public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH;

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		
		// --> ca.etsmtl.applets.etsmobile - news - 1
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, ALL_NEWS);
		
		// --> ca.etsmtl.applets.etsmobile - news/* - 2
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SINGLE_NEWS);
	}

	
	private NewsTableHelper helper;
	
	@Override
	public synchronized boolean onCreate() {
		helper = new NewsTableHelper(getContext());
		return false;
	}
	
	@Override
	public synchronized Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		verifyColumnExists(columns);

		queryBuilder.setTables(NewsTable.NEWS_TABLE);

		int requestType = sURIMatcher.match(uri);
		
		switch (requestType) {
		case ALL_NEWS:
			String where = "";
			for(int i = 0; i < selectionArgs.length; i++){
				where += NewsTable.NEWS_SOURCE + " LIKE \"" + selectionArgs[i] + "\"";
				if(i < selectionArgs.length - 1){
					where += " OR ";
				}
			}
			if(where.equals("")){
				where = NewsTable.NEWS_SOURCE + " LIKE \"nothing\"";
			}
			queryBuilder.appendWhere(where);
			break;
		case SINGLE_NEWS:
			queryBuilder.appendWhere(NewsTable.NEWS_ID + "="+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, columns, selection, null, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}
	
	@Override
	public synchronized int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public synchronized String getType(Uri uri) {
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case ALL_NEWS:
            return CONTENT_MULTIPLE_ITEM;
        case SINGLE_NEWS:
            return CONTENT_SINGLE_ITEM;
        default:
            return null;
        }
	}

	@Override
	public synchronized Uri insert(Uri uri, ContentValues values) {
		
		if(sURIMatcher.match(uri) == ALL_NEWS){
			SQLiteDatabase writable = helper.getWritableDatabase();
			try{
				if(writable.isOpen()){
					writable.beginTransaction();
					writable.insert(NewsTable.NEWS_TABLE, null, values);
					writable.setTransactionSuccessful();
				}
			}catch (IllegalStateException e) {}
				finally{
					writable.endTransaction();
					writable.close();
		            getContext().getContentResolver().notifyChange(uri, null);
				}
			return uri;	
		}
		return null;
	}

	@Override
	public synchronized int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}
	
	//Source : http://www.vogella.com/articles/AndroidSQLite/article.html
	private synchronized void verifyColumnExists (String[] columns) {
		String[] available = { 
				NewsTable.NEWS_DATE,
				NewsTable.NEWS_TITLE,
				NewsTable.NEWS_DESCRIPTION,
				NewsTable.NEWS_GUID,
				NewsTable.NEWS_ID,
				NewsTable.NEWS_SOURCE};
		if (columns != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(columns));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("L'un des champs demandés n'existe pas");
			}
		}
	}


}
