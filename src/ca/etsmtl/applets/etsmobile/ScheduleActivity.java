package ca.etsmtl.applets.etsmobile;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;
import ca.etsmtl.applets.etsmobile.models.CalendarCell;
import ca.etsmtl.applets.etsmobile.models.CurrentCalendar;
import ca.etsmtl.applets.etsmobile.models.JoursRemplaces;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.services.CalendarTask;
import ca.etsmtl.applets.etsmobile.tools.db.ActivityCalendarTableHelper;
import ca.etsmtl.applets.etsmobile.tools.db.ETSMobileOpenHelper;
import ca.etsmtl.applets.etsmobile.tools.db.JoursRemplaceTableHelper;
import ca.etsmtl.applets.etsmobile.tools.db.SessionTableHelper;
import ca.etsmtl.applets.etsmobile.views.CalendarEventsListView;
import ca.etsmtl.applets.etsmobile.views.CalendarTextView;
import ca.etsmtl.applets.etsmobile.views.NavBar;
import ca.etsmtl.applets.etsmobile.views.NumGridView;
import ca.etsmtl.applets.etsmobile.views.NumGridView.OnCellTouchListener;

public class ScheduleActivity extends Activity {

    public String creds;

    public static class CalendarTaskHandler extends Handler {
	private final WeakReference<ScheduleActivity> ref;

	public CalendarTaskHandler(final ScheduleActivity act) {
	    ref = new WeakReference<ScheduleActivity>(act);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(final Message msg) {
	    super.handleMessage(msg);
	    final ScheduleActivity act = ref.get();
	    final ArrayList<Session> s = (ArrayList<Session>) msg.obj;
	    switch (msg.what) {
	    case CalendarTask.ON_POST_EXEC:
		if (act != null) {
		    if (act.navBar != null) {
			act.navBar.hideLoading();
		    }
		    final ETSMobileOpenHelper helper = new ETSMobileOpenHelper(act);
		    final SQLiteDatabase db = helper.getWritableDatabase();

		    // save to sqlite
		    for (final Session session : s) {

			session.setUserId(act.creds);

			final ContentValues cv = session.getContentValues();
			final long session_id = db.insert(SessionTableHelper.TABLE_NAME, null, cv);
			session.setId(session_id);

			final List<JoursRemplaces> joursRemplacee = session.getJoursRemplaces();

			for (final JoursRemplaces joursRemplaces : joursRemplacee) {
			    joursRemplaces.setSessionId(session_id);
			    final long id = db.insert(JoursRemplaceTableHelper.TABLE_NAME, null,
				    joursRemplaces.getContentValues());
			    joursRemplaces.setId(id);
			}

			for (final Entry<String, List<ActivityCalendar>> a : session
				.getActivities().entrySet()) {
			    final List<ActivityCalendar> val = a.getValue();

			    for (final ActivityCalendar activityCalendar : val) {
				activityCalendar.setSessionId(session_id);
				final long id = db.insert(ActivityCalendarTableHelper.TABLE_NAME,
					null, activityCalendar.getContentValues());
				activityCalendar.setId(id);
			    }
			}
		    }
		    db.close();
		    helper.close();

		}
	    case CalendarTask.SHOW_DATA:
		// update calendar
		ETSMobileApp.getInstance().setSessions(s);

		act.currentGridView.setSessions(s);
		act.currentGridView.setCurrentCell(null);
		act.currentCalendar.setChanged();
		act.currentCalendar.notifyObservers(act.currentCalendar.getCalendar());

		if (act.currentGridView != null && act.currentGridView.getCurrentCell() != null) {
		    act.currentGridView.getCurrentCell().addObserver(act.lst_cours);
		    act.currentGridView.getCurrentCell().setChanged();
		    act.currentGridView.getCurrentCell().notifyObservers();
		}
		break;
	    default:
		act.navBar.showLoading();
		break;
	    }
	}
    }

    private CurrentCalendar currentCalendar;
    private NumGridView currentGridView;
    private CalendarEventsListView lst_cours;
    private NavBar navBar;

    private final OnCellTouchListener mNumGridView_OnCellTouchListener = new OnCellTouchListener() {
	@Override
	public void onCellTouch(final NumGridView v, final int x, final int y) {
	    // if (task.getStatus() != Status.RUNNING) {
	    CalendarCell cell = v.getCell(x, y);
	    cell.deleteObservers();

	    if (cell.getDate().getMonth() == currentCalendar.getCalendar().getTime().getMonth()
		    && cell.getDate().getYear() == currentCalendar.getCalendar().getTime()
			    .getYear()) {

		cell.addObserver(lst_cours);
		cell.setChanged();
		cell.notifyObservers();
		v.setCurrentCell(cell);

	    } else {
		if (cell.getDate().before(currentCalendar.getCalendar().getTime())) {

		    currentCalendar.previousMonth();
		    cell = v.getCell(x, v.getmCellCountY() - 1);
		    cell.addObserver(lst_cours);
		    cell.setChanged();
		    cell.notifyObservers();
		    v.setCurrentCell(cell);

		} else if (cell.getDate().after(currentCalendar.getCalendar().getTime())) {

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

    private AsyncTask<Object, Void, ArrayList<Session>> task;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.calendar_view);
	creds = new UserCredentials(PreferenceManager.getDefaultSharedPreferences(this))
		.getUsername();
	// get data async
	task = new CalendarTask(this, new CalendarTaskHandler(this)).execute(new UserCredentials(
		PreferenceManager.getDefaultSharedPreferences(this)));

	// set the navigation bar
	navBar = (NavBar) findViewById(R.id.navBar1);
	navBar.setTitle(R.drawable.navbar_horaire_title);

	// set the gridview containing the day names
	final String[] day_names = getResources().getStringArray(R.array.day_names);

	final GridView grid = (GridView) findViewById(R.id.gridDayNames);
	grid.setAdapter(new ArrayAdapter<String>(this, R.layout.day_name, day_names));

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
	currentGridView = (NumGridView) findViewById(R.id.calendar_view);
	currentGridView.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

	// assignation des session déja en mémoire
	currentGridView.setSessions(ETSMobileApp.getInstance().getSessions());

	// Affiche le mois courant
	final CalendarTextView txtcalendar_title = (CalendarTextView) findViewById(R.id.calendar_title);

	currentCalendar = new CurrentCalendar();
	// initialisation des observers

	currentCalendar.addObserver(currentGridView);
	currentCalendar.addObserver(txtcalendar_title);

	currentCalendar.setChanged();
	currentCalendar.notifyObservers(currentCalendar.getCalendar());

	lst_cours = (CalendarEventsListView) findViewById(R.id.lst_cours);
	currentGridView.getCurrentCell().addObserver(lst_cours);
	currentGridView.getCurrentCell().setChanged();
	currentGridView.getCurrentCell().notifyObservers();

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
}
