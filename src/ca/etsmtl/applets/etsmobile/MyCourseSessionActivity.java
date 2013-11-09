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

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import ca.etsmtl.applets.etsmobile.adapters.MyCourseSessionAdapter;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.views.NavBar;

import com.testflightapp.lib.TestFlight;

public class MyCourseSessionActivity extends ListActivity {

	private ArrayList<Session> sessions;
	private MyCourseSessionAdapter myCoursesAdapter;
	private NavBar navBar;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.my_courses_view);
		TestFlight.passCheckpoint(this.getClass().getName());
		navBar = (NavBar) findViewById(R.id.navBar3);
		navBar.hideRightButton();
		navBar.setTitle(R.drawable.navbar_notes_title);
		navBar.setHomeAction(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				finish();
			}
		});

		final UserCredentials creds = new UserCredentials(
				PreferenceManager.getDefaultSharedPreferences(this));

		if (savedInstanceState != null) {
			sessions = (ArrayList<Session>) savedInstanceState
					.getSerializable("sessions");
		}

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> adapterView,
					final View view, final int position, final long arg3) {
				final Bundle b = new Bundle();
				b.putString("session", myCoursesAdapter.getItem(position)
						.getShortName());
				b.putString("session_long", myCoursesAdapter.getItem(position)
						.getLongName());
				final Intent nextActivity = new Intent(view.getContext(),
						MyCourseListActivity.class);
				nextActivity.putExtras(b);
				startActivity(nextActivity);
			}
		});

		if (creds.getPassword() != null && creds.getUsername() != null
				&& !"".equals(creds.getPassword())
				&& !"".equals(creds.getUsername())) {

			if (sessions == null) {
				final SignetBackgroundThread<ArrayList<Session>, Session> signetBackgroundThead = new SignetBackgroundThread<ArrayList<Session>, Session>(
						"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
						"listeSessions", creds, Session.class);

				navBar.showLoading();

				new Thread(new Runnable() {

					@Override
					public void run() {

						final ArrayList<Session> newSessions = signetBackgroundThead
								.fetchArray();

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								sessions = newSessions;
								myCoursesAdapter = new MyCourseSessionAdapter(
										getApplicationContext(),
										R.layout.session_list_item, sessions);
								getListView().setAdapter(myCoursesAdapter);
								// if (progress != null) {
								// progress.dismiss();
								// }
								navBar.hideLoading();
							}
						});
					}
				}).start();
			} else {
				myCoursesAdapter = new MyCourseSessionAdapter(
						getApplicationContext(), R.layout.session_list_item,
						sessions);
				getListView().setAdapter(myCoursesAdapter);
			}

		} else {
			Toast.makeText(this, getString(R.string.usernamePasswordRequired),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onSaveInstanceState(final Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putSerializable("sessions", sessions);
	}
}
