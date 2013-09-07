package ca.etsmtl.applets.etsmobile;

import java.util.Dictionary;
import java.util.Hashtable;

import com.testflightapp.lib.TestFlight;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import ca.etsmtl.applets.etsmobile.adapters.MyCourseDetailAdapter;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.models.CourseEvaluation;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class MyCourseDetailActivity extends ListActivity {

	private CourseEvaluation courseEvaluation;
	private NavBar navBar;
	private String groupe;
	private String coteFinale;
	private String sigle;
	private String session;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_courses_view);
		TestFlight.passCheckpoint(this.getClass().getName());
		// Initialize courseEvaluation data from cache
		if (savedInstanceState != null) {
			courseEvaluation = (CourseEvaluation) savedInstanceState
					.getSerializable("courseEvaluation");
		}
		// navBar
		navBar = (NavBar) findViewById(R.id.navBar3);
		// get data from parent activity
		session = getIntent().getExtras().getString("session");
		sigle = getIntent().getExtras().getString("sigle");
		coteFinale = getIntent().getExtras().getString("cote");
		groupe = getIntent().getExtras().getString("groupe");
		// init navBar
		navBar.setRightButtonText("Site web");
		navBar.showRightButton();
		navBar.setTitle(sigle + "-" + groupe);
		navBar.setRightButtonAction(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://cours.etsmtl.ca/" + sigle)));
			}
		});
		if (courseEvaluation == null) {
			final UserCredentials creds = new UserCredentials(
					PreferenceManager.getDefaultSharedPreferences(this));

			if (creds.isLoggedIn()) {
				final Dictionary<String, String> requestParams = new Hashtable<String, String>();
				requestParams.put("codeAccesUniversel", creds.getUsername());
				requestParams.put("motPasse", creds.getPassword());
				requestParams.put("pSigle", sigle);
				requestParams.put("pGroupe", groupe);
				requestParams.put("pSession", session);

				// async task; get course details
				final SignetBackgroundThread<CourseEvaluation, CourseEvaluation> signetBackgroundThead = new SignetBackgroundThread<CourseEvaluation, CourseEvaluation>(
						"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
						"listeElementsEvaluation", requestParams,
						CourseEvaluation.class);

				navBar.showLoading();
				new Thread(new Runnable() {

					@Override
					public void run() {
						courseEvaluation = signetBackgroundThead.fetchObject();
						if (courseEvaluation != null) {
							courseEvaluation.setCote(coteFinale);
							// all ui changes must be executed on uiThread
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									final MyCourseDetailAdapter myCoursesAdapter = new MyCourseDetailAdapter(
											getApplicationContext(),
											courseEvaluation);
									getListView().setAdapter(myCoursesAdapter);
									getListView().setEmptyView(
											findViewById(R.id.empty));

									navBar.hideLoading();
								}
							});
						}
					}
				}).start();
			} else {
				Toast.makeText(this,
						getString(R.string.usernamePasswordRequired),
						Toast.LENGTH_LONG).show();
			}
		} else {
			final MyCourseDetailAdapter myCoursesAdapter = new MyCourseDetailAdapter(
					getApplicationContext(), courseEvaluation);
			getListView().setAdapter(myCoursesAdapter);
			getListView().setEmptyView(findViewById(R.id.empty));
		}

	}

	@Override
	public void onSaveInstanceState(final Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState
				.putSerializable("courseEvaluation", courseEvaluation);
	}
}