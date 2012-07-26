package ca.etsmtl.applets.etsmobile.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Log;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLProfileParser;

public class ProfileTask extends AsyncTask<String, String, Void> {

	private static final String TAG = "ProfileTask";
	public static final String PROFILE_KEY = "profile";
	private ObservableBundle bundle;

	public ProfileTask(ObservableBundle bundle) {
		this.bundle = bundle;
	}

	@Override
	protected Void doInBackground(String... params) {
		try {

			final String request = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<soap:Body>" + "<infoEtudiant xmlns=\"http://etsmtl.ca/\">"
					+ "<codeAccesUniversel>" + params[0]
					+ "</codeAccesUniversel>" + "<motPasse>" + params[1]
					+ "</motPasse>" + "</infoEtudiant>" + "</soap:Body>"
					+ "</soap:Envelope>";
			final URL url = new URL(
					"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx?op=infoEtudiant");
			final HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

			OutputStreamWriter writer = new OutputStreamWriter(
					conn.getOutputStream());

			writer.write(request);
			writer.flush();

			final InputStream stream = conn.getInputStream();
			if (stream != null) {
				SAXParser saxParser = SAXParserFactory.newInstance()
						.newSAXParser();
				XMLProfileParser parser = new XMLProfileParser(bundle);
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

}