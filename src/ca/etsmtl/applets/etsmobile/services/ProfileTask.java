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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLBottinParser;

public class ProfileTask extends AsyncTask<String, String, StudentProfile> {

	private static final String TAG = "ProfileTask";
	public static final String PROFILE_KEY = "profile";
	private int MESSAGE_ID = 0;
	private ObservableBundle bundle;
	private final Handler handler;
	private Message msg;

	public ProfileTask(Handler handler, int msgId) {
		this.handler = handler;
		this.MESSAGE_ID = msgId;
	}

	@Override
	protected StudentProfile doInBackground(String... params) {
		try {
			final String request = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<soap:Body>"
					+ "<infoEtudiant xmlns=\"http://etsmtl.ca/\">"
					+ "<codeAccesUniversel></codeAccesUniversel>"
					+ "<motPasse></motPasse>" + "</infoEtudiant>"
					+ "</soap:Body>" + "</soap:Envelope>";
			final URL url = new URL(
					"http://etsmtl.ca/cmspages/webservice.asmx?op=infoEtudiant");
			final HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Host", "etsmtl.ca");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
//			conn.setRequestProperty("SOAPAction", "");

			OutputStreamWriter writer = new OutputStreamWriter(
					conn.getOutputStream());

			writer.write("{'codeAccesUniversel': 'aj39950', 'motPasse': '23875'}");
			writer.flush();

			final InputStream stream = conn.getInputStream();
			//TODO parse response
//			if (stream != null) {
//				SAXParser saxParser = SAXParserFactory.newInstance()
//						.newSAXParser();
//				XMLBottinParser parser = new XMLBottinParser(bundle);
//				saxParser.parse(stream, parser);
//				stream.close();
//
//				// insert all, with transaction
//				// bottinDB.insertAll(newList);
//
//			}

		} catch (final MalformedURLException e) {
			Log.e(TAG, e.toString());
		} catch (final IOException e) {
			Log.e(TAG, e.toString());
		} 
		// catch (ParserConfigurationException e) {
		// Log.e(TAG, e.toString());
		// } catch (SAXException e) {
		// Log.e(TAG, e.toString());
		// }
		return null;
	}

	@Override
	protected void onPostExecute(StudentProfile result) {
		super.onPostExecute(result);
		Bundle data = new Bundle();
		data.putParcelable(PROFILE_KEY, result);

		msg.setData(data);
		msg.sendToTarget();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		msg = handler.obtainMessage(MESSAGE_ID);
	}
}