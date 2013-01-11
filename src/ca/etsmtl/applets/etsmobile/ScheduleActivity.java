package ca.etsmtl.applets.etsmobile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
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

    public ArrayList<Session> sessions;

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
	    switch (msg.what) {
	    case CalendarTask.ON_POST_EXEC:
		if (act != null) {
		    if (act.navBar != null) {
			act.navBar.hideLoading();
		    }

		    ArrayList<Session> obj = (ArrayList<Session>) msg.obj;
		    act.sessions = obj;
		    act.currentGridView.setSessions(obj);

		    act.currentGridView.setCurrentCell(null);

		    act.current.setChanged();
		    act.current.notifyObservers(act.current.getCalendar());
		    if (act.currentGridView != null && act.currentGridView.getCurrentCell() != null) {
			act.currentGridView.getCurrentCell().addObserver(act.lst_cours);
			act.currentGridView.getCurrentCell().setChanged();
			act.currentGridView.getCurrentCell().notifyObservers();
		    }

		}
		break;
	    default:
		act.navBar.showLoading();
		break;
	    }
	}
    }

    private CurrentCalendar current;
    private NumGridView currentGridView;
    private CalendarEventsListView lst_cours;
    private NavBar navBar;

    private final OnCellTouchListener mNumGridView_OnCellTouchListener = new OnCellTouchListener() {
	@Override
	public void onCellTouch(final NumGridView v, final int x, final int y) {
	    if (task.getStatus() != Status.RUNNING) {
		CalendarCell cell = v.getCell(x, y);
		cell.deleteObservers();

		if (cell.getDate().getMonth() == current.getCalendar().getTime().getMonth()
			&& cell.getDate().getYear() == current.getCalendar().getTime().getYear()) {

		    cell.addObserver(lst_cours);
		    cell.setChanged();
		    cell.notifyObservers();
		    v.setCurrentCell(cell);

		} else {
		    if (cell.getDate().before(current.getCalendar().getTime())) {

			current.previousMonth();
			cell = v.getCell(x, v.getmCellCountY() - 1);
			cell.addObserver(lst_cours);
			cell.setChanged();
			cell.notifyObservers();
			v.setCurrentCell(cell);

		    } else if (cell.getDate().after(current.getCalendar().getTime())) {

			current.nextMonth();
			cell = v.getCell(x, 0);
			cell.addObserver(lst_cours);
			cell.setChanged();
			cell.notifyObservers();
			v.setCurrentCell(cell);

		    }
		}

		v.invalidate();
	    }
	}
    };

    private AsyncTask<Object, Void, ArrayList<Session>> task;
    private UserCredentials userCredentials;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.calendar_view);

	userCredentials = new UserCredentials(PreferenceManager.getDefaultSharedPreferences(this));
	sessions = new ArrayList<Session>();

	if (savedInstanceState != null) {
	    onRestoreInstanceState(savedInstanceState);
	} else {
	    // get data async
	    task = new CalendarTask(new CalendarTaskHandler(this)).execute(userCredentials);
	}
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
		current.previousMonth();
	    }
	});
	btn_next.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(final View v) {
		current.nextMonth();
	    }
	});

	// set the calendar view
	currentGridView = (NumGridView) findViewById(R.id.calendar_view);
	currentGridView.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

	// Affiche le mois courant
	final CalendarTextView txtcalendar_title = (CalendarTextView) findViewById(R.id.calendar_title);

	current = new CurrentCalendar();
	// initialisation des observers

	current.addObserver(currentGridView);

	current.addObserver(txtcalendar_title);

	current.setChanged();
	current.notifyObservers(current.getCalendar());

	// Affiche la liste des évènements d'aujourd'hui
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

		current.setToday();

		currentGridView.getCurrentCell().addObserver(lst_cours);
		currentGridView.getCurrentCell().setChanged();
		currentGridView.getCurrentCell().notifyObservers();
	    }
	});

    }

    // @Override
    // protected void onRestoreInstanceState(Bundle savedInstanceState) {
    // super.onRestoreInstanceState(savedInstanceState);
    // Set<String> keys = savedInstanceState.keySet();
    // for (String string : keys) {
    // Session serializable = (Session)
    // savedInstanceState.getSerializable(string);
    // sessions.add(serializable);
    // }
    // saveSessionToFile();
    // }

    // private void saveSessionToFile() {
    // try {
    // FileOutputStream fos = openFileOutput(userCredentials.getUsername(),
    // Context.MODE_PRIVATE);
    // ObjectOutputStream os = new ObjectOutputStream(fos);
    // for (Session s : sessions) {
    // os.writeObject(s);
    // }
    // os.close();
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    // private void getFromFile() {
    // try {
    // FileInputStream fis = openFileInput(userCredentials.getUsername());
    // ObjectInputStream is = new ObjectInputStream(fis);
    // Session session = null;
    // while ((session = (Session) is.readObject()) != null) {
    // sessions.add(session);
    // }
    // is.close();
    // } catch (StreamCorruptedException e) {
    // e.printStackTrace();
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // } catch (ClassNotFoundException e) {
    // e.printStackTrace();
    // }
    //
    // }
}
