package ca.etsmtl.applets.etsmobile;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;
import ca.etsmtl.applets.etsmobile.models.CalendarCell;
import ca.etsmtl.applets.etsmobile.models.CurrentCalendar;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.services.CalendarTask;
import ca.etsmtl.applets.etsmobile.views.CalendarEventsListView;
import ca.etsmtl.applets.etsmobile.views.CalendarTextView;
import ca.etsmtl.applets.etsmobile.views.NavBar;
import ca.etsmtl.applets.etsmobile.views.NumGridView;
import ca.etsmtl.applets.etsmobile.views.NumGridView.OnCellTouchListener;

public class ScheduleActivity extends Activity {

	public static class CalendarTaskHandler extends Handler {
		private WeakReference<ScheduleActivity> ref;

		public CalendarTaskHandler(ScheduleActivity act) {
			ref = new WeakReference<ScheduleActivity>(act);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ScheduleActivity act = ref.get();
			switch (msg.what) {
			case CalendarTask.ON_POST_EXEC:
				act.navBar.hideLoading();
				
				
			
				//act.current
					//.setActivities((ArrayList<ActivityCalendar>) msg.obj);
				act.currentGridView.setSessions((ArrayList<Session>) msg.obj);
				act.nextGridView.setSessions((ArrayList<Session>) msg.obj);
				act.prevGridView.setSessions((ArrayList<Session>) msg.obj);
				
				act.current.setChanged();
				act.current.notifyObservers(act.current.getCalendar());
				act.current.deleteObserver(act.currentGridView);
				
				
				break;
			default:
				act.navBar.showLoading();
				break;
			}
		}
	}

	private CurrentCalendar current;
	private NumGridView prevGridView, currentGridView, nextGridView;
	private CalendarEventsListView lst_cours;
	private NavBar navBar;

	private final OnCellTouchListener mNumGridView_OnCellTouchListener = new OnCellTouchListener() {
		@Override
		public void onCellTouch(final NumGridView v, final int x, final int y) {
			CalendarCell cell = v.getCell(x, y);
			cell.deleteObservers();

			if (cell.getDate().getMonth() == current.getCalendar().getTime()
					.getMonth()
					&& cell.getDate().getYear() == current.getCalendar()
							.getTime().getYear()) {

				cell.addObserver(lst_cours);
				cell.setChanged();
				cell.notifyObservers();
				v.setCurrentCell(cell);

			} else {
				if (cell.getDate().before(current.getCalendar().getTime())) {

					currentGridView.update(null, 
							prevGridView.getCurrent().clone());
					currentGridView.setCurrentCell(x, y);
					currentGridView.invalidate();

					current.previousMonth();
					slideDown();

					cell = prevGridView.getCell(x,
							prevGridView.getmCellCountY() - 1);

					cell.addObserver(lst_cours);
					cell.setChanged();
					cell.notifyObservers();
					prevGridView.setCurrentCell(cell);

				} else if (cell.getDate()
						.after(current.getCalendar().getTime())) {

					
					currentGridView.update(null,
							nextGridView.getCurrent().clone());
					currentGridView.setCurrentCell(x, y);
					currentGridView.invalidate();

					current.nextMonth();
					slideUp();

					cell = nextGridView.getCell(x, 0);

					cell.addObserver(lst_cours);
					cell.setChanged();
					cell.notifyObservers();
					nextGridView.setCurrentCell(cell);

				}
			}

			v.invalidate();

		}
	};

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view);
		// get data
		new CalendarTask(new CalendarTaskHandler(this))
				.execute(new UserCredentials(PreferenceManager
						.getDefaultSharedPreferences(this)));

		// set the navigation bar
		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.setTitle(R.drawable.navbar_horaire_title);
		navBar.hideRightButton();

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

				//currentGridView.setActivities(current.getActivities());
				currentGridView.update(null, 
						prevGridView.getCurrent().clone());
				currentGridView.setCurrentCell(prevGridView.getCurrentCell());
				currentGridView.invalidate();

				current.previousMonth();
				slideDown();

			}
		});
		btn_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {

				//currentGridView.setActivities(current.getActivities());
				currentGridView.update(null,
						nextGridView.getCurrent().clone());
				currentGridView.setCurrentCell(nextGridView.getCurrentCell());
				currentGridView.invalidate();

				current.nextMonth();
				slideUp();

			}
		});

		// set the calendar view
		prevGridView = (NumGridView) findViewById(R.id.numgridview1);
		prevGridView.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

		currentGridView = (NumGridView) findViewById(R.id.numgridview2);
		currentGridView
				.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

		nextGridView = (NumGridView) findViewById(R.id.numgridview3);
		nextGridView.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

		// Affiche le mois courant
		final CalendarTextView txtcalendar_title = (CalendarTextView) findViewById(R.id.calendar_title);

		current = new CurrentCalendar();
		// initialisation des observers

		current.addObserver(prevGridView);
		current.addObserver(currentGridView);
		current.addObserver(nextGridView);
		current.addObserver(txtcalendar_title);

		current.setChanged();
		current.notifyObservers(current.getCalendar());

		// Affiche la liste des évènements d'aujourd'hui
		lst_cours = (CalendarEventsListView) findViewById(R.id.lst_cours);
		currentGridView.getCurrentCell().addObserver(lst_cours);

		currentGridView.getCurrentCell().setChanged();
		currentGridView.getCurrentCell().notifyObservers();

		currentGridView.setVisibility(View.VISIBLE);

		
	
	}

	/**
	 * Prev Month animation
	 */
	public void slideDown() {
		currentGridView.setOnCellTouchListener(null);

		nextGridView.clearAnimation();
		nextGridView.setVisibility(View.GONE);
		TranslateAnimation slide;

		slide = new TranslateAnimation(0, 0, prevGridView.getHeight() * -1.0f,
				0);
		slide.setDuration(1000);
		slide.setFillAfter(true);
		prevGridView.startAnimation(slide);
		prevGridView.setVisibility(View.VISIBLE);

		slide = new TranslateAnimation(0, 0, 0, currentGridView.getHeight()
				+ currentGridView.getCellHeight());
		slide.setDuration(1000);
		slide.setFillAfter(true);

		currentGridView.startAnimation(slide);

		currentGridView.setVisibility(View.GONE);

	}

	/**
	 * Next Month animation
	 */
	public void slideUp() {
		currentGridView.setOnCellTouchListener(null);

		prevGridView.clearAnimation();
		prevGridView.setVisibility(View.GONE);

		// éviter d'avoir le OnCellTouchListener de la grille1 par dessus la
		// grille 3 (superposition)
		prevGridView.layout(prevGridView.getLeft(), prevGridView.getTop()
				- prevGridView.getHeight(), prevGridView.getRight(),
				prevGridView.getBottom() - prevGridView.getHeight());

		TranslateAnimation slide = new TranslateAnimation(0, 0,
				currentGridView.getHeight() + currentGridView.getCellHeight(),
				0);
		slide.setDuration(1000);
		slide.setFillAfter(true);
		nextGridView.startAnimation(slide);

		nextGridView.setVisibility(View.VISIBLE);

		slide = new TranslateAnimation(0, 0, 0, currentGridView.getHeight()
				* -1.0f);
		slide.setDuration(1000);
		slide.setFillAfter(true);

		currentGridView.startAnimation(slide);

		currentGridView.setVisibility(View.GONE);

	}
}
