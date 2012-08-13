package ca.etsmtl.applets.etsmobile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.models.Course;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyCourseActivity extends ListActivity {
	
	private ArrayList<Course> courseActivities = new ArrayList<Course>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_courses_view);
		
		UserCredentials creds = new UserCredentials(getPreferences(MODE_PRIVATE));
		final String sessionString = getIntent().getExtras().getString("session");
		
		if (creds.getPassword() != null && creds.getUsername() != null && ! "".equals(creds.getPassword()) && ! "".equals(creds.getUsername())) {
			final SignetBackgroundThread<ArrayList<Course>, Course> signetBackgroundThead = 
					new SignetBackgroundThread<ArrayList<Course>, Course>(
							"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx", 
							"listeCours",
							creds,
							Course.class,
							FetchType.ARRAY);
			
			ArrayAdapter<Course> myCoursesAdapter = new ArrayAdapter<Course>(getApplicationContext(), android.R.layout.simple_list_item_1, courseActivities);
			getListView().setAdapter(myCoursesAdapter);
			
			signetBackgroundThead.execute();

			getListView().setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position,
						long arg3) {
					Bundle b = new Bundle();
					b.putString("sigle", courseActivities.get(position).getSigle());
					b.putString("session", sessionString);
					b.putString("cote", courseActivities.get(position).getCote());
					b.putString("groupe", courseActivities.get(position).getGroupe());
					Intent nextActivity = new Intent(view.getContext(), MyCourseDetailActivity.class);
					nextActivity.putExtras(b);
	                startActivity(nextActivity);
				}
			});
			
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
								
								ArrayAdapter<Course> myCoursesAdapter = new ArrayAdapter<Course>(getApplicationContext(), android.R.layout.simple_list_item_1, courseActivities);
								getListView().setAdapter(myCoursesAdapter);
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
	}
}
