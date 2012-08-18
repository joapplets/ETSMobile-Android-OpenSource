package ca.etsmtl.applets.etsmobile;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.adapters.MyCourseDetailAdapter;
import ca.etsmtl.applets.etsmobile.models.CourseEvaluation;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class MyCourseDetailActivity extends ListActivity {
	
	private CourseEvaluation courseEvaluation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.my_courses_view);
		
		ImageButton btnHome = (ImageButton)findViewById(R.id.empty_nav_bar_home_btn);
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ETSMobileActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		
		if (savedInstanceState != null) {
			courseEvaluation = (CourseEvaluation) savedInstanceState.getSerializable("courseEvaluation");
		}
		
		if (courseEvaluation == null) {
			UserCredentials creds = new UserCredentials(PreferenceManager.getDefaultSharedPreferences(this));
			
			final String session = getIntent().getExtras().getString("session");
			final String sigle = getIntent().getExtras().getString("sigle");
			final String coteFinale = getIntent().getExtras().getString("cote");
			final String groupe = getIntent().getExtras().getString("groupe");
	
			if (creds.getPassword() != null && creds.getUsername() != null && ! "".equals(creds.getPassword()) && ! "".equals(creds.getUsername())) {
				Dictionary<String, String> requestParams = new Hashtable<String, String>();
				requestParams.put("codeAccesUniversel", creds.getUsername());
				requestParams.put("motPasse", creds.getPassword());
				requestParams.put("pSigle", sigle);
				requestParams.put("pGroupe", groupe);
				requestParams.put("pSession", session);
				
				final SignetBackgroundThread<CourseEvaluation ,CourseEvaluation> signetBackgroundThead = 
						new SignetBackgroundThread<CourseEvaluation, CourseEvaluation>(
								"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx", 
								"listeElementsEvaluation",
								requestParams,
								CourseEvaluation.class,
								FetchType.OBJECT);
				
				signetBackgroundThead.execute();
				
				final ProgressDialog progress = new ProgressDialog(this);
				progress.setMessage(getString(R.string.loading));
				progress.show();
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							courseEvaluation = signetBackgroundThead.get();
							courseEvaluation.setCote(coteFinale);
							
							runOnUiThread(new Runnable() {
								public void run() {
									MyCourseDetailAdapter myCoursesAdapter = new MyCourseDetailAdapter(getApplicationContext(), R.layout.list_item_value, courseEvaluation.getEvaluationElements(), courseEvaluation);
									getListView().setAdapter(myCoursesAdapter);
									getListView().setEmptyView(findViewById(R.id.empty));
									
									if (progress != null) {
										progress.dismiss();
									}
								}
							});
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
					}
				}).start();
			} else {
				Toast.makeText(this, getString(R.string.usernamePasswordRequired), Toast.LENGTH_LONG).show();
			}
		} else {
			MyCourseDetailAdapter myCoursesAdapter = new MyCourseDetailAdapter(getApplicationContext(), R.layout.list_item_value, courseEvaluation.getEvaluationElements(), courseEvaluation);
			getListView().setAdapter(myCoursesAdapter);
			getListView().setEmptyView(findViewById(R.id.empty));
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  savedInstanceState.putSerializable("courseEvaluation", this.courseEvaluation);
	}
}
