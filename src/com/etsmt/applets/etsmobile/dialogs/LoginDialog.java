package com.etsmt.applets.etsmobile.dialogs;

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

public class LoginDialog extends AlertDialog {

	private TextView codeP;
	private TextView codeU;

	public LoginDialog(final Context context) {
		super(context);
	}

	protected LoginDialog(final Context context, final boolean cancelable,
			final OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_dialog);
		codeP = (TextView) findViewById(R.id.login_dialog_code_perm);
		codeU = (TextView) findViewById(R.id.login_dialog_code_univesel);

		setTitle(R.string.login_dialog_title);
		setButton("Ok", new OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				final SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(getContext());
				final Editor edit = prefs.edit();
				edit.putString(UserCredentials.CODE_P, (String) codeP.getText());
				edit.putString(UserCredentials.CODE_U, (String) codeU.getText());
				edit.commit();
				dismiss();
			}
		});

		setButton2("Cancel", new OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				codeP.setText("");
				codeU.setText("");

				cancel();

			}
		});
	}

}
