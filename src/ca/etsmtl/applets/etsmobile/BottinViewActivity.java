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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.providers.ETSMobileContentProvider;
import ca.etsmtl.applets.etsmobile.tools.db.BottinTableHelper;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class BottinViewActivity extends Activity {

	private TextView nomView;
	private TextView prenomView;
	private TextView serviceView;
	private TextView emplView;
	private TextView titreView;
	private TextView courrielView;
	private TextView phoneView;
	private NavBar navBar;
	private String prenom;
	private String service;
	private String empl;
	private String titre;
	private String courriel;
	private String phone;
	private String nom;
	private Button addContactBtn;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottin_view);

		final Long b = (Long) getIntent().getExtras().get("id");
		// long id = b.getLong("id");

		nomView = (TextView) findViewById(R.id.bottin_view_nom);
		prenomView = (TextView) findViewById(R.id.bottin_view_prenom);
		serviceView = (TextView) findViewById(R.id.bottin_view_service);
		emplView = (TextView) findViewById(R.id.bottin_view_empl);
		titreView = (TextView) findViewById(R.id.bottin_view_titre);
		courrielView = (TextView) findViewById(R.id.bottin_view_courriel);
		phoneView = (TextView) findViewById(R.id.bottin_view_phone);

		addContactBtn = (Button) findViewById(R.id.bottin_view_add_contact);
		final Cursor cursor = managedQuery(Uri.withAppendedPath(
				ETSMobileContentProvider.CONTENT_URI_BOTTIN, b.toString()),
				BottinTableHelper.AVAILABLE, null, null, null);
		if (cursor.getCount() > 0 && cursor.moveToFirst()) {
			nom = cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_NOM));
			nomView.setText(nom);
			prenomView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_PRENOM)));
			serviceView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_SERVICE)));
			emplView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_EMPLACEMENT)));
			titreView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_TIRE)));
			courrielView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_COURRIEL)));
			phoneView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_TELBUREAU)));

			prenom = cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_PRENOM));
			prenomView.setText(prenom);

			service = cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_SERVICE));
			serviceView.setText(service);

			empl = cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_EMPLACEMENT));
			emplView.setText(empl);

			titre = cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_TIRE));
			titreView.setText(titre);

			courriel = cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_COURRIEL));
			courrielView.setText(courriel);

			phone = cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_TELBUREAU));
			phoneView.setText(phone);
			addContactBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					launchContactAdder();
				}
			});
		} else {
			addContactBtn.setVisibility(View.INVISIBLE);
		}

		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.setTitle(R.drawable.navbar_phonebook_title);
		navBar.hideRightButton();
		navBar.hideLoading();
	}

	/**
	 * Launches the ContactAdder activity to add a new contact to the selected
	 * accont.
	 */
	protected void launchContactAdder() {
		final Intent i = new Intent(this, ContactAdder.class);
		final Bundle extras = new Bundle();
		extras.putString("nom", nom);
		extras.putString("preNom", prenom);
		extras.putString("service", service);
		extras.putString("empl", empl);
		extras.putString("titre", titre);
		extras.putString("courriel", courriel);
		extras.putString("phone", phone);
		i.putExtras(extras);
		startActivity(i);
	}
}
