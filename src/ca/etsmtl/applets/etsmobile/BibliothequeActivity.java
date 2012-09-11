package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.graphics.Picture;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;

import com.etsmt.applets.etsmobile.views.NavBar;

public class BibliothequeActivity extends Activity {

	private WebView webView;
	private NavBar navBar;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		webView = (WebView) findViewById(R.id.webView1);

		final WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setPictureListener(new PictureListener() {

			@Override
			public void onNewPicture(final WebView arg0, final Picture arg1) {
				navBar.hideLoading();
			}
		});
		webView.loadUrl(getString(R.string.url_biblio));

		navBar = (NavBar) findViewById(R.id.navBar2);
		navBar.setTitle(R.drawable.navbar_biblio_title);
		navBar.hideRightButton();
		navBar.showLoading();
	}
}
