package ca.etsmtl.applets.etsmobile;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
import ca.etsmtl.applets.etsmobile.dialogs.DatePickerDialogFragment;
import ca.etsmtl.applets.etsmobile.models.CalendarCell;
import ca.etsmtl.applets.etsmobile.models.CurrentCalendar;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.services.CalendarTaskWeek;
import ca.etsmtl.applets.etsmobile.views.CalendarEventsListView;
import ca.etsmtl.applets.etsmobile.views.CalendarTextView;
import ca.etsmtl.applets.etsmobile.views.NavBar;
import ca.etsmtl.applets.etsmobile.views.NumGridViewWeek;
import ca.etsmtl.applets.etsmobile.views.NumGridViewWeek.OnCellTouchListener;



public class ScheduleWeekActivity extends FragmentActivity {

	public UserCredentials creds;
	private DatePickerDialogFragment datePickerDialog;

	public static class CalendarTaskHandlerWeek extends Handler {
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
			Log.v("ScheduleWeekActivity","ScheduleWeekActivity: msg.what="+msg.what );
			switch (msg.what) {
			case CalendarTaskWeek.ON_POST_EXEC:
				if (act != null) {
					if (act.navBar != null) {
						act.navBar.hideLoading();
					}

					// update calendar
					if (retreivedSessions != null
							&& !retreivedSessions.isEmpty()) {
						ETSMobileApp.getInstance().setSessions(
								retreivedSessions);

						act.currentGridView.setSessions(retreivedSessions);
						act.currentGridView.setCurrentCell(null);
						act.currentCalendar.setChanged();
						act.currentCalendar.notifyObservers(act.currentCalendar
								.getCalendar());

						if (act.currentGridView != null
								&& act.currentGridView.getCurrentCell() != null) {
							act.currentGridView.getCurrentCell().addObserver(
									act.lst_cours);
							act.currentGridView.getCurrentCell().setChanged();
							act.currentGridView.getCurrentCell()
									.notifyObservers();
						}
					}
				}
				break;
			default:
				act.navBar.showLoading();
				break;
			}
		}
	}

	private void getCalendarICS() {
		// Handler mainHandler = new Handler(getMainLooper());
		// mainHandler.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// try {
		// InputStream fin;
		// AssetManager assetManager = getAssets();
		// Log.v("ScheduleActivity",
		// "ScheduleActivity: getCalendarICS: asset ="
		// + assetManager);
		// fin = (InputStream) assetManager.open("ete_2013.ics");
		// CalendarBuilder builder = new CalendarBuilder(
		// new TimeZoneRegistryImpl());
		// Calendar calendar = builder.build(fin);
		//
		// for (Iterator i = calendar.getComponents().iterator(); i
		// .hasNext();) {
		// Property property = (Property) i.next();
		// Log.v("ScheduleActivity",
		// "ScheduleActivity: getCalendarICS: calendar="
		// + property.getName() + " , "
		// + property.getValue());
		// }
		//
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ParserException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// });
	}

	private CurrentCalendar currentCalendar;
	private NumGridViewWeek currentGridView;
	private CalendarEventsListView lst_cours;
	private NavBar navBar;

	private final OnCellTouchListener mNumGridViewWeek_OnCellTouchListener = new OnCellTouchListener() {
		@Override
		public void onCellTouch(final NumGridViewWeek v, final int x, final int y) {
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

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view_week);
		
		creds = new UserCredentials(
				PreferenceManager.getDefaultSharedPreferences(this));
		// get data async
		handler = new CalendarTaskHandlerWeek(this);
		new CalendarTaskWeek(this, handler).execute(creds);
		getCalendarICS();

		// set the navigation bar
		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.setTitle(R.drawable.navbar_horaire_title);

		// set the gridview containing the day names
		final String[] day_names = getResources().getStringArray(
				R.array.day_names);

		final GridView grid = (GridView) findViewById(R.id.gridDayNames);
		grid.setAdapter(new ArrayAdapter<String>(this, R.layout.day_name,
				day_names));



		
		// set next and previous buttons
		final ImageButton btn_previous = (ImageButton) findViewById(R.id.btn_previous);
		final ImageButton btn_next = (ImageButton) findViewById(R.id.btn_next);

		btn_previous.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				currentCalendar.previousMonth();
			}
		});
		btn_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				currentCalendar.nextMonth();
			}
		});

		// set the calendar view
		currentGridView = (NumGridViewWeek) findViewById(R.id.calendar_view);
		currentGridView
				.setOnCellTouchListener(mNumGridViewWeek_OnCellTouchListener);

		// assignation des session déja en mémoire
		currentGridView.setSessions(ETSMobileApp.getInstance().getSessions());

		// Affiche le mois courant
		final CalendarTextView txtcalendar_title = (CalendarTextView) findViewById(R.id.calendar_title);
		
		
		// initialisation des observers
		currentCalendar = new CurrentCalendar();
		
		
		currentCalendar.addObserver(currentGridView);
		currentCalendar.addObserver(txtcalendar_title);
		
		//set DatePicker
		Date date =	currentCalendar.getCalendar().getTime();
		datePickerDialog = new DatePickerDialogFragment(ScheduleWeekActivity.this,0,mDateSetListener , date.getYear(), date.getMonth(), date.getDay());	 
		txtcalendar_title.setOnClickListener( new OnClickListener() {
					
				@Override
				public void onClick(View v) {
					datePickerDialog.show();
				}
		});
		currentCalendar.addObserver(datePickerDialog);
		currentCalendar.setChanged();
		currentCalendar.notifyObservers(currentCalendar.getCalendar());
		
		

		lst_cours = (CalendarEventsListView) findViewById(R.id.lst_cours);
		Log.v("ScheduleActivity","ScheduleActivity: lst_cours="+ lst_cours);
		if(currentGridView !=null){
			currentGridView.getCurrentCell().addObserver(lst_cours);
			currentGridView.getCurrentCell().setChanged();
			currentGridView.getCurrentCell().notifyObservers();
		}

		navBar.setRightButtonText(R.string.Ajourdhui);
		navBar.showRightButton();
		navBar.setRightButtonAction(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				currentGridView.getCurrentCell().deleteObservers();
				currentGridView.setCurrentCell(null);

				currentCalendar.setToday();

				currentGridView.getCurrentCell().addObserver(lst_cours);
				currentGridView.getCurrentCell().setChanged();
				currentGridView.getCurrentCell().notifyObservers();
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
		switch (item.getItemId()) {
		// case R.id.calendar_force_update:
		// new CalendarTask(this, handler).execute(creds);
		// break;

		default:
			break;
		}
		return true;
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener(){
		public void onDateSet(DatePicker datepicker, int year, int month, int day) {
			currentCalendar.setDate(year, month, day);
			currentCalendar.setChanged();
			
		}
    	
    };
}
