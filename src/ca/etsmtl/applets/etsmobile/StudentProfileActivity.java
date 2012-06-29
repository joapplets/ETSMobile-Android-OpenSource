package ca.etsmtl.applets.etsmobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLAppletsHandler;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLParser;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLUserProfileParser;

public class StudentProfileActivity extends Activity {

	private class UserProfileLoader extends AsyncTask<Void, Void, InputStream> {

		OutputStreamWriter writer;

		@Override
		protected InputStream doInBackground(final Void... params) {

			try {
				final String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
						+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+ "<soap:Body>"
						+ "<infoEtudiant xmlns=\"http://etsmtl.ca/\">"
						+ "<codeAccesUniversel>aj73330</codeAccesUniversel>"
						+ "<motPasse>76624</motPasse>"
						+ "</infoEtudiant>"
						+ "</soap:Body>" + "</soap:Envelope>";
				// Send the request
				// URL url = new
				// URL("https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx");

				final URL url = new URL(
						"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx");
				final HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Host", "signets-ens.etsmtl.ca");
				conn.setRequestProperty("Content-Type",
						"text/xml; charset=utf-8");
				conn.setRequestProperty("SOAPAction",
						"\"http://etsmtl.ca/infoEtudiant\"");

				writer = new OutputStreamWriter(conn.getOutputStream());

				// write parameters
				writer.write(data);
				writer.flush();

				return conn.getInputStream();
			} catch (final MalformedURLException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(final InputStream stream) {
			if (stream != null) {
				final XMLAppletsHandler handler = new XMLUserProfileParser(
						StudentProfileActivity.this);
				final XMLParser xml = new XMLParser(stream, handler,
						StudentProfileActivity.this);
				final StudentProfile profile = xml.getParsedStudentProfile();
				System.out.println(profile.getNomComplet());
			}
		}

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_profile);
		new UserProfileLoader().execute();

		/**
		 * No way qu'on save les credentials dans le cell...
		 */

		// // retreive user credentials
		// final String code = preferences.getString("codeAccesUniversel",
		// "NONE");
		// final String mot = preferences.getString("motPasse", "NONE");
		//
		// // retreive users info
		// // TODO correct post in JSONRetreiver
		// if (!code.equals("NONE") && !mot.equals("NONE")) {
		// new JSONRetreiver(this).execute(getString(R.string.ets_signets),
		// JSONRetreiver.requestTypes[0],
		// "codeAccesUniversel=" + code, "motPasse=" + mot);
		// } else {
		//
		// // alert to notify user that his profile is not saved on the phone
		// Builder builder = new AlertDialog.Builder(this);
		// builder.setMessage(getString(R.string.profile_no_credentials_alert))
		// .setPositiveButton("Oui", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.cancel();
		// startActivity(new Intent(
		// StudentProfileActivity.this,
		// AppPreferenceActivity.class));
		// }
		// }).setNegativeButton("Non", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.cancel();
		// }
		// }).create().show();
		// }
	}
}
