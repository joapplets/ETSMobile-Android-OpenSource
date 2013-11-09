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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import ca.etsmtl.applets.etsmobile.models.CalendarCell;
import ca.etsmtl.applets.etsmobile.models.CurrentCalendar;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.services.CalendarTask;
import ca.etsmtl.applets.etsmobile.services.CalendarTaskWeek;
import ca.etsmtl.applets.etsmobile.views.CalendarEventsListView;
import ca.etsmtl.applets.etsmobile.views.CalendarTextView;
import ca.etsmtl.applets.etsmobile.views.NavBar;
import ca.etsmtl.applets.etsmobile.views.NumGridViewWeek;
import ca.etsmtl.applets.etsmobile.views.NumGridViewWeek.OnCellTouchListener;

import com.testflightapp.lib.TestFlight;

public class ScheduleWeekActivity extends FragmentActivity {

	private static final String TAG = "ScheduleWeekActivity";
	public UserCredentials creds;
	private DatePickerDialog datePickerDialog;

	public static class CalendarTaskHandlerWeek extends Handler {
		private static final String TAG = "CalendarTaskHandlerWeek";
		private final WeakReference<ScheduleWeekActivity> ref;

		public CalendarTaskHandlerWeek(final ScheduleWeekActivity act) {
			ref = new WeakReference<ScheduleWeekActivity>(act);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);

			final ScheduleWeekActivity act = ref.get();

			final ArrayList<Session> retreivedSessions = (ArrayList<Session>) msg.obj;

