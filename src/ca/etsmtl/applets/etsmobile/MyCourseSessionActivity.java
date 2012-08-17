package ca.etsmtl.applets.etsmobile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.adapters.MyCourseSessionAdapter;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MyCourseSessionActivity extends ListActivity {

	private ArrayList<Session> sessions = new ArrayList<Session>();
	private MyCourseSessionAdapter myCoursesAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_courses_view);
		
		UserCredentials creds = new UserCredentials(PreferenceManager.getDefaultSharedPreferences(this));
		
		if (creds.getPassword() != null && creds.getUsername() != null && ! "".equals(creds.getPassword()) && ! "".equals(creds.getUsername())) {
			final SignetBackgroundThread<ArrayList<Session>, Session> signetBackgroundThead = 
					new SignetBackgroundThread<ArrayList<Session>, Session>(
							"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx", 
							"listeSessions",
							creds,
							Session.class,
							FetchType.ARRAY);
			
			myCoursesAdapter = new MyCourseSessionAdapter(this, android.R.layout.simple_list_item_1, sessions);
			getListView().setAdapter(myCoursesAdapter);
			
			signetBackgroundThead.execute();

			getListView().setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position,
						long arg3) {
					Bundle b = new Bundle();
					b.putString("session", myCoursesAdapter.getItem(position).getShortName());
					Intent nextActivity = new Intent(view.getContext(), MyCourseActivity.class);
					nextActivity.putExtras(b);
	                startActivity(nextActivity);
				}
			});
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						final ArrayList<Session> newSessions = signetBackgroundThead.get();
						
						runOnUiThread(new Runnable() {
							public void run() {
								sessions = newSessions;
								myCoursesAdapter = new MyCourseSessionAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, sessions);
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
