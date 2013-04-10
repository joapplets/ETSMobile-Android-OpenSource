package ca.etsmtl.applets.etsmobile;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
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

import com.bugsense.trace.BugSenseHandler;

public class ProfileActivity extends Activity implements OnClickListener, OnDismissListener {

    /**
     * Handles UI logic after async task has finished
     * 
     * @author Phil
     * 
     */
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
		case 2:// show bandwith
		    if (!act.isFinishing()) {
			if (msg.obj != null) {

			    final float[] result = (float[]) msg.obj;
			    act.bandwith_used.setText(act.getString(R.string.utilise)
				    + Float.toString(result[1] - result[0]));

			    act.bandwith_max.setText(Float.toString(result[1]) + " "
				    + act.getString(R.string.gigaoctetx));

			    act.progess.setMax((int) result[1]);
			    act.progess.setProgress((int) result[0]);
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
    private TextView bandwith_used;
    private TextView bandwith_max;

    private void doLogin() {
	creds = new UserCredentials(PreferenceManager.getDefaultSharedPreferences(this));
	CharSequence text = "";
	boolean tag = false;
	int mColor = Color.RED;

	if (creds.isLoggedIn()) {
	    text = getString(R.string.logout);
	    tag = true;
	    navBar.showLoading();
	    new ProfileTask(this, handler).execute(creds);
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
	BugSenseHandler.sendEvent("Bandwith");
	appt_input.setEnabled(false);
	phase_input.setEnabled(false);

	final Editor edit = prefs.edit();
	creds.setPhase(phase);
	creds.setAppt(appt);
	edit.putString(UserCredentials.REZ, phase);
	edit.putString(UserCredentials.APPT, appt);
	edit.commit();

	// get bandwidth from cooptel, parse html, extract floats, etc etc
	new AsyncTask<String, Void, float[]>() {
	    final Pattern usageRegex = Pattern
		    .compile("<TR><TD>(.*)</TD><TD>(.*)</TD><TD ALIGN=\"RIGHT\">(.*)</TD><TD ALIGN=\"RIGHT\">(.*)</TD></TR>");
	    final Pattern quotaRegex = Pattern
		    .compile("<TR><TD>Quota permis pour la p&eacute;riode</TD><TD ALIGN=\"RIGHT\">(.*)</TD></TD></TR>");

	    @Override
	    protected float[] doInBackground(String... params) {
		final float[] result = new float[2];
		try {
		    final HttpGet get = new HttpGet(URI.create(String.format(
			    "http://www2.cooptel.qc.ca/services/temps/?mois=%d&cmd=Visualiser",
			    Calendar.getInstance().get(Calendar.MONTH) + 1)));
		    final BasicScheme scheme = new BasicScheme();
		    final Credentials credentials = new UsernamePasswordCredentials("ets-res"
			    + params[0] + "-" + params[1], "ets" + params[1]);
		    try {
			final Header h = scheme.authenticate(credentials, get);
			get.addHeader(h);
			final HttpClient client = new DefaultHttpClient();
			final HttpResponse re = client.execute(get);

			// if HTTP200
			if (re.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			    final String ent = EntityUtils.toString(re.getEntity());
			    final Matcher matcher = usageRegex.matcher(ent);

			    float total = 0;
			    // String[] usageResult = matcher.;
			    // parse all results
			    while (matcher.find()) {
				final Number upload = Float.parseFloat(matcher.group(3));
				final Number download = Float.parseFloat(matcher.group(4));

				total += upload.floatValue();
				total += download.floatValue();
			    }

			    final Matcher quotaResult = quotaRegex.matcher(ent);
			    float totalBandwithAvail = 0;
			    if (quotaResult.find()) {
				totalBandwithAvail = Float.parseFloat(quotaResult.group(1));
			    }
			    result[0] = total / 1024;
			    result[1] = totalBandwithAvail / 1024;

			}
		    } catch (final AuthenticationException e) {
			e.printStackTrace();
		    }

		} catch (final IOException e) {
		    e.printStackTrace();
		}

		return result;
	    }

	    @Override
	    protected void onPostExecute(float[] result) {
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
	bandwith_used = (TextView) findViewById(R.id.bandwith_used_lbl);
	bandwith_max = (TextView) findViewById(R.id.bandwith_max);
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
		if (s.length() >= 3) {
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
				new ProfileTask(view.getContext(), handler).execute(creds);
				break;

			    default:
				dialog.cancel();
				dialog.dismiss();
				break;
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