			if (act != null && !act.isFinishing()) {
				switch (msg.what) {
				case CalendarTaskWeek.ON_POST_EXEC:
					if (act.navBar != null) {
						act.navBar.hideLoading();
					}

					// update calendar
					if (retreivedSessions != null
							&& !retreivedSessions.isEmpty()) {

						ETSMobileApp.getInstance().saveSessionsToPrefs(
								retreivedSessions);

						act.currentGridView.setSessions(retreivedSessions);
						act.currentGridView.setCurrentCell(null);
						act.currentCalendar.setChanged();
						act.currentCalendar.notifyObservers(act.currentCalendar
								.getCalendar());

						if (act.currentGridView != null
								&& act.currentGridView.getCurrentCell() != null) {
							Log.d(TAG, "settingObservers");
							act.currentGridView.getCurrentCell().addObserver(
									act.lst_cours);
							act.currentGridView.getCurrentCell().setChanged();
							act.currentGridView.getCurrentCell()
									.notifyObservers();
						}
					}
					break;
				default:
					if (act.navBar != null) {
						act.navBar.showLoading();
					}
					break;
				}
			}
		}
	}

	private CurrentCalendar currentCalendar;
	private NumGridViewWeek currentGridView;
	private CalendarEventsListView lst_cours;
	private NavBar navBar;

	private final OnCellTouchListener mNumGridViewWeek_OnCellTouchListener = new OnCellTouchListener() {
		@Override
		public void onCellTouch(final NumGridViewWeek v, final int x,
				final int y) {
			// if (task.getStatus() != Status.RUNNING) {
			CalendarCell cell = v.getCell(x, y);
			cell.deleteObservers();
			Log.v("ScheduleWeekActivity", "ScheduleWeekActivity: onCellTouch");
			if (cell.getDate().getMonth() == currentCalendar.getCalendar()
					.getTime().getMonth()
					&& cell.getDate().getYear() == currentCalendar
							.getCalendar().getTime().getYear()) {

				cell.addObserver(lst_cours);
				cell.setChanged();
				cell.notifyObservers();
				v.setCurrentCell(cell);

			} else {
				if (cell.getDate().before(
						currentCalendar.getCalendar().getTime())) {

					currentCalendar.previousMonth();
					cell = v.getCell(x, v.getmCellCountY() - 1);
					cell.addObserver(lst_cours);
					cell.setChanged();
					cell.notifyObservers();
					v.setCurrentCell(cell);

				} else if (cell.getDate().after(
						currentCalendar.getCalendar().getTime())) {

					currentCalendar.nextMonth();
					cell = v.getCell(x, 0);
					cell.addObserver(lst_cours);
					cell.setChanged();
					cell.notifyObservers();
					v.setCurrentCell(cell);

				}
			}

			v.invalidate();
		}
		// }
	};
	private CalendarTaskHandlerWeek handler;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view_week);
		creds = new UserCredentials(
				PreferenceManager.getDefaultSharedPreferences(this));
		TestFlight.passCheckpoint(this.getClass().getName());
		// get data async
		handler = new CalendarTaskHandlerWeek(this);
		new CalendarTaskWeek(handler).execute(creds);
		// getCalendarICS();

		// set the navigation bar
		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.setTitle(R.drawable.navbar_horaire_title);

		// set the gridview containing the day names
		final String[] day_names = getResources().getStringArray(
				R.array.day_names_5_days_week);

		final GridView grid = (GridView) findViewById(R.id.gridDayNames);
		grid.setAdapter(new ArrayAdapter<String>(this, R.layout.day_name,
				day_names));

		// set next and previous buttons
		final ImageButton btn_previous = (ImageButton) findViewById(R.id.btn_previous);
		final ImageButton btn_next = (ImageButton) findViewById(R.id.btn_next);

		btn_previous.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				currentCalendar.previousWeek();
			}
		});
		btn_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				currentCalendar.nextWeek();
			}
		});

		// set the calendar view
		currentGridView = (NumGridViewWeek) findViewById(R.id.calendar_view);
		currentGridView
				.setOnCellTouchListener(mNumGridViewWeek_OnCellTouchListener);

		// assignation des session d�ja en m�moire
		currentGridView.setSessions(ETSMobileApp.getInstance()
				.getSessionsFromPrefs());

		// Affiche le mois courant
		final CalendarTextView txtcalendar_title = (CalendarTextView) findViewById(R.id.calendar_title);

		// initialisation des observers
		currentCalendar = new CurrentCalendar();

		currentCalendar.addObserver(currentGridView);
		currentCalendar.addObserver(txtcalendar_title);

		// set DatePicker
		// ScheduleWeekActivity.this, 0, mDateSetListener, i, j, k);
		txtcalendar_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerFragment(new WeakReference<ScheduleWeekActivity>(
						ScheduleWeekActivity.this)).show(
						getSupportFragmentManager(), "");
			}
		});
		// currentCalendar.addObserver(datePickerDialog);
		currentCalendar.setChanged();
		currentCalendar.notifyObservers(currentCalendar.getCalendar());

		lst_cours = (CalendarEventsListView) findViewById(R.id.lst_cours);
		Log.v("ScheduleActivity", "ScheduleActivity: lst_cours=" + lst_cours);
		if (currentGridView != null) {
			if (currentGridView.getCurrentCell() != null) {
				Log.v("ScheduleWeekActivity",
						"ScheduleWeekActivity: onCreate: currentGridView.getCurrentCell()!=null");
				currentGridView.getCurrentCell().addObserver(lst_cours);
				currentGridView.getCurrentCell().setChanged();
				currentGridView.getCurrentCell().notifyObservers();
			}
		}

		navBar.setRightButtonText(R.string.Ajourdhui);
		navBar.showRightButton();
		navBar.setRightButtonAction(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (currentGridView.getCurrentCell() != null) {
					currentGridView.getCurrentCell().deleteObservers();
					currentGridView.setCurrentCell(null);
				}

				currentCalendar.setToday();
				currentCalendar.setChanged();

				if (currentGridView.getCurrentCell() != null) {
					currentGridView.getCurrentCell().addObserver(lst_cours);
					currentGridView.getCurrentCell().setChanged();
					currentGridView.getCurrentCell().notifyObservers();
				} else {
					lst_cours = (CalendarEventsListView) findViewById(R.id.lst_cours);
					lst_cours.resetAdaper();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.calendar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.calendar_month_view:
			intent = new Intent(this, ScheduleActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			break;
		case R.id.calendar_force_update:
			new CalendarTask(handler).execute(creds);
			break;

		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
			finish();
		}
		return true;
	}

	private final DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker datepicker, int year, int month,
				int day) {
			currentCalendar.setDate(year, month, day);
			currentCalendar.setChanged();

		}

	};

	@SuppressLint("ValidFragment")
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		private final WeakReference<ScheduleWeekActivity> ref;

		public DatePickerFragment(
				WeakReference<ScheduleWeekActivity> weakReference) {
			this.ref = weakReference;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			ScheduleWeekActivity scheduleActivity = ref.get();
			scheduleActivity.getCurrentCalendar().setDate(year, month, day);
			scheduleActivity.getCurrentCalendar().setChanged();
		}
	}

	public CurrentCalendar getCurrentCalendar() {
		return currentCalendar;
	}
}
