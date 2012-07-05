package ca.etsmtl.applets.etsmobile.tools.db;

import java.util.ArrayList;

import ca.etsmtl.applets.etsmobile.models.Programme;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProgrammesAdapter{

	private final String PROGRAMMES_TABLE = SQLDBHelper.PROGRAMMES_TABLE;
	private final String PROGRAMMES_DESCRIPTION = SQLDBHelper.PROGRAMMES_DESCRIPTION;
	private final String PROGRAMMES_NAME = SQLDBHelper.PROGRAMMES_NAME;
	private final String PROGRAMMES_PROGRAMME_ID = SQLDBHelper.PROGRAMMES_PROGRAMME_ID;
	private final String PROGRAMMES_SHORT_NAME = SQLDBHelper.PROGRAMMES_SHORT_NAME;
	private final String PROGRAMMES_URL = SQLDBHelper.PROGRAMMES_URL;
	private final String PROGRAMMES_URL_PDF = SQLDBHelper.PROGRAMMES_URL_PDF;
	
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = SQLDBHelper.DB_NAME;
	
	private static ProgrammesAdapter instance = null;
	private static SQLDBHelper db = null;
	
	private ProgrammesAdapter(){}
	
	public static ProgrammesAdapter getInstance(Context c){
		
		// if instance is null, which means the we are opening the app
		if(instance == null){
			
			// create one instance
			instance = new ProgrammesAdapter();
			
			// create the db, if it's already in the filesystem it will not recreate it
			db = new SQLDBHelper(c , DB_NAME, null, DB_VERSION);
			db.close();
		}
		
		// return the instance.
		return instance;
	}

	public ArrayList<Programme> getAllNews(){
		
		// the vector that will contain the selected notes
		ArrayList<Programme> v = new ArrayList<Programme>();
		
		// set the database to be readable
		SQLiteDatabase readDB  = db.getReadableDatabase();
		
		// Equivalent to : SELECT * FROM NOTES ORDER BY NEWS_DATE DESC
		Cursor c = readDB.query(PROGRAMMES_TABLE, new String[] {"*"}, null, null, null, null, PROGRAMMES_PROGRAMME_ID + " DESC");
		
		Programme programme;
		while(c.moveToNext()){
			
			programme = new Programme();
			
			programme.setDescription(c.getString(c.getColumnIndex(PROGRAMMES_DESCRIPTION)));
			programme.setNom(c.getString(c.getColumnIndex(PROGRAMMES_NAME)));
			programme.setID(c.getString(c.getColumnIndex(PROGRAMMES_PROGRAMME_ID)));
			programme.setNomCourt(c.getString(c.getColumnIndex(PROGRAMMES_SHORT_NAME)));
			programme.setURL(c.getString(c.getColumnIndex(PROGRAMMES_URL)));
			programme.setURLtoPDF(c.getString(c.getColumnIndex(PROGRAMMES_URL_PDF)));
			
			v.add(programme);
		}
		
		c.close();
		readDB.close();
		
		return v;
	}
}
