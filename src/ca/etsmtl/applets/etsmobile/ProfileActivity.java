package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.services.ProfileTask;

public class ProfileActivity extends Activity implements OnClickListener {

	private Button btnLogin;
	protected StudentProfile profile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_profile);

		btnLogin = (Button) findViewById(R.id.profile_login_btn);
		btnLogin.setOnClickListener(this);

		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		String login = prefs.getString("login", "");
		String pwd = prefs.getString("pwd", "");

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					profile = msg.getData().getParcelable(
							ProfileTask.PROFILE_KEY);
					break;

				default:
					btnLogin.setText(getText(R.string.logout));
					break;
				}

			}
		};

		if (!login.equals("") && !pwd.equals("")) {
			new ProfileTask(handler, 0).execute(login, pwd);
		} else {

		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
