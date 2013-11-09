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
package ca.etsmtl.applets.etsmobile;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ScrollView;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class AboutActivity extends Activity {

	private ScrollView scrollView;

	private Handler handler;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.about);
		final NavBar navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.setTitle(R.drawable.navbar_title);
		navBar.hideLoading();
		navBar.hideRightButton();
		navBar.hideHome();
		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		handler = new Handler();
		final TimerTask scrollerSchedule = new TimerTask() {

			@Override
			public void run() {

				handler.post(new Runnable() {

					@Override
					public void run() {
						if (scrollView.getScrollY() < scrollView
								.getMaxScrollAmount() * 9) {
							scrollView.smoothScrollTo(0,
									scrollView.getScrollY() + 4);
						} else {
							scrollView.smoothScrollTo(0, 0);
						}
					}
				});
			}
		};
		final Timer t = new Timer();
		t.schedule(scrollerSchedule, 1000, 50);

	}
}
