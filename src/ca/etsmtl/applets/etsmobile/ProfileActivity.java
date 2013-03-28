package ca.etsmtl.applets.etsmobile;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.services.ProfileTask;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class ProfileActivity extends Activity implements OnClickListener, OnDismissListener {

    private static class ProfileHandler extends Handler {
	private final WeakReference<ProfileActivity> ref;

	public ProfileHandler(final ProfileActivity act) {
	    ref = new WeakReference<ProfileActivity>(act);
	}

	@Override
	public void handleMessage(final Message msg) {
	    super.handleMessage(msg);
	    final ProfileActivity act = ref.get();
	    // weak ref might be null or finishing
	    if (act != null && !act.isFinishing()) {

		switch (msg.what) {
		case ProfileTask.ON_POST_EXEC:

		    if (act.navBar != null) {
			act.navBar.hideLoading();
		    }
		    final Bundle data = msg.getData();
		    final StudentProfile studentProfile = (StudentProfile) data
			    .get(ProfileTask.PROFILE_KEY);

		    if (studentProfile != null) {
			if (studentProfile.getSolde().equals("")
				&& studentProfile.getNom().equals("")
				&& studentProfile.getPrenom().equals("")) {

			    Toast.makeText(act, R.string.error_profile_login, Toast.LENGTH_LONG)
				    .show();

			    act.showDialog(ProfileActivity.SHOW_LOGIN);

			    act.btnLogin.setText(R.string.login);
			    act.btnLogin.setTag(false);
			    act.btnLogin.setBackgroundColor(Color.GRAY);

			} else {

			    act.name.setText(studentProfile.getPrenom());
			    act.lastname.setText(studentProfile.getNom());
			    act.solde.setText(studentProfile.getSolde());
			    act.codeP.setText(studentProfile.getCodePerm());

			    // save credentials to prefs
			    final SharedPreferences prefs = act.prefs;

			    final Editor editor = prefs.edit();
			    editor.putString("codeP", act.creds.getUsername());
			    editor.putString("codeU", act.creds.getPassword());
			    editor.commit();

			    act.btnLogin.setTag(true);
			    act.btnLogin.setText(act.getString(R.string.logout));
			    act.btnLogin.setBackgroundColor(Color.RED);
			}
		    }

		    break;
		case 2:
		    // show bandwith
		    if (!act.isFinishing()) {
			if (msg.obj != null) {
			    act.used_bandwith.setText(act.getString(R.string.utilise)
				    + (String) msg.obj);
			    final String b = (String) msg.obj;
			    act.progess.setProgress((int) Double.parseDouble(b.substring(0,
				    b.indexOf("GB") - 1)));
			} else {
			}
		    }
		    act.appt_input.setEnabled(true);
		    act.phase_input.setEnabled(true);
		    break;
		default:
		    break;
		}
	    }
	}
    }

    private static final int SHOW_LOGIN = 1;
    protected static final int LOGIN_ERROR = 0;
    protected static final int SHOW_BAND_RESULT = 2;
    private Button btnLogin;
    protected StudentProfile profile;
    private UserCredentials creds;
    private View view;
    private NavBar navBar;
    private TextView name;
    private TextView lastname;
    private TextView solde;
    private TextView codeP;
    private Handler handler;
    private SharedPreferences prefs;
    private ProgressBar progess;
    private TextView phase_input;
    private TextView appt_input;
    private TextView used_bandwith;

    private void doLogin() {
	creds = new UserCredentials(PreferenceManager.getDefaultSharedPreferences(this));
	CharSequence text = "";
	boolean tag = false;
	int mColor = Color.RED;

	if (!creds.getUsername().equals("") && !creds.getPassword().equals("")) {
	    text = getString(R.string.logout);
	    tag = true;
	    navBar.showLoading();
	    new ProfileTask(handler).execute(creds);
	} else {
	    showDialog(SHOW_LOGIN, null);
	    text = getString(R.string.login);
	    tag = false;
	    mColor = Color.GRAY;
	}
	btnLogin.setText(text);
	btnLogin.setTag(tag);
	btnLogin.setBackgroundColor(mColor);
    }

    private void getBandwith(String phase, String appt) {

	appt_input.setEnabled(false);
	phase_input.setEnabled(false);

	final Editor edit = prefs.edit();
	creds.setPhase(phase);
	creds.setAppt(appt);
	edit.putString(UserCredentials.REZ, phase);
	edit.putString(UserCredentials.APPT, appt);
	edit.commit();

	new AsyncTask<String, Void, String>() {

	    @Override
	    protected String doInBackground(String... params) {

		String ent = null;
		try {

		    final StringBuilder sb = new StringBuilder();
		    sb.append("http://etsmtl.me/py/usage/");
		    sb.append(params[0]);
		    sb.append("/");
		    sb.append(params[1]);

		    final HttpGet get = new HttpGet(URI.create(sb.toString()));
		    final HttpClient client = new DefaultHttpClient();
		    final HttpResponse re = client.execute(get);

		    if (re.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			ent = EntityUtils.toString(re.getEntity());
		    } else {
			ent = null;
		    }

		} catch (final IOException e) {
		    e.printStackTrace();
		}

		return ent;
	    }

	    @Override
	    protected void onPostExecute(String result) {
		handler.obtainMessage(2, result).sendToTarget();
		super.onPostExecute(result);
	    }

	}.execute(phase, appt);
    }

    /**
     * Login btn
     */
    @Override
    public void onClick(final View view) {
	if (!(Boolean) btnLogin.getTag()) {
	    // login
	    showDialog(SHOW_LOGIN, null);
	} else {
	    // logout
	    // remove credentials
	    final Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    editor.putString(UserCredentials.CODE_P, "");
	    editor.putString(UserCredentials.CODE_U, "");

	    editor.commit();
	    creds = null;

	    name.setText("");
	    lastname.setText("");
	    solde.setText("");
	    codeP.setText("");

	    btnLogin.setTag(false);
	    btnLogin.setText(getString(R.string.login));
	    finish();

	}
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.student_profile);
	handler = new ProfileHandler(this);

	btnLogin = (Button) findViewById(R.id.profile_login_btn);
	btnLogin.setOnClickListener(this);

	// nav bar
	navBar = (NavBar) findViewById(R.id.navBar1);
	navBar.setTitle(R.drawable.navbar_profil_title);
	navBar.hideRightButton();
	navBar.hideLoading();

	name = (TextView) findViewById(R.id.student_profile_name);
	lastname = (TextView) findViewById(R.id.student_profile_lastname);
	solde = (TextView) findViewById(R.id.student_profile_solde);
	codeP = (TextView) findViewById(R.id.student_profile_codePermanent);
	progess = (ProgressBar) findViewById(R.id.bandwith_progress);
	used_bandwith = (TextView) findViewById(R.id.bandwith_used_lbl);
	phase_input = (TextView) findViewById(R.id.bandwith_phase_input);

	phase_input.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() >= 1) {
		    if (appt_input.getText().length() > 4) {
			getBandwith(s.toString(), appt_input.getText().toString());
		    }
		}
	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	    }

	    @Override
	    public void afterTextChanged(Editable s) {
	    }
	});

	appt_input = (TextView) findViewById(R.id.bandwith_appt_input);

	appt_input.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() >= 4) {
		    if (phase_input.getText().length() >= 1) {
			getBandwith(phase_input.getText().toString(), s.toString());
		    }
		}
	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	    }

	    @Override
	    public void afterTextChanged(Editable s) {
	    }
	});

	prefs = PreferenceManager.getDefaultSharedPreferences(this);
	creds = new UserCredentials(prefs);
	if (creds.hasBandwithInfo()) {
	    phase_input.setText(creds.getPhase());
	    appt_input.setText(creds.getAppt());
	}
	doLogin();
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
	Dialog d = super.onCreateDialog(id);
	view = getLayoutInflater().inflate(R.layout.login_dialog, null);
	switch (id) {
	case SHOW_LOGIN:
	    d = new AlertDialog.Builder(this).setTitle(getString(R.string.login_dialog_title))
		    .setView(view).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			/**
			 * Login dialog onClick
			 */
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
			    String codeP;
			    String codeU;
			    switch (which) {
			    case DialogInterface.BUTTON_POSITIVE:
				codeP = ((TextView) view
					.findViewById(R.id.login_dialog_code_univesel)).getText()
					.toString();
				codeU = ((TextView) view.findViewById(R.id.login_dialog_mot_passe))
					.getText().toString();
				creds = new UserCredentials(codeP, codeU);
				new ProfileTask(handler).execute(creds);
				break;

			    default:
				dialog.cancel();
				dialog.dismiss();
				break;
			    }
			}
		    }).create();
	    break;

	// case SHOW_BAND_RESULT:
	// final String result = bandwith;
	// d = new AlertDialog.Builder(this).setMessage(
	// "Phase: " + rez + " Appt: " + appt + " \nIl vous reste : " +
	// result).create();
	// break;
	// case SHOW_BANDW:
	// // set bandwith labels
	// ((TextView) view.findViewById(R.id.textView1))
	// .setText(getString(R.string.bandwith_dialog_rez));
	// ((TextView) view.findViewById(R.id.bandwith_used_lbl))
	// .setText(getString(R.string.bandwith_dialog_appt));
	// ((TextView)
	// view.findViewById(R.id.login_dialog_code_univesel)).setHint(null);
	//
	// ((EditText) view.findViewById(R.id.login_dialog_code_univesel))
	// .setInputType(InputType.TYPE_CLASS_NUMBER);
	// ((EditText) view.findViewById(R.id.login_dialog_mot_passe))
	// .setInputType(InputType.TYPE_CLASS_NUMBER);
	//
	// ((EditText)
	// view.findViewById(R.id.login_dialog_code_univesel)).setText(creds.getRez());
	// ((EditText)
	// view.findViewById(R.id.login_dialog_mot_passe)).setText(creds.getAppt());
	//
	// // create dialog
	// d = new
	// AlertDialog.Builder(this).setTitle(R.string.votre_lieu_de_r_sidence)
	// .setView(view)
	// .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
	// {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// final Editor edit = prefs.edit();
	// rez = ((TextView) view.findViewById(R.id.login_dialog_code_univesel))
	// .getText().toString();
	// appt = ((TextView) view.findViewById(R.id.login_dialog_mot_passe))
	// .getText().toString();
	// if (!rez.equals("") && !appt.equals("")) {
	// creds.setRez(rez);
	// creds.setAppt(rez);
	// edit.putString(UserCredentials.REZ, rez);
	// edit.putString(UserCredentials.APPT, appt);
	// edit.commit();
	//
	// getBandwith();
	//
	// dialog.dismiss();
	// }
	// }
	// }).create();
	// break;
	}
	return d;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
	doLogin();
    }
}
