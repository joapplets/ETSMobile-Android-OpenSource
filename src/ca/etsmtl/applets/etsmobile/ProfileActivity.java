package ca.etsmtl.applets.etsmobile;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;

import org.apache.http.HttpResponse;
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
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.services.ProfileTask;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class ProfileActivity extends Activity implements OnClickListener, OnDismissListener {

    public String bandwith;

    private static class ProfileHandler extends Handler {
	private final WeakReference<ProfileActivity> ref;

	public ProfileHandler(final ProfileActivity act) {
	    ref = new WeakReference<ProfileActivity>(act);
	}

	@Override
	public void handleMessage(final Message msg) {
	    super.handleMessage(msg);
	    final ProfileActivity act = ref.get();
	    switch (msg.what) {
	    case ProfileTask.ON_POST_EXEC:
		if (!act.isFinishing()) {
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
		}
		break;
	    case 2:

		if (!act.isFinishing()) {
		    act.bandwith = (String) msg.obj;
		    act.showDialog(ProfileActivity.SHOW_BAND_RESULT);
		}
		break;
	    default:
		break;
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
    private TextView btnBandwith;
    private String appt;
    private String rez;
    private SharedPreferences prefs;

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

    private void getBandwith() {
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
		    ent = EntityUtils.toString(re.getEntity());

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

	}.execute(rez, appt);
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

	btnBandwith = (TextView) findViewById(R.id.btn_bandwith);
	btnBandwith.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		showDialog(3);
	    }
	});

	prefs = PreferenceManager.getDefaultSharedPreferences(this);
	creds = new UserCredentials(prefs);

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

	case SHOW_BAND_RESULT:
	    final String result = bandwith;
	    d = new AlertDialog.Builder(this).setMessage(
		    "Phase: " + rez + " Appt: " + appt + " \nIl vous reste : " + result).create();
	    break;
	case 3:
	    // set bandwith labels
	    ((TextView) view.findViewById(R.id.textView1))
		    .setText(getString(R.string.bandwith_dialog_rez));
	    ((TextView) view.findViewById(R.id.textView2))
		    .setText(getString(R.string.bandwith_dialog_appt));
	    ((TextView) view.findViewById(R.id.login_dialog_code_univesel)).setHint(null);

	    ((EditText) view.findViewById(R.id.login_dialog_code_univesel))
		    .setInputType(InputType.TYPE_CLASS_NUMBER);
	    ((EditText) view.findViewById(R.id.login_dialog_mot_passe))
		    .setInputType(InputType.TYPE_CLASS_NUMBER);

	    // create dialog
	    d = new AlertDialog.Builder(this).setTitle(R.string.votre_lieu_de_r_sidence)
		    .setView(view)
		    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			    final Editor edit = prefs.edit();
			    rez = ((TextView) view.findViewById(R.id.login_dialog_code_univesel))
				    .getText().toString();
			    appt = ((TextView) view.findViewById(R.id.login_dialog_mot_passe))
				    .getText().toString();
			    if (!rez.equals("") && !appt.equals("")) {
				edit.putString(UserCredentials.REZ, rez);

				edit.putString(UserCredentials.APPT, appt);
				edit.commit();

				getBandwith();

				dialog.dismiss();
			    }
			}
		    }).create();
	    break;
	}
	return d;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
	doLogin();
    }
}
