package ca.etsmtl.applets.etsmobile;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.services.ProfileTask;

public class ProfileActivity extends Activity implements OnClickListener,
		Observer, android.content.DialogInterface.OnClickListener {

	private static final int SHOW_LOGIN = 1;
	private Button btnLogin;
	protected StudentProfile profile;
	private final ObservableBundle bundle = new ObservableBundle();
	private Handler loginTaskHandler;
	private View view;
	private UserCredentials creds;

	/**
	 * Login dialog onClick
	 */
	@Override
	public void onClick(final DialogInterface dialog, final int which) {
		String codeP;
		String codeU;
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			codeP = ((TextView) view.findViewById(R.id.login_dialog_code_perm))
					.getText().toString();
			codeU = ((TextView) view
					.findViewById(R.id.login_dialog_code_univesel)).getText()
					.toString();
			new ProfileTask(bundle).execute(codeP, codeU);
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
			final Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
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
		view = getLayoutInflater().inflate(R.layout.login_dialog, null);
		btnLogin = (Button) findViewById(R.id.profile_login_btn);
		btnLogin.setOnClickListener(this);

		creds = new UserCredentials(
				PreferenceManager.getDefaultSharedPreferences(this));
		// final String codeP = prefs.getString("codeP", "");
		// final String codeU = prefs.getString("codeU", "");
		// handles callback from observable
		loginTaskHandler = new Handler();
		bundle.addObserver(this);

		CharSequence text;
		boolean tag;
		if (!creds.getUsername().equals("") && !creds.getPassword().equals("")) {
			new ProfileTask(bundle).execute(creds.getUsername(),
					creds.getPassword());
			text = getString(R.string.logout);
			tag = true;
		} else {
			text = getString(R.string.login);
			tag = false;
		}
		/**
		 * SEARCH NAV BAR TODO: Create custom View -> ActionBar
		 * */
		// home btn
		((ImageButton) findViewById(R.id.empty_nav_bar_home_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {
						finish();
					}
				});
		btnLogin.setText(text);
		btnLogin.setTag(tag);
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
			break;
		}
		return d;
	}

	/**
	 * TODO OPTIMISATION, SUXZ HARD
	 */
	@Override
	public void update(final Observable arg0, final Object arg1) {
		final ContentValues val = (ContentValues) arg1;
		if (val != null) {
			// show login error
			if (val.containsKey("erreur")
					&& val.getAsString("erreur").length() > 0) {
				loginTaskHandler.post(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(ProfileActivity.this)
								.setTitle("Erreur")
								.setMessage(val.getAsString("erreur")).create()
								.show();
					}
				});

			} else {
				// init layout
				final Context ctx = this;
				loginTaskHandler.post(new Runnable() {

					@Override
					public void run() {
						((TextView) findViewById(R.id.student_profile_name))
								.setText(val.getAsString("prenom").trim());
						((TextView) findViewById(R.id.student_profile_lastname))
								.setText(val.getAsString("nom").trim());
						((TextView) findViewById(R.id.student_profile_solde))
								.setText(val.getAsString("soldeTotal").trim());
						((TextView) findViewById(R.id.student_profile_codePermanent))
								.setText(val.getAsString("codePerm").trim());

						// save credentials to prefs
						final SharedPreferences prefs = PreferenceManager
								.getDefaultSharedPreferences(ctx);
						final Editor editor = prefs.edit();
						editor.putString("codeP", creds.getUsername());
						editor.putString("codeU", creds.getPassword());
						editor.commit();
						btnLogin.setTag(true);
						btnLogin.setText(getString(R.string.logout));
					}
				});

			}
		}
	}

}
