package ca.etsmtl.applets.etsmobile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.adapters.MyCourseSessionAdapter;
import ca.etsmtl.applets.etsmobile.models.Session;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MyCourseSessionActivity extends ListActivity {

	private ArrayList<Session> sessions;
	private MyCourseSessionAdapter myCoursesAdapter;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.my_courses_view);
		
		ImageButton btnHome = (ImageButton)findViewById(R.id.empty_nav_bar_home_btn);
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		UserCredentials creds = new UserCredentials(PreferenceManager.getDefaultSharedPreferences(this));
		
		if (savedInstanceState != null) {
			sessions = (ArrayList<Session>) savedInstanceState.getSerializable("sessions");
		}
		
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
		
		if (creds.getPassword() != null && creds.getUsername() != null && ! "".equals(creds.getPassword()) && ! "".equals(creds.getUsername())) {
			
			if (sessions == null) {
				final SignetBackgroundThread<ArrayList<Session>, Session> signetBackgroundThead = 
						new SignetBackgroundThread<ArrayList<Session>, Session>(
								"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx", 
								"listeSessions",
								creds,
								Session.class,
								FetchType.ARRAY);

				signetBackgroundThead.execute();
				
				final ProgressDialog progress = new ProgressDialog(this);
				progress.setMessage(getString(R.string.loading));
				progress.show();
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							final ArrayList<Session> newSessions = signetBackgroundThead.get();
							
							runOnUiThread(new Runnable() {
								public void run() {
									sessions = newSessions;
									myCoursesAdapter = new MyCourseSessionAdapter(getApplicationContext(), R.layout.session_list_item, sessions);
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
				myCoursesAdapter = new MyCourseSessionAdapter(getApplicationContext(), R.layout.session_list_item, sessions);
				getListView().setAdapter(myCoursesAdapter);
			}
			
		} else {
			Toast.makeText(this, getString(R.string.usernamePasswordRequired), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  savedInstanceState.putSerializable("sessions", this.sessions);
	}
}
