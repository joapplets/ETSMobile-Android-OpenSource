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

import java.util.ArrayList;
import java.util.Collections;

import android.os.Handler;
import android.os.Message;
import ca.etsmtl.applets.etsmobile.ETSMobileApp;
import ca.etsmtl.applets.etsmobile.models.Session;

public class CalendarTaskWeek extends CalendarTask {

	public static final int ON_POST_EXEC = 11;

	public CalendarTaskWeek(final Handler handler) {
		super(handler);
	}

	@Override
	protected void onPostExecute(final ArrayList<Session> result) {
		super.onPostExecute(result);

		Collections.sort(result);
		ETSMobileApp.getInstance().saveSessionsToPrefs(result);
		// Bundle data = new Bundle();
		final Message msg = handler.obtainMessage(
				CalendarTaskWeek.ON_POST_EXEC, result);
		msg.sendToTarget();

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		handler.obtainMessage().sendToTarget();
	}
}
