package ca.etsmtl.applets.etsmobile;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import ca.etsmtl.applets.etsmobile.adapters.MyCourseAdapter;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.dialogs.LoginDialog;
import ca.etsmtl.applets.etsmobile.models.Course;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.services.ProfileTask;
import ca.etsmtl.applets.etsmobile.views.NavBar;

/**
 * Liste des Cours
 * 
 * @author Phil
 * 
 */
public class MyCourseListActivity extends ListActivity implements
		OnDismissListener {

	private static final int SHOW_LOGIN = 0;
	protected static final int LOGIN_ERROR = 0;
	private ArrayList<Course> courseActivities;
	private MyCourseAdapter myCoursesAdapter;

	private Handler handler;

	private static class MyCourseHandler extends Handler {
		private final WeakReference<MyCourseListActivity> ref;

		public MyCourseHandler(MyCourseListActivity act) {
			ref = new WeakReference<MyCourseListActivity>(act);
		}

		@Override
		public void handleMessage(final Message msg) {
			final MyCourseListActivity act = ref.get();
			switch (msg.what) {

			case ProfileTask.ON_POST_EXEC:
				final Bundle data = msg.getData();
				final StudentProfile profile = (StudentProfile) data
						.get(ProfileTask.PROFILE_KEY);
				if (profile != null) {
					// save credentials to prefs
					final SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(act
									.getApplicationContext());
					final Editor editor = prefs.edit();
					editor.putString("codeP", act.creds.getUsername());
					editor.putString("codeU", act.creds.getPassword());
					editor.commit();

					act.initCours(act.sessionString);
				} else {
					act.showDialog(MyCourseListActivity.LOGIN_ERROR);
				}
				break;

			default:
				break;
			}
		}
	}

	private UserCredentials creds;
	private String sessionString = "";
	private NavBar navBar;

	private void doLogin() {
		creds = new UserCredentials(
				PreferenceManager.getDefaultSharedPreferences(this));

		if (!creds.getUsername().equals("") && !creds.getPassword().equals("")) {
			new ProfileTask(handler).execute(creds);
		}

	}

	private void initCours(final String sessionString) {
		creds = new UserCredentials(
				PreferenceManager.getDefaultSharedPreferences(this));
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
					"listeCours", creds, Course.class);
			navBar.showLoading();

			new Thread(new Runnable() {

				@Override
				public void run() {
					final ArrayList<Course> newCourseActivities = signetBackgroundThead
							.fetchArray();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							courseActivities = new ArrayList<Course>();

							for (final Course course : newCourseActivities) {
								if (sessionString.equals(course.getSession())) {
									courseActivities.add(course);
								}
							}

							myCoursesAdapter = new MyCourseAdapter(
									getApplicationContext(),
									R.layout.course_list_item, courseActivities);
							getListView().setAdapter(myCoursesAdapter);
							navBar.hideLoading();
						}
					});

				}
			}).start();
		} else {
			myCoursesAdapter = new MyCourseAdapter(getApplicationContext(),
					R.layout.course_list_item, courseActivities);
			getListView().setAdapter(myCoursesAdapter);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_courses_view);

		navBar = (NavBar) findViewById(R.id.navBar3);
		navBar.hideRightButton();
		navBar.setTitle(R.drawable.navbar_notes_title);

		handler = new MyCourseHandler(this);

		if (savedInstanceState != null) {
			courseActivities = (ArrayList<Course>) savedInstanceState
					.getSerializable("courseActivities");
		}

		final UserCredentials creds = new UserCredentials(
				PreferenceManager.getDefaultSharedPreferences(this));
		sessionString = getIntent().getExtras().getString("session");
		// set title
		navBar.setTitle(getIntent().getExtras().getString("session_long"));

		if (creds.getPassword() != null && creds.getUsername() != null
				&& !"".equals(creds.getPassword())
				&& !"".equals(creds.getUsername())) {
			initCours(sessionString);
		} else {
			showDialog(MyCourseListActivity.SHOW_LOGIN);
			Toast.makeText(this, getString(R.string.usernamePasswordRequired),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected Dialog onCreateDialog(final int id) {
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
	public void onDismiss(final DialogInterface dialog) {
		doLogin();
	}

	@Override
	public void onSaveInstanceState(final Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState
				.putSerializable("courseActivities", courseActivities);
	}
}