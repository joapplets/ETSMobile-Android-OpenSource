package ca.etsmtl.applets.etsmobile.tools.db;

import java.util.ArrayList;
import java.util.Observable;

import ca.etsmtl.applets.etsmobile.models.News;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//TODO SYNCRONIZE ALL THE ACCESS...
public class NewsAdapter extends Observable{
	
	/**
	 * Everything related to a news.
	 */
	public static final String NEWS_TABLE = SQLDBHelper.NEWS_TABLE;
	public static  final String NEWS_TITLE = SQLDBHelper.NEWS_TITLE;
	public static  final String NEWS_DATE = SQLDBHelper.NEWS_DATE;
	public static  final String NEWS_DESCRIPTION = SQLDBHelper.NEWS_DESCRIPTION;
	public static  final String NEWS_GUID = SQLDBHelper.NEWS_GUID;
	public static  final String NEWS_SOURCE = SQLDBHelper.NEWS_SOURCE;
	
	// db info.
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = SQLDBHelper.DB_NAME;
	
	// db helper
	private static SQLDBHelper db = null;
	
	//the unique instance of the db
	private static NewsAdapter instance = null;
	
	// private constructor so we can only have one intance of the db
	private NewsAdapter(){}
	
	// Here I applied the singleton pattern because I only want
	// one instance of the db (which contains all the data of the app)
	public static NewsAdapter getInstance(Context c){
		
		// if instance is null, which means the we are opening the app
		if(instance == null){
			
			// create one instance
			instance = new NewsAdapter();
			
			// create the db, if it's already in the filesystem it will not recreate it
			db = new SQLDBHelper(c , DB_NAME, null, DB_VERSION);
			db.close();
		}
		
		// return the instance.
		return instance;
	}
	
	public void insertNews(String title, long date, String description, String guid, String source){
		// set the db into writable mode, then insert the values into the table
		SQLiteDatabase writable = db.getWritableDatabase();
		if(writable.isOpen()){
			writable.beginTransaction();
			try{
				ContentValues cv = new ContentValues();
				
				// put into the date column the String date
				cv.put(NEWS_TITLE, title);
					
				// same here...
				cv.put(NEWS_DATE, date);
				
				//same...
				cv.put(NEWS_DESCRIPTION, description);
					
				//same...
				cv.put(NEWS_GUID, guid);
				
				//same...
				cv.put(NEWS_SOURCE, source);
				
				writable.insert(NEWS_TABLE, null, cv);
				writable.setTransactionSuccessful();
				cv = null;
			}catch (IllegalStateException e) {}
			finally{
				writable.endTransaction();
				writable.close();
				setChanged();
				notifyObservers("News db updated");
			}	
		}
	}
	
	public ArrayList<News> getAllNews(){
		
		ArrayList<News> v = new ArrayList<News>();
		SQLiteDatabase readable = db.getReadableDatabase();
		if(readable.isOpen()){
			readable.beginTransaction();
			// the vector that will contain the selected notes
			try{
				
				// Equivalent to : SELECT * FROM NOTES ORDER BY NEWS_DATE DESC
				Cursor c = readable.query(NEWS_TABLE, new String[] {"*"}, null, null, null, null, NEWS_DATE + " DESC");
				
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
				readable.setTransactionSuccessful();
			}catch (IllegalStateException e) {}
			finally{
				readable.endTransaction();
				readable.close();
			}	
		}
		return v;
	}
	
	public ArrayList<News> getNewsBySource(ArrayList<String> source){
		
		SQLiteDatabase readable = db.getReadableDatabase();
		ArrayList<News> list = new ArrayList<News>();
		if(readable.isOpen()){
			readable.beginTransaction();
			try{
				
				String sources = "";
				for(int i = 0; i < source.size(); i++){
					sources += NEWS_SOURCE + " LIKE \"" + source.get(i).toString() + "\"";
					if(i < source.size() - 1){
						sources += " OR ";
					}
				}
				
				Cursor c = readable.query(NEWS_TABLE, new String[] {"*"}, sources, null, null, null, NEWS_DATE + " DESC");
				
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
				readable.setTransactionSuccessful();
			}catch (IllegalStateException e) {}
			finally{
				readable.endTransaction();
				readable.close();
			}	
		}		
		return list;
	}
	
	//TODO
	public News getNewsByGUID(String guid){
		
		News n = null;
		
		// set the database to be readable
		SQLiteDatabase readDB  = db.getReadableDatabase();
		if(readDB.isOpen()){
			try{
				readDB.beginTransaction();
				Cursor c = readDB.query(NEWS_TABLE, new String[] {"*"}, NEWS_GUID + " LIKE \"" + guid + "\"", null, null, null, null);
				if(c.moveToNext()){
					n = new News();
					n.setTitle(c.getString(c.getColumnIndex(NEWS_TITLE)));
					n.setPubDate(c.getLong(c.getColumnIndex(NEWS_DATE)));
					n.setDescription(c.getString(c.getColumnIndex(NEWS_DESCRIPTION)));
					n.setGuid(c.getString(c.getColumnIndex(NEWS_GUID)));
					readDB.setTransactionSuccessful();
				}	
				c.close();
			}catch (IllegalStateException e) {}
			finally{
				readDB.endTransaction();
				readDB.close();	
			}	
		}
		return n;
	}
	
	// Usefull to verify that we are not putting duplicate news in the db.
	public ArrayList<String> getAllGUIDFromSource(String source){

		SQLiteDatabase readable = db.getReadableDatabase();
		ArrayList<String> list = new ArrayList<String>();
		if(readable.isOpen()){
			readable.beginTransaction();
			
			try{		
				Cursor c = readable.query(NEWS_TABLE, new String[] {NEWS_GUID},  NEWS_SOURCE + " LIKE \"" + source + "\"", null, null, null, null);
				while(c.moveToNext()){
					list.add(c.getString(c.getColumnIndex(NEWS_GUID)));
				}
				c.close();
				readable.setTransactionSuccessful();
			}catch (IllegalStateException e) {}
			finally{
				readable.endTransaction();
				readable.close();
			}	
		}
		return list;
	}
}
