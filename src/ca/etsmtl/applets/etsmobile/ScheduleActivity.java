package ca.etsmtl.applets.etsmobile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;
import ca.etsmtl.applets.etsmobile.models.CalendarCell;
import ca.etsmtl.applets.etsmobile.models.CalendarEvent;
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
		
		public ListeHorraireEtProf(UserCredentials cred,
				Session currentSession) {
			// TODO Auto-generated constructor stub
			password = cred.getPassword();
			username = cred.getUsername();
			session = currentSession.getLongName();	
		}
	}

	private CurrentCalendar current;
	private NumGridView mNumGridView1,mNumGridView2,mNumGridView3;
	private CalendarEventsListView lst_cours;
	private NavBar navBar;
	private ArrayList<Session> sessions;
	private Session currentSession;

	
	public void slideDown()
	{
	    mNumGridView2.setOnCellTouchListener(null);
		
		mNumGridView3.clearAnimation();
		mNumGridView3.setVisibility(View.GONE);
		TranslateAnimation slide;
		
		   
		slide = new TranslateAnimation(0, 0, mNumGridView1.getHeight()*-1.0f,0 );
		slide.setDuration(1000);   
		slide.setFillAfter(true);   
		mNumGridView1.startAnimation(slide);  
		mNumGridView1.setVisibility(View.VISIBLE);
		
		
		
		slide = new TranslateAnimation(0, 0, 0,mNumGridView2.getHeight()+mNumGridView2.getCellHeight());   
	    slide.setDuration(1000);   
	    slide.setFillAfter(true);   
	   
	  
	     
	    mNumGridView2.startAnimation(slide);  
	
	    mNumGridView2.setVisibility(View.GONE);
	   
	  
	}
	
	
	public void slideUp()
	{
		mNumGridView2.setOnCellTouchListener(null);
		
		mNumGridView1.clearAnimation();
		mNumGridView1.setVisibility(View.GONE);
		
		//ÔøΩviter d'avoir le OnCellTouchListener de la grille1 par dessus la grille 3 (superposition)
		mNumGridView1.layout(mNumGridView1.getLeft(), mNumGridView1.getTop()-mNumGridView1.getHeight(), mNumGridView1.getRight(), mNumGridView1.getBottom()-mNumGridView1.getHeight());
		
		
		   
		TranslateAnimation slide = new TranslateAnimation(0, 0, mNumGridView2.getHeight()+mNumGridView2.getCellHeight(),0 );   
		slide.setDuration(1000);   
		slide.setFillAfter(true);   
		mNumGridView3.startAnimation(slide);  
		    
		mNumGridView3.setVisibility(View.VISIBLE);
		   
		   
		slide = new TranslateAnimation(0, 0, 0,mNumGridView2.getHeight() * -1.0f );   
	    slide.setDuration(1000);   
	    slide.setFillAfter(true);   
	    
	  
	    mNumGridView2.startAnimation(slide);  

	    
	    mNumGridView2.setVisibility(View.GONE);
	    
	 
	}
	
	
	
	
	private final OnCellTouchListener mNumGridView_OnCellTouchListener = new OnCellTouchListener() {
		@Override
		public void onCellTouch(final NumGridView v, final int x, final int y) {
			CalendarCell cell = v.getCell(x, y);
			cell.deleteObservers();
			
			
			if(cell.getDate().getMonth() == current.getCalendar().getTime().getMonth() && 
					cell.getDate().getYear() == current.getCalendar().getTime().getYear())
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

					
					mNumGridView2.update(null, mNumGridView1.getCurrent().clone());
					mNumGridView2.setCurrentCell(x,y);
					mNumGridView2.invalidate();
					
					current.previousMonth();
					slideDown();
					
			
					cell = mNumGridView1.getCell(x, mNumGridView1.getmCellCountY()-1);
				
					cell.addObserver(lst_cours);
					cell.setChanged();
					cell.notifyObservers();
					mNumGridView1.setCurrentCell(cell);
					
					
				}
				else if(cell.getDate().after(current.getCalendar().getTime()))
				{
					
					mNumGridView2.update(null, mNumGridView3.getCurrent().clone());
					mNumGridView2.setCurrentCell(x,y);
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
	private ArrayAdapter<String> listeCoursAdapter;
	private ArrayList<Cours> cours;

	/**
	 * Trouve la session en cours parmis les sessions accessibles, initialise
	 * les paramÔøΩtre d'affichage de session
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
	 * Donne la liste des cours dans l'interval d'une session donn√©e
	 * 
	 * @return
	 */
	private ArrayList<ActivityCalendar> getCoursIntervalSession() {
		try {
			final ListeHorraireEtProf listeHoraireEtProf = new ListeHorraireEtProf(
					new UserCredentials(PreferenceManager
							.getDefaultSharedPreferences(this)),
					currentSession);
			final SignetBackgroundThread<ArrayList<ActivityCalendar>, ActivityCalendar> signetBackgroundThead = new SignetBackgroundThread<ArrayList<ActivityCalendar>, ActivityCalendar>(
					"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
					"listeHoraireEtProf", listeHoraireEtProf, ActivityCalendar.class,
					FetchType.ARRAY, "listeActivites");

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

		
		//set the calendar view
		mNumGridView1 = (NumGridView) findViewById(R.id.numgridview1);
		mNumGridView1.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

		mNumGridView2 = (NumGridView) findViewById(R.id.numgridview2);
		mNumGridView2.setOnCellTouchListener(mNumGridView_OnCellTouchListener);
		
		mNumGridView3 = (NumGridView) findViewById(R.id.numgridview3);
		mNumGridView3.setOnCellTouchListener(mNumGridView_OnCellTouchListener);

		//Affiche le mois courant
		final CalendarTextView txtcalendar_title = (CalendarTextView) findViewById(R.id.calendar_title);

		current = new CurrentCalendar();
		// initialisation des observers
	
		current.addObserver(mNumGridView1);
		current.addObserver(mNumGridView2);
		current.addObserver(mNumGridView3);
		current.addObserver(txtcalendar_title);

		current.setChanged();
		current.notifyObservers(current.getCalendar());
		
		//Affiche la liste des évènements d'aujourd'hui
		lst_cours = (CalendarEventsListView) findViewById(R.id.lst_cours);
		mNumGridView2.getCurrentCell().addObserver(lst_cours);
		
		mNumGridView2.getCurrentCell().setChanged();
		mNumGridView2.getCurrentCell().notifyObservers();
		
		
		mNumGridView2.setVisibility(View.VISIBLE);
		
		
		
		current.deleteObserver(mNumGridView2);
		
	
		
		/*
		getSessions();

		findAndInitCurrentSession();

		if (currentSession != null) {
			//Contient les activitÈes (tp et cours d'un Ètudiant )
			ArrayList<ActivityCalendar>  arrayActivity = removeDuplicates(  getCoursIntervalSession() );
			Log.v("Nbr d'activité", "Nbr d'activité "+arrayActivity.size());
		}
	
 	*/
	}

	public ArrayList<ActivityCalendar> removeDuplicates(ArrayList<ActivityCalendar> activityList)
	{
	  for ( int i = 0; i < activityList.size(); i++ ) {
	     for ( int j = 0; j < activityList.size(); j++ ){
	        if ( i == j ){
	        }
	        else if ( activityList.get( j ).getCours().equals( activityList.get( i ).getCours() ) && activityList.get( j ).getName().equals( activityList.get( i ).getName())){
	           activityList.get(i).setLocation(activityList.get(i).getLocation()+"/"+activityList.get(j).getLocation());
	           activityList.remove( j );
	        }
	     }
	 }
	  return activityList;
	}
}
