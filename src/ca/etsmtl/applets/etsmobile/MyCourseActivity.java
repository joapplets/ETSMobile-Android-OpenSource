package ca.etsmtl.applets.etsmobile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.etsmt.applets.etsmobile.dialogs.LoginDialog;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import ca.etsmtl.applets.etsmobile.adapters.MyCourseAdapter;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.models.Course;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.services.ProfileTask;

public class MyCourseActivity extends ListActivity implements OnDismissListener {

	private static final int SHOW_LOGIN = 0;
	private ArrayList<Course> courseActivities;
	private MyCourseAdapter myCoursesAdapter;
	private ObservableBundle bundle;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_courses_view);

		final ImageButton btnHome = (ImageButton) findViewById(R.id.empty_nav_bar_home_btn);
		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final Intent intent = new Intent(v.getContext(),
						ETSMobileActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

		if (savedInstanceState != null) {
			courseActivities = (ArrayList<Course>) savedInstanceState
					.getSerializable("courseActivities");
		}

		final UserCredentials creds = new UserCredentials(
				PreferenceManager.getDefaultSharedPreferences(this));
		final String sessionString = getIntent().getExtras().getString(
				"session");

		if (creds.getPassword() != null && creds.getUsername() != null
				&& !"".equals(creds.getPassword())
				&& !"".equals(creds.getUsername())) {
			getListView().setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(final AdapterView<?> adapterView,
						final View view, final int position, final long arg3) {
					final Bundle b = new Bundle();
					b.putString("sigle", myCoursesAdapter.getItem(position)
							.getSigle());
					b.putString("session", sessionString);
					b.putString("cote", myCoursesAdapter.getItem(position)
							.getCote());
					b.putString("groupe", myCoursesAdapter.getItem(position)
							.getGroupe());
					final Intent nextActivity = new Intent(view.getContext(),
							MyCourseDetailActivity.class);
					nextActivity.putExtras(b);
					startActivity(nextActivity);
				}
			});

			if (courseActivities == null) {
				final SignetBackgroundThread<ArrayList<Course>, Course> signetBackgroundThead = new SignetBackgroundThread<ArrayList<Course>, Course>(
						"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
						"listeCours", creds, Course.class, FetchType.ARRAY);

				signetBackgroundThead.execute();

				final ProgressDialog progress = new ProgressDialog(this);
				progress.setMessage(getString(R.string.loading));
				progress.show();

				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							final ArrayList<Course> newCourseActivities = signetBackgroundThead
									.get();

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									courseActivities = new ArrayList<Course>();

									for (final Course course : newCourseActivities) {
										if (sessionString.equals(course
												.getSession())) {
											courseActivities.add(course);
										}
									}

									myCoursesAdapter = new MyCourseAdapter(
											getApplicationContext(),
											R.layout.course_list_item,
											courseActivities);
									getListView().setAdapter(myCoursesAdapter);
									if (progress != null) {
										progress.dismiss();
									}
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
				myCoursesAdapter = new MyCourseAdapter(getApplicationContext(),
						R.layout.course_list_item, courseActivities);
				getListView().setAdapter(myCoursesAdapter);
			}
		} else {
			showDialog(SHOW_LOGIN);
			Toast.makeText(this, getString(R.string.usernamePasswordRequired),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog d = null;
		switch (id) {
		case SHOW_LOGIN:
			d = new LoginDialog(this);
			d.setOnDismissListener(this);
			break;

		default:
			break;
		}
		return d;
	}

	@Override
	public void onSaveInstanceState(final Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState
				.putSerializable("courseActivities", courseActivities);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		doLogin();
	}

	private void doLogin() {
		UserCredentials creds = new UserCredentials(
				PreferenceManager.getDefaultSharedPreferences(this));
		CharSequence text = "";
		boolean tag = false;
		if (!creds.getUsername().equals("") && !creds.getPassword().equals("")) {
			new ProfileTask(bundle).execute(creds.getUsername(),
					creds.getPassword());
			text = getString(R.string.logout);
			tag = true;
		} else {
			text = getString(R.string.login);
			tag = false;
		}

	}
}
