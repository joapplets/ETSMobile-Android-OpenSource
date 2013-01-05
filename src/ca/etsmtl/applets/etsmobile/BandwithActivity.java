package ca.etsmtl.applets.etsmobile;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import ca.etsmtl.applets.etsmobile.dialogs.BandwithDialog;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;

public class BandwithActivity extends Activity {

	private String appt;
	private String rez;

	private void getBandwith() {
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {

				String ent = null;
				try {

					final StringBuilder sb = new StringBuilder();
					sb.append("http://etsmtl.me/py/usage/");
					sb.append(params[0]);
					sb.append(params[1]);

					HttpGet get = new HttpGet(URI.create(sb.toString()));
					HttpClient client = new DefaultHttpClient();
					HttpResponse re = client.execute(get);
					ent = re.getEntity().toString();

					// array = objectList;
				} catch (final IOException e) {
					e.printStackTrace();
				}

				return ent;
			}

			@Override
			protected void onPostExecute(String result) {
				Bundle args = new Bundle();
				args.putString("result", result);
				showDialog(0, args);
				super.onPostExecute(result);
			}

		}.execute(rez, appt);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		appt = prefs.getString(UserCredentials.APPT, "");
		rez = prefs.getString(UserCredentials.REZ, "");

		if (appt.equals("") || rez.equals("")) {
			showDialog(1);
		} else {
			getBandwith();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		Dialog d = null;
		switch (id) {
		case 1:
			d = new BandwithDialog(this);
			break;

		default:
			String result = args.getString("result");
			d = new AlertDialog.Builder(this).setMessage(
					"Phase: " + rez + "Appt: " + appt + " il vous reste :"
							+ result).create();
			break;
		}

		return d;
	}
}
