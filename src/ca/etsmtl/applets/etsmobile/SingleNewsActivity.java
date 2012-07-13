package ca.etsmtl.applets.etsmobile;

import org.apache.http.conn.scheme.Scheme;

import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.tools.db.NewsAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class SingleNewsActivity extends Activity{
	
	private String title, content, date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// On va chercher les param passés par le bundle et on associe
		// les valeurs aux champs respectifs.
		Bundle bundle = getIntent().getExtras();
		
		NewsAdapter newsDB = NewsAdapter.getInstance(this);
		News n = newsDB.getNewsByGUID((String) bundle.getCharSequence("guid"));
		
		if(n != null){
			title = n.getTitle();
			content = n.getDescription();
			date = DateFormat.getDateFormat(this).format(n.getPubDate());
		}
		
	
		content = "<meta name=\"viewport\" content=\"target-densitydpi=device-dpi\" />" +
				content;
		
		WebView webView = new WebView(this);
		webView.getSettings().setSupportZoom(false);
		webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
		setContentView(webView);		
	}
	
}
