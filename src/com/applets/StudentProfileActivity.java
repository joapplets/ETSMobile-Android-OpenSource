package com.applets;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.applets.tools.json.IAsyncTaskListener;
import com.applets.tools.json.JSONRetreiver;

public class StudentProfileActivity extends Activity implements
		IAsyncTaskListener {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.student_profile);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		// retreive user credentials
		final String code = preferences.getString("codeAccesUniversel", "NONE");
		final String mot = preferences.getString("motPasse", "NONE");

		// retreive users info
		// TODO correct post in JSONRetreiver
		if (!code.equals("NONE") && !mot.equals("NONE")) {
			new JSONRetreiver(this).execute(getString(R.string.ets_signets),
					JSONRetreiver.requestTypes[0],
					"codeAccesUniversel=" + code, "motPasse=" + mot);
		} else {

			// alert to notify user that his profile is not saved on the phone
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.profile_no_credentials_alert))
					.setPositiveButton("Oui", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							startActivity(new Intent(
									StudentProfileActivity.this,
									AppPreferenceActivity.class));
						}
					}).setNegativeButton("Non", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).create().show();
		}
	}

	@Override
	public void onPostExecute(JSONObject result) {
		try {
			String lastname = result.getString("nom");
			String name = result.getString("prenom");
			String solde = result.getString("soldeTotal");
			
			((TextView) findViewById(R.id.profile_lastname)).setText(lastname);
			((TextView) findViewById(R.id.profile_name)).setText(name);
			((TextView) findViewById(R.id.profile_solde)).setText(solde + "$");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
