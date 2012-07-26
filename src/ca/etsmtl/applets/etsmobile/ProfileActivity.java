package ca.etsmtl.applets.etsmobile;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.models.ObservableBundle;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.services.ProfileTask;

public class ProfileActivity extends Activity implements OnClickListener,
		Observer, android.content.DialogInterface.OnClickListener {

	private static final int SHOW_LOGIN = 1;
	private Button btnLogin;
	protected StudentProfile profile;
	private ObservableBundle bundle = new ObservableBundle();
	private Handler loginTaskHandler;
	private View view;
	private String codeU;
	private String codeP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_profile);
		view = getLayoutInflater().inflate(R.layout.login_dialog, null);
		btnLogin = (Button) findViewById(R.id.profile_login_btn);
		btnLogin.setOnClickListener(this);

		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		String codeP = prefs.getString("codeP", "");
		String codeU = prefs.getString("codeU", "");
		// handles callback from observable
		loginTaskHandler = new Handler();
		bundle.addObserver(this);

		CharSequence text;
		boolean tag;
		if (!codeP.equals("") && !codeU.equals("")) {
			new ProfileTask(bundle).execute(codeP, codeU);
			text = getString(R.string.logout);
			tag = true;
		} else {
			text = getString(R.string.login);
			tag = false;
		}

		btnLogin.setText(text);
		btnLogin.setTag(tag);
	}

	/**
	 * Login btn
	 */
	@Override
	public void onClick(View view) {
		if (!(Boolean) btnLogin.getTag()) {
			showDialog(SHOW_LOGIN);
		} else {
			// remove credentials
			Editor editor = getPreferences(MODE_PRIVATE).edit();
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
	protected Dialog onCreateDialog(int id, Bundle args) {
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
	public void update(Observable arg0, Object arg1) {
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
						Editor editor = getPreferences(MODE_PRIVATE).edit();
						editor.putString("codeP", codeP);
						editor.putString("codeU", codeU);
						editor.commit();
						btnLogin.setTag(true);
						btnLogin.setText(getString(R.string.logout));
					}
				});

			}
		}
	}

	/**
	 * Login dialog onClick
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
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

}
