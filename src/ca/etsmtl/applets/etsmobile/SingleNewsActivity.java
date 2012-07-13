package ca.etsmtl.applets.etsmobile;

import ca.etsmtl.applets.etsmobile.providers.NewsListContentProvider;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTable;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.webkit.WebView;

public class SingleNewsActivity extends Activity{
	
	private String title, content, date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// On va chercher les param passés par le bundle et on associe
		// les valeurs aux champs respectifs.
		Bundle bundle = getIntent().getExtras();
		
		int id = bundle.getInt("id");
		
		String[] projection = {NewsTable.NEWS_DATE, NewsTable.NEWS_TITLE, NewsTable.NEWS_DESCRIPTION};
		Cursor c = managedQuery(Uri.withAppendedPath(NewsListContentProvider.CONTENT_URI, String.valueOf(id)), projection, null, null, null);
		if(c.moveToFirst()){
			date = DateFormat.getDateFormat(this).format(c.getLong(c.getColumnIndex(NewsTable.NEWS_DATE)));
			title = c.getString(c.getColumnIndex(NewsTable.NEWS_TITLE));
			content = c.getString(c.getColumnIndex(NewsTable.NEWS_DESCRIPTION));
		}
	
		content = "<meta name=\"viewport\" content=\"target-densitydpi=device-dpi\" />" + title + "<br>"  + date + "<br>" + content;
		
		WebView webView = new WebView(this);
		webView.getSettings().setSupportZoom(false);
		webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
		setContentView(webView);		
	}
	
}
