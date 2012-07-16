package ca.etsmtl.applets.etsmobile;

import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import ca.etsmtl.applets.etsmobile.providers.NewsListContentProvider;
import ca.etsmtl.applets.etsmobile.services.NewsFetcher;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTable;

public class SingleNewsActivity extends Activity{
	
	private String html, title, content, date, source, link;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMMMMMMMM yyyy");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// On va chercher les param passés par le bundle et on associe
		// les valeurs aux champs respectifs.
		Bundle bundle = getIntent().getExtras();
		
		int id = bundle.getInt("id");
		
		String[] projection = {NewsTable.NEWS_DATE, NewsTable.NEWS_TITLE, NewsTable.NEWS_DESCRIPTION, NewsTable.NEWS_SOURCE, NewsTable.NEWS_LINK};
		Cursor c = managedQuery(Uri.withAppendedPath(NewsListContentProvider.CONTENT_URI, String.valueOf(id)), projection, null, null, null);
		if(c.moveToFirst()){
			date = dateFormat.format(c.getLong(c.getColumnIndex(NewsTable.NEWS_DATE)));
			title = c.getString(c.getColumnIndex(NewsTable.NEWS_TITLE));
			content = c.getString(c.getColumnIndex(NewsTable.NEWS_DESCRIPTION));
			source = c.getString(c.getColumnIndex(NewsTable.NEWS_SOURCE));
			link = c.getString(c.getColumnIndex(NewsTable.NEWS_LINK));
		}
	
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		progressDialog.setTitle("ÉTS Mobile");
		progressDialog.setMessage("Veuillez patienter...");
		
		WebView webView = new WebView(this);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		WebSettings settings = webView.getSettings();
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int progress) {
		        progressDialog.show();
		        progressDialog.setProgress(0);
		        setProgress(progress * 1000);

		        progressDialog.incrementProgressBy(progress);

		        if(progress == 100 && progressDialog.isShowing()){
		        	progressDialog.dismiss();	
		        }
			}
		});
		
		// Si on enable js on, par précaution on disable l'accès au FS
		// sauf pour le dossier assets et res
		settings.setJavaScriptEnabled(true);
		settings.setAllowFileAccess(false);
		settings.setSupportZoom(false);
		settings.setDomStorageEnabled(true);
		
		if(source.equals(NewsFetcher.FACEBOOK) || source.equals(NewsFetcher.TWITTER)){
			webView.setWebViewClient(new WebViewClient(){
				
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					return super.shouldOverrideUrlLoading(view, url);
				}
			});
			webView.loadUrl(link);
		}
		if(source.equals(NewsFetcher.RSS_ETS)){
			Document doc = Jsoup.parse(content);
			doc.head().append("<meta name=\"viewport\" content=\"width=device-width; target-densityDpi=device-dpi\">");
			doc.head().append("<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/rssets.css\">");
			html = doc.html();
			html = "<h2>" + title + "</h2><br>" + date +"<hr width=\"98%\" size=\"2\" align=\"center\"><br>" + html; 
			webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
		}
		setContentView(webView);
	} 
	
}
