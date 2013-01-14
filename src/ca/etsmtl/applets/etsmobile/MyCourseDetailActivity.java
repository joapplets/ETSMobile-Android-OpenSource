package ca.etsmtl.applets.etsmobile;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

import android.app.ListActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import ca.etsmtl.applets.etsmobile.adapters.MyCourseDetailAdapter;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.models.CourseEvaluation;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class MyCourseDetailActivity extends ListActivity {

    private CourseEvaluation courseEvaluation;
    private NavBar navBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.my_courses_view);

	navBar = (NavBar) findViewById(R.id.navBar3);
	navBar.hideRightButton();
	navBar.setTitle(R.drawable.navbar_notes_title);
	navBar.setHomeAction(new OnClickListener() {

	    @Override
	    public void onClick(final View v) {
		finish();
	    }
	});

	if (savedInstanceState != null) {
	    courseEvaluation = (CourseEvaluation) savedInstanceState
		    .getSerializable("courseEvaluation");
	}

	if (courseEvaluation == null) {
	    final UserCredentials creds = new UserCredentials(
		    PreferenceManager.getDefaultSharedPreferences(this));

	    final String session = getIntent().getExtras().getString("session");
	    final String sigle = getIntent().getExtras().getString("sigle");
	    final String coteFinale = getIntent().getExtras().getString("cote");
	    final String groupe = getIntent().getExtras().getString("groupe");

	    if (creds.getPassword() != null && creds.getUsername() != null
		    && !"".equals(creds.getPassword()) && !"".equals(creds.getUsername())) {
		final Dictionary<String, String> requestParams = new Hashtable<String, String>();
		requestParams.put("codeAccesUniversel", creds.getUsername());
		requestParams.put("motPasse", creds.getPassword());
		requestParams.put("pSigle", sigle);
		requestParams.put("pGroupe", groupe);
		requestParams.put("pSession", session);

		final SignetBackgroundThread<CourseEvaluation, CourseEvaluation> signetBackgroundThead = new SignetBackgroundThread<CourseEvaluation, CourseEvaluation>(
			"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
			"listeElementsEvaluation", requestParams, CourseEvaluation.class,
			FetchType.OBJECT);

		signetBackgroundThead.execute();

		// final ProgressDialog progress = new ProgressDialog(this);
		// progress.setMessage(getString(R.string.loading));
		// progress.show();

		navBar.showLoading();
		new Thread(new Runnable() {

		    @Override
		    public void run() {
			try {
			    courseEvaluation = signetBackgroundThead.get();
			    courseEvaluation.setCote(coteFinale);

			    runOnUiThread(new Runnable() {
				@Override
				public void run() {
				    final MyCourseDetailAdapter myCoursesAdapter = new MyCourseDetailAdapter(
					    getApplicationContext(), R.layout.list_item_value,
					    courseEvaluation.getEvaluationElements(),
					    courseEvaluation);
				    getListView().setAdapter(myCoursesAdapter);
				    getListView().setEmptyView(findViewById(R.id.empty));

				    // if (progress != null) {
				    // progress.dismiss();
				    // }
				    navBar.hideLoading();
				}
			    });

			} catch (final InterruptedException e) {
			    e.printStackTrace();
			} catch (final ExecutionException e) {
			    e.printStackTrace();
			}
		    }
		}).start();
	    } else {
		Toast.makeText(this, getString(R.string.usernamePasswordRequired),
			Toast.LENGTH_LONG).show();
	    }
	} else {
	    final MyCourseDetailAdapter myCoursesAdapter = new MyCourseDetailAdapter(
		    getApplicationContext(), R.layout.list_item_value,
		    courseEvaluation.getEvaluationElements(), courseEvaluation);
	    getListView().setAdapter(myCoursesAdapter);
	    getListView().setEmptyView(findViewById(R.id.empty));
	}
    }

    @Override
    public void onSaveInstanceState(final Bundle savedInstanceState) {
	super.onSaveInstanceState(savedInstanceState);
	savedInstanceState.putSerializable("courseEvaluation", courseEvaluation);
    }
}
