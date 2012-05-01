package ca.etsmtl.applets.etsmobile.tools.db;

import java.util.ArrayList;

import ca.etsmtl.applets.etsmobile.models.News;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NewsDB {
	
	/**
	 * Everything related to a news.
	 */
	private final String NEWS_TABLE = SQLDBHelper.NEWS_TABLE;
	private final String NEWS_TITLE = SQLDBHelper.NEWS_TITLE;
	private final String NEWS_DATE = SQLDBHelper.NEWS_DATE;
	private final String NEWS_DESCRIPTION = SQLDBHelper.NEWS_DESCRIPTION;
	private final String NEWS_GUID = SQLDBHelper.NEWS_GUID;
	private final String NEWS_SOURCE = SQLDBHelper.NEWS_SOURCE;

	// db info.
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = SQLDBHelper.DB_NAME;
	
	// db helper
	private static SQLDBHelper db = null;
	
	//the unique instance of the db
	private static NewsDB instance = null;
	
	// private constructor so we can only have one intance of the db
	private NewsDB(){}
	
	// Here I applied the singleton pattern because I only want
	// one instance of the db (which contains all the data of the app)
	public static NewsDB getInstance(Context c){
		
		// if instance is null, which means the we are opening the app
		if(instance == null){
			
			// create one instance
			instance = new NewsDB();
			
			// create the db, if it's already in the filesystem it will not recreate it
			db = new SQLDBHelper(c , DB_NAME, null, DB_VERSION);
		}
		
		// return the instance.
		db.close();
		return instance;
	}
	
	public void insertNews(String title, long l, String description, String guid, String source){
		ContentValues cv = new ContentValues();
		
		// put into the date column the String date
		cv.put(NEWS_TITLE, title);
			
		// same here...
		cv.put(NEWS_DATE, l);
		
		//same...
		cv.put(NEWS_DESCRIPTION, description);
			
		//same...
		cv.put(NEWS_GUID, guid);
		
		//same...
		cv.put(NEWS_SOURCE, source);
		
		// set the db into writable mode, then insert the values into the table
		db.getWritableDatabase().insert(NEWS_TABLE, null, cv);
			
		// close the db
		db.close();
	}
	
	public ArrayList<News> getAllNews(){
		
		// the vector that will contain the selected notes
		ArrayList<News> v = new ArrayList<News>();
		
		// set the database to be readable
		SQLiteDatabase readDB  = db.getReadableDatabase();
		
		// Equivalent to : SELECT * FROM NOTES ORDER BY NEWS_DATE DESC
		Cursor c = readDB.query(NEWS_TABLE, new String[] {"*"}, null, null, null, null, NEWS_DATE + " DESC");
		
		News news;
		while(c.moveToNext()){
			
			news = new News();
			
			news.setTitle(c.getString(c.getColumnIndex(NEWS_TITLE)));
			news.setPubDate(c.getLong(c.getColumnIndex(NEWS_DATE)));
			news.setDescription(c.getString(c.getColumnIndex(NEWS_DESCRIPTION)));
			news.setGuid(c.getString(c.getColumnIndex(NEWS_GUID)));
			news.setSource(c.getString(c.getColumnIndex(NEWS_SOURCE)));
			
			v.add(news);
		}
		
		c.close();
		readDB.close();
		
		return v;
	}
	
	public ArrayList<News> getNewsBySource(ArrayList<String> source){
		
		ArrayList<News> list = new ArrayList<News>();
		
		// set the database to be readable
		SQLiteDatabase readDB  = db.getReadableDatabase();
		
		String sources = "";
		for(int i = 0; i < source.size(); i++){
			sources += NEWS_SOURCE + " LIKE \"" + source.get(i).toString() + "\"";
			if(i < source.size() - 1){
				sources += " OR ";
			}
		}
		
		Cursor c = readDB.query(NEWS_TABLE, new String[] {"*"}, sources, null, null, null, NEWS_DATE + " DESC");
		
		while(c.moveToNext()){
			News n = new News();
			n.setTitle(c.getString(c.getColumnIndex(NEWS_TITLE)));
			n.setPubDate(c.getLong(c.getColumnIndex(NEWS_DATE)));
			n.setDescription(c.getString(c.getColumnIndex(NEWS_DESCRIPTION)));
			n.setGuid(c.getString(c.getColumnIndex(NEWS_GUID)));
			n.setSource(c.getString(c.getColumnIndex(NEWS_SOURCE)));
			
			list.add(n);
		}
		
		c.close();
		readDB.close();
		
		return list;
	}
	
	public News getNewsByGUID(String guid){
		
		News n = null;
		
		// set the database to be readable
		SQLiteDatabase readDB  = db.getReadableDatabase();
		
		Cursor c = readDB.query(NEWS_TABLE, new String[] {"*"}, NEWS_GUID + " LIKE \"" + guid + "\"", null, null, null, null);
		if(c.moveToNext()){
			n = new News();
			n.setTitle(c.getString(c.getColumnIndex(NEWS_TITLE)));
			n.setPubDate(c.getLong(c.getColumnIndex(NEWS_DATE)));
			n.setDescription(c.getString(c.getColumnIndex(NEWS_DESCRIPTION)));
			n.setGuid(c.getString(c.getColumnIndex(NEWS_GUID)));
		}
		
		c.close();
		readDB.close();
		
		return n;
	}
}
