package ca.etsmtl.applets.etsmobile.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;

public class BandwithDialog extends AlertDialog {
	private TextView txtRez;
	private TextView txtAppt;

	public BandwithDialog(final Context context) {
		super(context);
	}

	protected BandwithDialog(final Context context, final boolean cancelable,
			final OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_dialog);
		txtRez = (TextView) findViewById(R.id.login_dialog_mot_passe);
		txtAppt = (TextView) findViewById(R.id.login_dialog_code_univesel);

		final TextView labelRez = (TextView) findViewById(R.id.textView1);
		final TextView labelAppt = (TextView) findViewById(R.id.textView2);
		labelRez.setText(R.string.bandwith_dialog_rez);
		labelAppt.setText(R.string.bandwith_dialog_appt);

		setTitle(R.string.login_dialog_title);
		setButton("Ok", new OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				final SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(getContext());
				final Editor edit = prefs.edit();
				edit.putString(UserCredentials.REZ, (String) txtRez.getText());
				edit.putString(UserCredentials.APPT, (String) txtAppt.getText());
				edit.commit();
				dismiss();
			}
		});

		setButton2("Cancel", new OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				txtRez.setText("");
				txtAppt.setText("");

				cancel();

			}
		});
	}

}
