package ca.etsmtl.applets.etsmobile.services;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;

public class ProfileTask extends
		AsyncTask<UserCredentials, String, StudentProfile> {

	public static final String PROFILE_KEY = "profile";
	public static final int ON_POST_EXEC = 0;
	public static final int ON_PRE_EXEC = 1;
	private final Handler handler;

	public ProfileTask(final Handler handler) {
		this.handler = handler;

	}

	@Override
	protected StudentProfile doInBackground(final UserCredentials... params) {
		onPreExecute();
		SignetBackgroundThread<StudentProfile, StudentProfile> signets = new SignetBackgroundThread<StudentProfile, StudentProfile>(
				"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
				"infoEtudiant", params[0], StudentProfile.class);
		StudentProfile profile = null;
		profile = signets.fetchObject();
		return profile;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		handler.obtainMessage(ON_PRE_EXEC).sendToTarget();
	}

	@Override
	protected void onPostExecute(final StudentProfile result) {
		super.onPostExecute(result);
		final Message msg = handler.obtainMessage(ProfileTask.ON_POST_EXEC);
		final Bundle data = new Bundle();
		data.putParcelable(ProfileTask.PROFILE_KEY, result);
		msg.setData(data);
		msg.sendToTarget();

	}

}