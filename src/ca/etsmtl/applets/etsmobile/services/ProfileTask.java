package ca.etsmtl.applets.etsmobile.services;

import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;

/**
 * Async Task; Retreive the Student Profile with {@link SignetBackgroundThread}
 * 
 * @author Phil
 * 
 */
public class ProfileTask extends AsyncTask<UserCredentials, String, StudentProfile> {

    public static final String PROFILE_KEY = "profile";
    public static final int ON_POST_EXEC = 0;
    public static final int ON_PRE_EXEC = 1;
    private final Handler handler;
    private final Context ctx;

    public ProfileTask(Context ctx, final Handler handler) {
	this.ctx = ctx;
	this.handler = handler;

    }

    @Override
    protected StudentProfile doInBackground(final UserCredentials... params) {
	onPreExecute();
	final SignetBackgroundThread<StudentProfile, StudentProfile> signets = new SignetBackgroundThread<StudentProfile, StudentProfile>(
		ctx.getString(R.string.ets_signets), "infoEtudiant", params[0],
		StudentProfile.class, FetchType.OBJECT);

	signets.execute();
	StudentProfile profile = null;
	try {
	    profile = signets.get();
	    // Log.d("TAG", profile.toString());
	} catch (final InterruptedException e) {
	    onCancelled();
	    e.printStackTrace();
	} catch (final ExecutionException e) {
	    onCancelled();
	    e.printStackTrace();
	}
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