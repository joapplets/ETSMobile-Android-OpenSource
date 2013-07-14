package ca.etsmtl.applets.etsmobile;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import ca.etsmtl.applets.etsmobile.adapters.ETSMobileAdapter;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.services.ProfileTask;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class ETSMobileActivity extends Activity implements OnItemClickListener, OnTouchListener,
	android.content.DialogInterface.OnClickListener {

    /**
     * Handles login; save to sharedPrefs if the {@link StudentProfile} is valid
     * 
     * @author Phil
     * @TODO: refactor
     */
    private static final class LoginHandler extends Handler {
	private final WeakReference<ETSMobileActivity> ref;

	public LoginHandler(ETSMobileActivity act) {
	    ref = new WeakReference<ETSMobileActivity>(act);
	}

	@Override
	public void handleMessage(final Message msg) {
	    final ETSMobileActivity act = ref.get();
	    // weak ref might be null or finishing
	    if (act != null && !act.isFinishing()) {
		switch (msg.what) {

		case ProfileTask.ON_POST_EXEC:

		    final Bundle data = msg.getData();
		    final StudentProfile studentProfile = (StudentProfile) data
			    .get(ProfileTask.PROFILE_KEY);
		    // if profile found
		    if (studentProfile != null) {
			// if profile has info
			if (!studentProfile.getSolde().equals("")
				&& !studentProfile.getNom().equals("")
				&& !studentProfile.getPrenom().equals("")) {
			    // save credentials to prefs
			    final SharedPreferences prefs = PreferenceManager
				    .getDefaultSharedPreferences(act);
			    final Editor editor = prefs.edit();
			    editor.putString("codeP", act.credentials.getUsername());
			    editor.putString("codeU", act.credentials.getPassword());
			    editor.commit();
			    // show user appreciation
			    Toast.makeText(act, act.getString(R.string.welcome), Toast.LENGTH_LONG)
				    .show();
			} else {
			    // Log.d("TAG", studentProfile.toString());
			    // wrong
			    Toast.makeText(
				    act,
				    "Erreur d'identification : Vos informations personnelles sont érronée(s)",
				    Toast.LENGTH_LONG).show();
			    act.showDialog(ETSMobileActivity.LOGIN);
			}
		    }
		    break;

		default:
		    break;
		}
	    }
	}
    }

    private UserCredentials credentials;
    private NavBar navBar;
    private static final int LOGIN = 0;
    private View view;
    private Handler handler;

    /**
     * Login dialog onClick(ok); create Credential Obj; Launch
     * {@link ProfileTask}
     * 
     * @TODO: refactor
     */
    @Override
    public void onClick(final DialogInterface dialog, final int which) {
	String codeP;
	String codeU;

	switch (which) {
	case DialogInterface.BUTTON_POSITIVE:

	    codeP = ((TextView) view.findViewById(R.id.login_dialog_code_univesel)).getText()
		    .toString();
	    codeU = ((TextView) view.findViewById(R.id.login_dialog_mot_passe)).getText()
		    .toString();

	    credentials = new UserCredentials(codeP, codeU);

	    if (!credentials.isEmployee()) {
		new ProfileTask(this, handler).execute(credentials);
	    } else {
		finish();
		startActivity(new Intent(this, ETSMobileEmplActivity.class));
	    }
	    break;

	default:
	    break;
	}
    }

    /**
     * MAIN
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.main);
	// handles UI for login
	handler = new LoginHandler(this);

	// home made nav bar; hide all
	navBar = (NavBar) findViewById(R.id.navBarMain);
	navBar.setTitle(R.drawable.navbar_title);
	navBar.hideLoading();
	navBar.hideRightButton();
	navBar.hideHome();

	// Custom GridAdapter, each icon is the main entry to a "module"
	final GridView gridview = (GridView) findViewById(R.id.gridview);
	gridview.setAdapter(new ETSMobileAdapter(getApplicationContext()));

	// load credentials from sharePrefs; No prefs, show login
	final UserCredentials creds = new UserCredentials(
		PreferenceManager.getDefaultSharedPreferences(this));

	if (!creds.isLoggedIn()) {
	    showDialog(ETSMobileActivity.LOGIN);
	}

	gridview.setOnItemClickListener(this);
	gridview.setOnTouchListener(this);

	// about bnt
	((ImageButton) findViewById(R.id.imgBtnabout)).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		final Intent intent = new Intent(v.getContext(), AboutActivity.class);
		startActivity(intent);
	    }
	});
    }

    /**
     * Inflate custom dialog view; Create Alert Dialog
     */
    @Override
    protected Dialog onCreateDialog(final int id, final Bundle args) {
	Dialog d = super.onCreateDialog(id, args);
	view = getLayoutInflater().inflate(R.layout.login_dialog, null, true);
	switch (id) {
	case LOGIN:
	    d = new AlertDialog.Builder(this).setTitle(getString(R.string.login_dialog_title))
		    .setView(view).setPositiveButton("Ok", this).create();
	    break;
	}
	return d;
    }

    /**
     * Grid Item Click
     */
    @Override
    public void onItemClick(final AdapterView<?> arg0, final View arg1, final int position,
	    final long id) {
	Intent intent = null;
	switch (position) {
	case 0:
	    intent = new Intent(this, ProfileActivity.class);
	    break;
	case 1:
	    intent = new Intent(this, NewsListActivity.class);
	    break;
	case 2:
	    intent = new Intent(this, SecurityActivity.class);
	    break;
	case 3:
		intent = new Intent(this, ScheduleWeekActivity.class);
	   // intent = new Intent(this, ScheduleActivity.class);
	    break;
	case 4:
	    intent = new Intent(this, BottinListActivity.class);
	    break;
	case 5:
	    intent = new Intent(this, MyCourseSessionActivity.class);
	    break;
	case 6:
	    intent = new Intent(this, BibliothequeActivity.class);
	    break;
	case 7:
	    // contact; Ask to open email app; prefill email info
	    intent = new Intent(android.content.Intent.ACTION_SEND);
	    intent.setType("plain/text");
	    intent.putExtra(android.content.Intent.EXTRA_EMAIL,
		    new String[] { "applets@ens.etsmtl.ca" });
	    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ETSMobile-Android : Commentaire");
	    intent.putExtra(android.content.Intent.EXTRA_TEXT, "Bravo ! 10/10 !");
	    break;
	case 8:
	    intent = new Intent(this, EmailWebView.class);
	    break;
	default:
	    break;
	}

	if (intent != null) {
	    startActivity(intent);
	}
    }

    /** Disable scrolling of the main view */
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
	switch (event.getAction()) {
	case MotionEvent.ACTION_MOVE:
	    return true;
	default:
	    break;
	}
	return false;
    }

}