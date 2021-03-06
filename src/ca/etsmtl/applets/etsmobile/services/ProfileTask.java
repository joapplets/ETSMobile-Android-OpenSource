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
package ca.etsmtl.applets.etsmobile.services;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.models.StudentPrograms;
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
		StudentProfile profile = null;
		List<StudentPrograms> programms = null;

		final SignetBackgroundThread<StudentProfile, StudentProfile> signets = new SignetBackgroundThread<StudentProfile, StudentProfile>(
				"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
				"infoEtudiant", params[0], StudentProfile.class);
		final SignetBackgroundThread<List<StudentPrograms>, StudentPrograms> signetsListPrograms = new SignetBackgroundThread<List<StudentPrograms>, StudentPrograms>(
				"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
				"listeProgrammes", params[0], StudentPrograms.class);

		profile = signets.fetchObject();
		programms = signetsListPrograms.fetchArray();

		if (profile != null) {
			profile.setStudentPrograms(programms);
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
