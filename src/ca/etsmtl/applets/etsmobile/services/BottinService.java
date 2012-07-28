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
		public void startFetching() {
			if (!working) {
				new Fetcher().execute();
			}
		}

		public boolean isWorking() {
			return working;
		}
	}

	public class Fetcher extends AsyncTask<Void, Void, Void> {

		private static final String TAG = "BottinFetcherFetcher";

		@Override
		protected Void doInBackground(Void... arg0) {
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

				OutputStreamWriter writer = new OutputStreamWriter(
						conn.getOutputStream());

				writer.write(request);
				writer.flush();

				final InputStream stream = conn.getInputStream();
				if (stream != null) {
					SAXParser saxParser = SAXParserFactory.newInstance()
							.newSAXParser();
					XMLBottinParser parser = new XMLBottinParser(bundle);
					saxParser.parse(stream, parser);
					stream.close();
				}

			} catch (final MalformedURLException e) {
				Log.e(TAG, e.toString());
			} catch (final IOException e) {
				Log.e(TAG, e.toString());
			} catch (ParserConfigurationException e) {
				Log.e(TAG, e.toString());
			} catch (SAXException e) {
				Log.e(TAG, e.toString());
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			working = true;
		}

		@Override
		protected void onPostExecute(Void result) {
			working = false;
		}
	}

	private ContentValues[] values = new ContentValues[500];
	private ObservableBundle bundle;
	private boolean working;
	private IBinder binder = new BottinBinder();
	private int i = 0;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		bundle = new ObservableBundle();
		bundle.addObserver(this);
		if (!working) {
			new Fetcher().execute();
		}
		return START_NOT_STICKY;
	}

	@Override
	public void update(Observable observable, Object object) {
		if (object instanceof ContentValues) {
			// BottinEntry n = (BottinEntry) object;
			values[i] = (ContentValues) object;
			if (i++ >= 499) {
				long start = System.currentTimeMillis();
				Log.d("XMLBottinParser", "inserting 500 : " + start + "ms");
				getContentResolver().bulkInsert(
						ETSMobileContentProvider.CONTENT_URI, values);
				long stop = System.currentTimeMillis();
				Log.d("XMLBottinParser", "end insert : " + (stop - start)
						+ "ms");
				values = null;
				values = new ContentValues[500];
				i = 0;
			}
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

}
