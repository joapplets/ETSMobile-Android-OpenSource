package ca.etsmtl.applets.etsmobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.ctrls.CalendarTextView;
import ca.etsmtl.applets.etsmobile.models.Cours;
import ca.etsmtl.applets.etsmobile.models.CurrentCalendar;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.views.NumGridView;
import ca.etsmtl.applets.etsmobile.views.NumGridView.OnCellTouchListener;

import com.etsmt.applets.etsmobile.views.NavBar;
import com.google.gson.annotations.SerializedName;

public class ScheduleActivity extends Activity {

	private class ListeHorraireEtProf {
		@SerializedName("motPasse")
		private String password;

		@SerializedName("codeAccesUniversel")
		private String username;

		@SerializedName("pSession")
		private String session;

		public ListeHorraireEtProf(UserCredentials creds, String pSession) {
			session = pSession;
			password = creds.getPassword();
			username = creds.getUsername();
		}
	}

	private class IntervalSession {
		@SerializedName("motPasse")
		private String password;

		@SerializedName("codeAccesUniversel")
		private String username;

		@SerializedName("SesDebut")
		private String debut;

		@SerializedName("SesFin")
		private String fin;

		public IntervalSession(UserCredentials creds, Session pSession) {
			debut = pSession.getDateDebutString();
			fin = pSession.getDateFinString();
			password = creds.getPassword();
			username = creds.getUsername();
		}
	}

	private CurrentCalendar current;
	private NumGridView mNumGridView;
	private NavBar navBar;
	private ListeHorraireEtProf creds;
	private ArrayList<Session> sessions;
	private Session currentSession;

	private OnCellTouchListener mNumGridView_OnCellTouchListener = new OnCellTouchListener() {
		@Override
		public void onCellTouch(NumGridView v, int x, int y) {
			v.setSelectedCell(new Point(x, y));

			// startAnimationPopOut();
			v.invalidate();

		}
	};
	private ArrayAdapter<String> listeCoursAdapter;
	private ArrayList<Cours> cours;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view);

		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.hideRightButton();

		final String[] day_names = getResources().getStringArray(
				R.array.day_names);
		// grille des jours
		final GridView grid = (GridView) findViewById(R.id.gridDayNames);
		grid.setAdapter(new ArrayAdapter<String>(this, R.layout.day_name,
				day_names));

		this.current = new CurrentCalendar();

		// bouton des mois
		ImageButton btn_previous = (ImageButton) findViewById(R.id.btn_previous);
		ImageButton btn_next = (ImageButton) findViewById(R.id.btn_next);
		btn_previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				current.previousMonth();

			}
		});
		btn_next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				current.nextMonth();

			}
		});

		mNumGridView = (NumGridView) findViewById(R.id.numgridview);
		mNumGridView.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

		CalendarTextView txtcalendar_title = (CalendarTextView) findViewById(R.id.calendar_title);

		ListView lst_cours = (ListView) findViewById(R.id.lst_cours);

		String[] cours_names_examples = { "Cours 1", "Cours 2" };
		listeCoursAdapter = new ArrayAdapter<String>(this, R.layout.day_name,
				cours_names_examples);
		lst_cours.setAdapter(listeCoursAdapter);

		// initialisation par défaut du calendrier
		this.current.addObserver(mNumGridView);
		this.current.addObserver(txtcalendar_title);

		this.current.setChanged();
		this.current.notifyObservers(this.current.getCalendar());

		getSessions();

		findAndInitCurrentSession();

		if (currentSession != null) {
			cours = getCoursIntervalSession();
		}
		Cours c = cours.get(0);
	}

	/**
	 * Donne la liste des cours dans l'interval d'une session donnée
	 * @return 
	 */
	private ArrayList<Cours> getCoursIntervalSession() {
		try {
			IntervalSession interval = new IntervalSession(new UserCredentials(
					PreferenceManager.getDefaultSharedPreferences(this)),
					currentSession);
			final SignetBackgroundThread<ArrayList<Cours>, Cours> signetBackgroundThead = new SignetBackgroundThread<ArrayList<Cours>, Cours>(
					"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
					"listeCoursIntervalleSessions", interval, Cours.class,
					FetchType.ARRAY);

			signetBackgroundThead.execute();

			return signetBackgroundThead.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Trouve la session en cours parmis les sessions accessibles, initialise
	 * les paramêtre d'affichage de session
	 */
	private void findAndInitCurrentSession() {
		for (Session s : sessions) {
			Date date = new Date();
			if (s.getDateDebut().before(date) && s.getDateFin().after(date)) {
				//Update UI with Dots for Debut Fin Session
				currentSession = s;
				break;
			}
		}

		// current.

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
		} catch (InterruptedException e) {
			sessions = new ArrayList<Session>();
			e.printStackTrace();
		} catch (ExecutionException e) {
			sessions = new ArrayList<Session>();
			e.printStackTrace();
		}
	}

	/* animation */
	/*
	 * private void startAnimationPopOut() { NumGridView myLayout =
	 * (NumGridView) findViewById(R.id.numgridview);
	 * 
	 * Animation animation =
	 * AnimationUtils.loadAnimation(this,R.anim.bottom_out);
	 * 
	 * animation.setAnimationListener(new AnimationListener() {
	 * 
	 * @Override public void onAnimationStart(Animation animation) {
	 * 
	 * }
	 * 
	 * @Override public void onAnimationRepeat(Animation animation) {
	 * 
	 * }
	 * 
	 * @Override public void onAnimationEnd(Animation animation) {
	 * 
	 * } });
	 * 
	 * myLayout.clearAnimation(); myLayout.startAnimation(animation);
	 * 
	 * }
	 */

}
