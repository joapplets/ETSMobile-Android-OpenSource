package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.graphics.Picture;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class EmailWebView extends Activity {

	private WebView webView;
	private NavBar navBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		webView = (WebView) findViewById(R.id.webView1);

		// load credentials from sharePrefs; No prefs, show login
		final UserCredentials creds = new UserCredentials(
				PreferenceManager.getDefaultSharedPreferences(this));

		final WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setPictureListener(new PictureListener() {

			@Override
			public void onNewPicture(final WebView arg0, final Picture arg1) {
				navBar.hideLoading();
			}
		});
		webView.loadUrl(getString(R.string.url_email_ets));

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				final String user = creds.getUsername();
				final String pwd = creds.getPassword();
				view.loadUrl("javascript:document.getElementById('username').value = '"
						+ user
						+ "';document.getElementById('password').value='"
						+ pwd
						+ "';");
			}
		});
		navBar = (NavBar) findViewById(R.id.navBar2);
		navBar.setTitle("ÉTS Courriel");
		navBar.hideRightButton();
		navBar.showLoading();
		// Android WebView, how to handle redirects in app instead of opening a
		// browser
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				// do your handling codes here, which url is the requested url
				// probably you need to open that url rather than redirect:
				view.loadUrl(url);
				navBar.showLoading();
				return false; // then it is not handled by default action
			}
		});
	}
}
