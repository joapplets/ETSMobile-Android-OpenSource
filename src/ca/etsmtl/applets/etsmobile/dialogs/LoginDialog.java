/*******************************************************************************
 * Copyright 2013 Club ApplETS
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

		codeP = (TextView) findViewById(R.id.login_dialog_mot_passe);
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
