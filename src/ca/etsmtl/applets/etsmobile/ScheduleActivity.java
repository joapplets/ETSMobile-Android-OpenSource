package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import ca.etsmtl.applets.etsmobile.models.CalendarCell;
import ca.etsmtl.applets.etsmobile.models.CurrentCalendar;
import ca.etsmtl.applets.etsmobile.views.CalendarEventsListView;
import ca.etsmtl.applets.etsmobile.views.CalendarTextView;
import ca.etsmtl.applets.etsmobile.views.NavBar;
import ca.etsmtl.applets.etsmobile.views.NumGridView;
import ca.etsmtl.applets.etsmobile.views.NumGridView.OnCellTouchListener;

public class ScheduleActivity extends Activity {

	private CurrentCalendar current;
	private NumGridView mNumGridView1, mNumGridView2, mNumGridView3;
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

					mNumGridView2.update(null, mNumGridView1.getCurrent()
							.clone());
					mNumGridView2.setCurrentCell(x, y);
					mNumGridView2.invalidate();

					current.previousMonth();
					slideDown();

					cell = mNumGridView1.getCell(x,
							mNumGridView1.getmCellCountY() - 1);

					cell.addObserver(lst_cours);
					cell.setChanged();
					cell.notifyObservers();
					mNumGridView1.setCurrentCell(cell);

				} else if (cell.getDate()
						.after(current.getCalendar().getTime())) {

					mNumGridView2.update(null, mNumGridView3.getCurrent()
							.clone());
					mNumGridView2.setCurrentCell(x, y);
					mNumGridView2.invalidate();

					current.nextMonth();
					slideUp();

					cell = mNumGridView3.getCell(x, 0);

					cell.addObserver(lst_cours);
					cell.setChanged();
					cell.notifyObservers();
					mNumGridView3.setCurrentCell(cell);

				}
			}

			v.invalidate();

		}
	};

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view);

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

				mNumGridView2.update(null, mNumGridView1.getCurrent().clone());
				mNumGridView2.setCurrentCell(mNumGridView1.getCurrentCell());
				mNumGridView2.invalidate();

				current.previousMonth();
				slideDown();

			}
		});

		btn_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {

				mNumGridView2.update(null, mNumGridView3.getCurrent().clone());
				mNumGridView2.setCurrentCell(mNumGridView3.getCurrentCell());
				mNumGridView2.invalidate();

				current.nextMonth();
				slideUp();

			}
		});

		// set the calendar view
		mNumGridView1 = (NumGridView) findViewById(R.id.numgridview1);
		mNumGridView1.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

		mNumGridView2 = (NumGridView) findViewById(R.id.numgridview2);
		mNumGridView2.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

		mNumGridView3 = (NumGridView) findViewById(R.id.numgridview3);
		mNumGridView3.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

		// Affiche le mois courant
		final CalendarTextView txtcalendar_title = (CalendarTextView) findViewById(R.id.calendar_title);

		current = new CurrentCalendar();
		// initialisation des observers

		current.addObserver(mNumGridView1);
		current.addObserver(mNumGridView2);
		current.addObserver(mNumGridView3);
		current.addObserver(txtcalendar_title);

		current.setChanged();
		current.notifyObservers(current.getCalendar());

		// Affiche la liste des Žv�nements d'aujourd'hui
		lst_cours = (CalendarEventsListView) findViewById(R.id.lst_cours);
		mNumGridView2.getCurrentCell().addObserver(lst_cours);

		mNumGridView2.getCurrentCell().setChanged();
		mNumGridView2.getCurrentCell().notifyObservers();

		mNumGridView2.setVisibility(View.VISIBLE);

		current.deleteObserver(mNumGridView2);

		/*
		 * getSessions();
		 * 
		 * findAndInitCurrentSession();
		 * 
		 * if (currentSession != null) { cours = getCoursIntervalSession(); }
		 * cours.get(0);
		 */

	}

	public void slideDown() {
		mNumGridView2.setOnCellTouchListener(null);

		mNumGridView3.clearAnimation();
		mNumGridView3.setVisibility(View.GONE);
		TranslateAnimation slide;

		slide = new TranslateAnimation(0, 0, mNumGridView1.getHeight() * -1.0f,
				0);
		slide.setDuration(1000);
		slide.setFillAfter(true);
		mNumGridView1.startAnimation(slide);
		mNumGridView1.setVisibility(View.VISIBLE);

		slide = new TranslateAnimation(0, 0, 0, mNumGridView2.getHeight()
				+ mNumGridView2.getCellHeight());
		slide.setDuration(1000);
		slide.setFillAfter(true);

		mNumGridView2.startAnimation(slide);

		mNumGridView2.setVisibility(View.GONE);

	}

	public void slideUp() {
		mNumGridView2.setOnCellTouchListener(null);

		mNumGridView1.clearAnimation();
		mNumGridView1.setVisibility(View.GONE);

		// Žviter d'avoir le OnCellTouchListener de la grille1 par dessus la
		// grille 3 (superposition)
		mNumGridView1.layout(mNumGridView1.getLeft(), mNumGridView1.getTop()
				- mNumGridView1.getHeight(), mNumGridView1.getRight(),
				mNumGridView1.getBottom() - mNumGridView1.getHeight());

		TranslateAnimation slide = new TranslateAnimation(0, 0,
				mNumGridView2.getHeight() + mNumGridView2.getCellHeight(), 0);
		slide.setDuration(1000);
		slide.setFillAfter(true);
		mNumGridView3.startAnimation(slide);

		mNumGridView3.setVisibility(View.VISIBLE);

		slide = new TranslateAnimation(0, 0, 0, mNumGridView2.getHeight()
				* -1.0f);
		slide.setDuration(1000);
		slide.setFillAfter(true);

		mNumGridView2.startAnimation(slide);

		mNumGridView2.setVisibility(View.GONE);

	}

}
