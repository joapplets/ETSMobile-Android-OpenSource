/*******************************************************************************
 * Copyright 2013 Club ApplETS
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ca.etsmtl.applets.etsmobile;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import ca.etsmtl.applets.etsmobile.providers.ETSMobileContentProvider;
import ca.etsmtl.applets.etsmobile.services.NewsService;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;

public class SingleNewsActivity extends Activity {

	private WebView webView;
	private FrameLayout webViewPlaceholder;
	private String html, title, content, date, source, link;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd MMMMMMMMMM yyyy", Locale.CANADA_FRENCH);
	private static ProgressDialog progressDialog;

	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			SingleNewsActivity.progressDialog.dismiss();
		};
	};

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {

		// On va chercher les param passés par le bundle et on associe
		// les valeurs aux champs respectifs.
		final Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			final int id = bundle.getInt("id");
			System.out.println("The requested id is : " + id);
			final String[] projection = { NewsTableHelper.NEWS_DATE,
					NewsTableHelper.NEWS_TITLE,
					NewsTableHelper.NEWS_DESCRIPTION,
					NewsTableHelper.NEWS_SOURCE, NewsTableHelper.NEWS_LINK };
			final Cursor c = managedQuery(Uri.withAppendedPath(
					ETSMobileContentProvider.CONTENT_URI_NEWS,
					String.valueOf(id)), projection, null, null, null);
			if (c.moveToFirst()) {
				date = dateFormat.format(c.getLong(c
						.getColumnIndex(NewsTableHelper.NEWS_DATE)));
				title = c.getString(c
						.getColumnIndex(NewsTableHelper.NEWS_TITLE));
				content = c.getString(c
						.getColumnIndex(NewsTableHelper.NEWS_DESCRIPTION));
				source = c.getString(c
						.getColumnIndex(NewsTableHelper.NEWS_SOURCE));
				link = c.getString(c.getColumnIndex(NewsTableHelper.NEWS_LINK));
			}
			webViewPlaceholder = (FrameLayout) findViewById(R.id.webViewPlaceholder);
			if (webView == null) {
				webView = new WebView(this);
				webView.setLayoutParams(new ViewGroup.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
				final WebSettings settings = webView.getSettings();
				webView.setWebChromeClient(new WebChromeClient() {
					@Override
					public void onProgressChanged(final WebView view,
							final int progress) {
						if (SingleNewsActivity.progressDialog != null
								&& progress == 100
								&& SingleNewsActivity.progressDialog
										.isShowing()) {
							final Message msg = new Message();
							msg.setTarget(SingleNewsActivity.handler);
							msg.sendToTarget();
						}
					}
				});

				// Si on enable js on, par précaution on disable l'accès au FS
				// sauf pour le dossier assets et res
				settings.setJavaScriptEnabled(true);
				settings.setAllowFileAccess(false);
				settings.setSupportZoom(false);
				settings.setDomStorageEnabled(true);

				if (source.equals(NewsService.FACEBOOK)
						|| source.equals(NewsService.TWITTER)
						|| source.equals(NewsService.INTERFACE)) {
					webView.setWebViewClient(new WebViewClient() {

						@Override
						public boolean shouldOverrideUrlLoading(
								final WebView view, final String url) {
							return super.shouldOverrideUrlLoading(view, url);
						}
					});
					webView.loadUrl(link);
				}
				if (source.equals(NewsService.RSS_ETS)) {
					final Document doc = Jsoup.parse(content);
					doc.head()
							.append("<meta name=\"viewport\" content=\"width=device-width, target-densityDpi=device-dpi\">");
					doc.head()
							.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/rssets.css\">");
					html = doc.html();
					html = "<h2>"
							+ title
							+ "</h2><br>"
							+ date
							+ "<hr width=\"98%\" size=\"2\" align=\"center\"><br>"
							+ html;
					webView.loadDataWithBaseURL(null, html, "text/html",
							"UTF-8", null);
				}
			}
			webViewPlaceholder.addView(webView);
		}else{
			finish();
		}
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.news_view);
		init();

		SingleNewsActivity.progressDialog = new ProgressDialog(
				SingleNewsActivity.this);
		SingleNewsActivity.progressDialog
				.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		SingleNewsActivity.progressDialog.setCancelable(false);
		SingleNewsActivity.progressDialog.setTitle("�TS Mobile");
		SingleNewsActivity.progressDialog.setMessage("En cours de chargement");
		SingleNewsActivity.progressDialog.show();
	}

	@Override
	protected void onRestoreInstanceState(final Bundle savedInstanceState) {
		if (webView != null) {
			webViewPlaceholder.removeView(webView);
		}
		super.onRestoreInstanceState(savedInstanceState);
		setContentView(R.layout.news_view);
		init();
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		webView.saveState(outState);
		super.onSaveInstanceState(outState);
	}

}
