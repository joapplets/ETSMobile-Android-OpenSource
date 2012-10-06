package ca.etsmtl.applets.etsmobile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.models.CalendarCell;
import ca.etsmtl.applets.etsmobile.models.Cours;
import ca.etsmtl.applets.etsmobile.models.CurrentCalendar;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.views.CalendarEventsListView;
import ca.etsmtl.applets.etsmobile.views.CalendarTextView;
import ca.etsmtl.applets.etsmobile.views.NavBar;
import ca.etsmtl.applets.etsmobile.views.NumGridView;
import ca.etsmtl.applets.etsmobile.views.NumGridView.OnCellTouchListener;

import com.google.gson.annotations.SerializedName;

public class ScheduleActivity extends Activity {

	private class IntervalSession {
		@SerializedName("motPasse")
		private final String password;

		@SerializedName("codeAccesUniversel")
		private final String username;

		@SerializedName("SesDebut")
		private final String debut;

		@SerializedName("SesFin")
		private final String fin;

		public IntervalSession(final UserCredentials creds,
				final Session pSession) {
			debut = pSession.getDateDebutString();
			fin = pSession.getDateFinString();
			password = creds.getPassword();
			username = creds.getUsername();
		}
	}

	private class ListeHorraireEtProf {
		@SerializedName("motPasse")
		private String password;

		@SerializedName("codeAccesUniversel")
		private String username;

		@SerializedName("pSession")
		private String session;
	}

	private CurrentCalendar current;
	private NumGridView mNumGridView;
	private CalendarEventsListView lst_cours;
	private NavBar navBar;
	private ArrayList<Session> sessions;
	private Session currentSession;

	private final OnCellTouchListener mNumGridView_OnCellTouchListener = new OnCellTouchListener() {
		@Override
		public void onCellTouch(final NumGridView v, final int x, final int y) {
			CalendarCell cell = v.getCell(x, y);
			cell.deleteObservers();
			
			
			if(cell.getDate().getMonth() == current.getCalendar().get(Calendar.MONTH))
			{
				
				cell.addObserver(lst_cours);
				cell.setChanged();
				cell.notifyObservers();
				v.setCurrentCell(cell);
				
			}
			else
			{
				if(cell.getDate().before(current.getCalendar().getTime()))
				{
					current.previousMonth();
					cell = v.getCell(x, v.getmCellCountY()-1);
					cell.addObserver(lst_cours);
					cell.setChanged();
					cell.notifyObservers();
					v.setCurrentCell(cell);
				}
				else if(cell.getDate().after(current.getCalendar().getTime()))
				{
					current.nextMonth();
					cell = v.getCell( x, 0);
					cell.addObserver(lst_cours);
					cell.setChanged();
					cell.notifyObservers();
					v.setCurrentCell(cell);
				
				}
			}
			
			v.invalidate();

		}
	};
	private ArrayAdapter<String> listeCoursAdapter;
	private ArrayList<Cours> cours;

	/**
	 * Trouve la session en cours parmis les sessions accessibles, initialise
	 * les paramÍtre d'affichage de session
	 */
	private void findAndInitCurrentSession() {
		for (final Session s : sessions) {
			final Date date = new Date();
			if (s.getDateDebut().before(date) && s.getDateFin().after(date)) {
				// Update UI with Dots for Debut Fin Session
				currentSession = s;
				break;
			}
		}

		// current.

	}

	/**
	 * Donne la liste des cours dans l'interval d'une session donnÈe
	 * 
	 * @return
	 */
	private ArrayList<Cours> getCoursIntervalSession() {
		try {
			final IntervalSession interval = new IntervalSession(
					new UserCredentials(PreferenceManager
							.getDefaultSharedPreferences(this)),
					currentSession);
			final SignetBackgroundThread<ArrayList<Cours>, Cours> signetBackgroundThead = new SignetBackgroundThread<ArrayList<Cours>, Cours>(
					"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
					"listeCoursIntervalleSessions", interval, Cours.class,
					FetchType.ARRAY);

			signetBackgroundThead.execute();

			return signetBackgroundThead.get();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Obtien la liste des sessions acceccibles
	 */
	private void getSessions() {

		try {

			final SignetBackgroundThread<ArrayList<Session>, Session> signetBackgroundThead = new SignetBackgroundThread<ArrayList<Session>, Session>(
					"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
					"listeSessions", new UserCredentials(PreferenceManager
							.getDefaultSharedPreferences(this)), Session.class,
					FetchType.ARRAY);

			signetBackgroundThead.execute();

			sessions = signetBackgroundThead.get();
		} catch (final InterruptedException e) {
			sessions = new ArrayList<Session>();
			e.printStackTrace();
		} catch (final ExecutionException e) {
			sessions = new ArrayList<Session>();
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view);

		
		//set the navigation bar
		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.setTitle(R.drawable.navbar_horaire_title);
		navBar.hideRightButton();

		//set the gridview containing the day names
		final String[] day_names = getResources().getStringArray(
				R.array.day_names);
	
		final GridView grid = (GridView) findViewById(R.id.gridDayNames);
		grid.setAdapter(new ArrayAdapter<String>(this, R.layout.day_name,
				day_names));

		
		//set next and previous buttons
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

		
		//set the calendar view
		mNumGridView = (NumGridView) findViewById(R.id.numgridview);
		mNumGridView.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

		//Affiche le mois courant
		final CalendarTextView txtcalendar_title = (CalendarTextView) findViewById(R.id.calendar_title);

		current = new CurrentCalendar();
		// initialisation des observers
		current.addObserver(mNumGridView);
		current.addObserver(txtcalendar_title);

		current.setChanged();
		current.notifyObservers(current.getCalendar());
		
		//Affiche la liste des évènements d'aoujourd'hui
		lst_cours = (CalendarEventsListView) findViewById(R.id.lst_cours);
		mNumGridView.getCurrentCell().addObserver(lst_cours);
		
		mNumGridView.getCurrentCell().setChanged();
		mNumGridView.getCurrentCell().notifyObservers();
		
		getSessions();

		findAndInitCurrentSession();

		if (currentSession != null) {
			cours = getCoursIntervalSession();
		}
//		cours.get(0);
	}



}
