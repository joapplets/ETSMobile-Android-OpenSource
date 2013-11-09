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
package ca.etsmtl.applets.etsmobile.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;
import ca.etsmtl.applets.etsmobile.providers.ETSMobileContentProvider;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLBottinParser;

public class BottinService extends Service implements Observer {

	public class BottinBinder extends Binder {
		public boolean isWorking() {
			return working;
		}

		public void startFetching() {
			if (!working) {
				new Fetcher().execute();
			}
		}
	}

	public class Fetcher extends AsyncTask<Void, Void, Void> {

		private static final String TAG = "BottinFetcherFetcher";

		@Override
		protected Void doInBackground(final Void... arg0) {
			try {

				final String request = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
						+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+ "<soap:Body>"
						+ "<Recherche xmlns=\"http://etsmtl.ca/\">"
						+ "<FiltreNom></FiltreNom>"
						+ "<FiltrePrenom></FiltrePrenom>"
						+ "<FiltreServiceCode></FiltreServiceCode>"
						+ "</Recherche>" + "</soap:Body>" + "</soap:Envelope>";
				final URL url = new URL(
						"http://etsmtl.ca/cmspages/webservice.asmx?op=Recherche");
				final HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Host", "etsmtl.ca");
				conn.setRequestProperty("Content-Type",
						"text/xml; charset=utf-8");
				conn.setRequestProperty("SOAPAction",
						"\"http://etsmtl.ca/Recherche\"");

				final OutputStreamWriter writer = new OutputStreamWriter(
						conn.getOutputStream());

				writer.write(request);
				writer.flush();

				final InputStream stream = conn.getInputStream();
				if (stream != null) {
					final SAXParser saxParser = SAXParserFactory.newInstance()
							.newSAXParser();
					final XMLBottinParser parser = new XMLBottinParser(bundle);
					saxParser.parse(stream, parser);
					stream.close();
				}

			} catch (final MalformedURLException e) {
				Log.e(Fetcher.TAG, e.toString());
			} catch (final IOException e) {
				Log.e(Fetcher.TAG, e.toString());
			} catch (final ParserConfigurationException e) {
				Log.e(Fetcher.TAG, e.toString());
			} catch (final SAXException e) {
				Log.e(Fetcher.TAG, e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(final Void result) {
			working = false;
		}

		@Override
		protected void onPreExecute() {
			working = true;
		}
	}

	// private ContentValues[] values = new ContentValues[500];
	private ObservableBundle bundle;
	private boolean working;
	private final IBinder binder = new BottinBinder();

	// private int i = 0;

	@Override
	public IBinder onBind(final Intent arg0) {
		return binder;
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags,
			final int startId) {
		bundle = new ObservableBundle();
		bundle.addObserver(this);
		if (!working) {
			new Fetcher().execute();
		}
		return Service.START_NOT_STICKY;
	}

	@Override
	public void update(final Observable observable, final Object object) {
		if (object instanceof ContentValues[]) {
			getContentResolver().bulkInsert(
					ETSMobileContentProvider.CONTENT_URI_BOTTIN,
					(ContentValues[]) object);
			System.currentTimeMillis();
		}
	}

}
