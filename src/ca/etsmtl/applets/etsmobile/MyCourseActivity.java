package ca.etsmtl.applets.etsmobile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.adapters.MyCourseAdapter;
import ca.etsmtl.applets.etsmobile.models.Course;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyCourseActivity extends ListActivity {
	
	private ArrayList<Course> courseActivities;
	private MyCourseAdapter myCoursesAdapter;
	
	@SuppressWarnings("unchecked")
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
			courseActivities = (ArrayList<Course>) savedInstanceState.getSerializable("courseActivities");
		}
		
		UserCredentials creds = new UserCredentials(PreferenceManager.getDefaultSharedPreferences(this));
		final String sessionString = getIntent().getExtras().getString("session");
		
		if (creds.getPassword() != null && creds.getUsername() != null && ! "".equals(creds.getPassword()) && ! "".equals(creds.getUsername())) {
			getListView().setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position,
						long arg3) {
					Bundle b = new Bundle();
					b.putString("sigle", myCoursesAdapter.getItem(position).getSigle());
					b.putString("session", sessionString);
					b.putString("cote", myCoursesAdapter.getItem(position).getCote());
					b.putString("groupe", myCoursesAdapter.getItem(position).getGroupe());
					Intent nextActivity = new Intent(view.getContext(), MyCourseDetailActivity.class);
					nextActivity.putExtras(b);
	                startActivity(nextActivity);
				}
			});
			
			if (courseActivities == null) {
				final SignetBackgroundThread<ArrayList<Course>, Course> signetBackgroundThead = 
						new SignetBackgroundThread<ArrayList<Course>, Course>(
								"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx", 
								"listeCours",
								creds,
								Course.class,
								FetchType.ARRAY);
				
				signetBackgroundThead.execute();
				
				final ProgressDialog progress = new ProgressDialog(this);
				progress.setMessage(getString(R.string.loading));
				progress.show();
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							final ArrayList<Course> newCourseActivities = signetBackgroundThead.get();
							
							runOnUiThread(new Runnable() {
								public void run() {
									courseActivities = new ArrayList<Course>();
									
									for (Course course : newCourseActivities) {
										if (sessionString.equals(course.getSession())) {
											courseActivities.add(course);
										}
									}
									
									myCoursesAdapter = new MyCourseAdapter(getApplicationContext(), R.layout.course_list_item, courseActivities);
									getListView().setAdapter(myCoursesAdapter);
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
				myCoursesAdapter = new MyCourseAdapter(getApplicationContext(), R.layout.course_list_item, courseActivities);
				getListView().setAdapter(myCoursesAdapter);
			}
		} else {
			Toast.makeText(this, getString(R.string.usernamePasswordRequired), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  savedInstanceState.putSerializable("courseActivities", this.courseActivities);
	}
}
