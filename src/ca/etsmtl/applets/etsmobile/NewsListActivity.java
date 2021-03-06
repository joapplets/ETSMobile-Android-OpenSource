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

import java.util.Calendar;
import java.util.TimeZone;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import ca.etsmtl.applets.etsmobile.fragments.NewsListFragment;
import ca.etsmtl.applets.etsmobile.listeners.NewsListSelectedItemListener;
import ca.etsmtl.applets.etsmobile.preferences.NewsListPreferences;
import ca.etsmtl.applets.etsmobile.receivers.NewsAlarmReceiver;
import ca.etsmtl.applets.etsmobile.services.NewsService;
import ca.etsmtl.applets.etsmobile.services.NewsService.NewsFetcherBinder;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class NewsListActivity extends FragmentActivity implements
		NewsListSelectedItemListener, OnClickListener {

	private class ManualFetcher extends
			AsyncTask<NewsFetcherBinder, Void, Void> {

		@Override
		protected Void doInBackground(final NewsFetcherBinder... params) {
			final NewsFetcherBinder binder = params[0];
			if (binder != null) {
				binder.startFetching();
			}
			return null;
		}

		@Override
		protected void onPostExecute(final Void result) {
			super.onPostExecute(result);
			navBar.hideLoading();
			try {
				unbindService(connection);
			} catch (final IllegalArgumentException e) {
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			navBar.showLoading();
		}

	}

	private final static String SERVICE = "ca.etsmtl.applets.etsmobile.services.NewsFetcher";

	private final ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(final ComponentName name,
				final IBinder service) {
			new ManualFetcher().execute((NewsFetcherBinder) service);
		}

		@Override
		public void onServiceDisconnected(final ComponentName name) {
		}
	};
	private NavBar navBar;

	private SharedPreferences prefs;

	private void connectToFetcherService() {
		final Intent i = new Intent(this, NewsService.class);
		if (!serviceIsRunning()) {
			startService(i);
			navBar.showLoading();
		}
		bindService(i, connection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.base_bar_source_btn:
			final Intent intent = new Intent(getApplicationContext(),
					NewsListPreferences.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(final Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.news_list_fragment);
		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.setTitle(R.drawable.navbar_news_title);
		navBar.hideLoading();
		// navBar.showRightButton();
		navBar.setRightButtonAction(this);

		prefs = getSharedPreferences("dbpref", Context.MODE_PRIVATE);
		setAlarm();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.news_list_menu, menu);
		return true;
	}

	@Override
	public void onItemClick(final View v) {

		// On cr�e un nouveau intent qui va nous permettre de lancer
		// la nouvelle activity

		final Intent intent = new Intent(getApplicationContext(),
				SingleNewsActivity.class);
		intent.putExtra("id", (Integer) v.getTag(R.string.viewholderidtag));

		// On lance l'intent qui va cr�er la nouvelle activity.
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.newsListMenuUpdate:
			connectToFetcherService();
			break;
		case R.id.newsListMenuPreferences:
			intent = new Intent(getApplicationContext(),
					NewsListPreferences.class);
			break;
		default:
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}

		return true;
	}

	@Override
	protected void onPause() {
		try {
			unbindService(connection);
		} catch (final IllegalArgumentException e) {
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		final NewsListFragment frag = (NewsListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.newsList_fragment);

		frag.getLoaderManager().restartLoader(NewsListFragment.ID, null, frag);

		if (prefs.getBoolean("isEmpty", true)) {
			connectToFetcherService();
		}
		super.onResume();
	}

	private boolean serviceIsRunning() {
		final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (final RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (service.service.getClassName().equals(NewsListActivity.SERVICE)) {
				return true;
			}
		}
		return false;
	}

	private void setAlarm() {
		final Intent toAlarm = new Intent(this, NewsAlarmReceiver.class);
		final PendingIntent toDownload = PendingIntent.getBroadcast(this, 0,
				toAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
		final AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		final Calendar updateTime = Calendar.getInstance();
		updateTime.setTimeZone(TimeZone.getTimeZone("GMT"));

		updateTime.set(Calendar.HOUR_OF_DAY, 6);
		updateTime.set(Calendar.MINUTE, 00);
		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				updateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				toDownload);

		updateTime.set(Calendar.HOUR_OF_DAY, 12);
		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				updateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				toDownload);

		updateTime.set(Calendar.HOUR_OF_DAY, 18);
		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				updateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				toDownload);
	}

}
