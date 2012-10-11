package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.services.ProfileTask;

import com.etsmt.applets.etsmobile.views.NavBar;

public class ProfileActivity extends Activity implements OnClickListener,
		OnDismissListener, DialogInterface.OnClickListener {

	private static final int SHOW_LOGIN = 1;
	protected static final int LOGIN_ERROR = 0;
	private Button btnLogin;
	protected StudentProfile profile;
	private UserCredentials creds;
	private View view;
	// TODO: put static
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case ProfileTask.ON_POST_EXEC:
				final Bundle data = msg.getData();
				final StudentProfile studentProfile = (StudentProfile) data
						.get(ProfileTask.PROFILE_KEY);
				if (studentProfile == null) {
					showDialog(ProfileActivity.LOGIN_ERROR);
				} else {
					((TextView) findViewById(R.id.student_profile_name))
							.setText(studentProfile.getPrenom());
					((TextView) findViewById(R.id.student_profile_lastname))
							.setText(studentProfile.getNom());
					((TextView) findViewById(R.id.student_profile_solde))
							.setText(studentProfile.getSolde());
					((TextView) findViewById(R.id.student_profile_codePermanent))
							.setText(studentProfile.getCodePerm());

					// save credentials to prefs
					final SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					final Editor editor = prefs.edit();
					editor.putString("codeP", creds.getUsername());
					editor.putString("codeU", creds.getPassword());
					editor.commit();

					btnLogin.setTag(true);
					btnLogin.setText(getString(R.string.logout));
					navBar.hideLoading();
				}
				break;

			default:
				break;
			}
		}

	};
	private NavBar navBar;

	private void doLogin() {
		creds = new UserCredentials(
				PreferenceManager.getDefaultSharedPreferences(this));
		CharSequence text = "";
		boolean tag = false;
		if (!creds.getUsername().equals("") && !creds.getPassword().equals("")) {
			text = getString(R.string.logout);
			tag = true;
			navBar.showLoading();
			new ProfileTask(handler).execute(creds);
		} else {
			text = getString(R.string.login);
			tag = false;
		}
		btnLogin.setText(text);
		btnLogin.setTag(tag);
	}

	/**
	 * Login dialog onClick
	 */
	@Override
	public void onClick(final DialogInterface dialog, final int which) {
		String codeP;
		String codeU;
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			codeP = ((TextView) view.findViewById(R.id.login_dialog_mot_passe))
					.getText().toString();
			codeU = ((TextView) view
					.findViewById(R.id.login_dialog_code_univesel)).getText()
					.toString();
			final UserCredentials credentials = new UserCredentials(codeP,
					codeU);
			new ProfileTask(handler).execute(credentials);
			// dialog.dismiss();
			break;

		default:
			break;
		}
	}

	/**
	 * Login btn
	 */
	@Override
	public void onClick(final View view) {
		if (!(Boolean) btnLogin.getTag()) {
			showDialog(ProfileActivity.SHOW_LOGIN);
		} else {
			// remove credentials
			final Editor editor = PreferenceManager
					.getDefaultSharedPreferences(this).edit();
			editor.putString("codeP", "");
			editor.putString("codeU", "");
			editor.commit();
			btnLogin.setTag(false);
			btnLogin.setText(getString(R.string.login));

			((TextView) findViewById(R.id.student_profile_name)).setText("");
			((TextView) findViewById(R.id.student_profile_lastname))
					.setText("");
			((TextView) findViewById(R.id.student_profile_solde)).setText("");
			((TextView) findViewById(R.id.student_profile_codePermanent))
					.setText("");
		}
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_profile);

		btnLogin = (Button) findViewById(R.id.profile_login_btn);
		btnLogin.setOnClickListener(this);
		view = getLayoutInflater().inflate(R.layout.login_dialog, null);

		// NEW !!!
		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.setTitle(R.drawable.navbar_profil_title);
		navBar.hideRightButton();
		doLogin();
	}

	@Override
	protected Dialog onCreateDialog(final int id, final Bundle args) {
		Dialog d = super.onCreateDialog(id, args);
		switch (id) {
		case 1:
			d = new AlertDialog.Builder(this)
					.setTitle(getString(R.string.login_dialog_title))
					.setView(view).setPositiveButton("Ok", this).create();
			break;

		default:
			d = new AlertDialog.Builder(this).setTitle("Erreur")
					.setMessage("Erreur").create();
			break;
		}
		return d;
	}

	@Override
	public void onDismiss(final DialogInterface dialog) {
		doLogin();
	}

}
