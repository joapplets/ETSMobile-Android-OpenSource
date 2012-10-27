package ca.etsmtl.applets.etsmobile.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import ca.etsmtl.applets.etsmobile.ScheduleActivity.CalendarTaskHandler;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;

import com.google.gson.annotations.SerializedName;

public class CalendarTask extends
		AsyncTask<Object, Void, ArrayList<Session>> {
	private class ListeHorraireEtProf {
		@SerializedName("motPasse")
		private final String password;

		@SerializedName("codeAccesUniversel")
		private final String username;

		@SerializedName("pSession")
		private final String session;

		public ListeHorraireEtProf(final UserCredentials cred,
				final Session currentSession) {
			// TODO Auto-generated constructor stub
			password = cred.getPassword();
			username = cred.getUsername();
			session = currentSession.getShortName();
		}
	}

	public static final int ON_POST_EXEC = 10;
	private static final String TAG = "CalendarTask";
	private CalendarTaskHandler handler;
	private int[] colors = new int[] {
			Color.RED, 
			Color.YELLOW, 
			Color.GREEN,
			Color.rgb(255, 0, 255), //fushia
			Color.rgb(0, 255, 255), // aqua
			Color.rgb(128, 0, 0), // maroon
			Color.rgb(0, 255, 0), // lime
			Color.rgb(0, 0, 128) //navy
			};

	
	public CalendarTask(CalendarTaskHandler handler) {
		this.handler = handler;
	}

	@Override
	protected ArrayList<Session> doInBackground(Object... params) {
		onPreExecute();
		ArrayList<Session> sessions = getSessions((UserCredentials) params[0]);
		
		for(Session s: sessions)
			s.setActivities(getCoursIntervalSession(
					(UserCredentials) params[0], s));	
			
		return sessions;
	}

	/**
	 * Obtien la liste des sessions acceccibles
	 * 
	 * @param creds
	 */
	private ArrayList<Session> getSessions(UserCredentials creds) {
		ArrayList<Session> sessions = new ArrayList<Session>();
		try {

			final SignetBackgroundThread<ArrayList<Session>, Session> signetBackgroundThead = new SignetBackgroundThread<ArrayList<Session>, Session>(
					"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
					"listeSessions", creds, Session.class, FetchType.ARRAY);

			signetBackgroundThead.execute();

			sessions = signetBackgroundThead.get();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} catch (final ExecutionException e) {
			e.printStackTrace();
		}

		return sessions;
	}

	/**
	 * Trouve la session en cours parmis les sessions accessibles, initialise
	 * les paramêtre d'affichage de session
	 * 
	 * @param sessions
	 * @return
	 */
	private Session findAndInitCurrentSession(ArrayList<Session> sessions) {
		for (final Session s : sessions) {
			final Date date = new Date();
			if (s.getDateDebut().before(date) && s.getDateFin().after(date)) {
				// Update UI with Dots for Debut Fin Session
				return s;
			}
		}
		return null;
	}

	/**
	 * Donne la liste des cours dans l'interval d'une session donnÃ©e
	 * 
	 * @param creds
	 * @param currentSession
	 * 
	 * @return
	 */
	private ArrayList<ActivityCalendar> getCoursIntervalSession(
			UserCredentials creds, Session currentSession) {
		try {
			final ListeHorraireEtProf listeHoraireEtProf = new ListeHorraireEtProf(
					creds, currentSession);
			final SignetBackgroundThread<ArrayList<ActivityCalendar>, ActivityCalendar> signetBackgroundThead = new SignetBackgroundThread<ArrayList<ActivityCalendar>, ActivityCalendar>(
					"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
					"listeHoraireEtProf", listeHoraireEtProf,
					ActivityCalendar.class, FetchType.ARRAY, "listeActivites");

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



	@Override
	protected void onPostExecute(ArrayList<Session> result) {
		super.onPostExecute(result);

		ArrayList<String> activites = new ArrayList<String>();
		
		for(Session s: result)
		{
			int color_index=0;
			activites.clear();
			s.removeDuplicates();
			
			
			// set colors before notifying ui
			for (int i = 0; i < s.getActivities().size(); i++) {
				
				if(activites.indexOf(s.getActivities().get(i).getCours()) != -1)
				{
					s.getActivities().get(i).setEventColor(
							s.getActivities().get(
									activites.indexOf(s.getActivities().get(i).getCours())
									).getEventColor()
							);
					activites.add(s.getActivities().get(i).getCours());
				}
				else
				{
					s.getActivities().get(i).setEventColor(colors[color_index]);
					color_index++;
					activites.add(s.getActivities().get(i).getCours());
				}
			}
		}
		
		Collections.sort(result);
		

		// Bundle data = new Bundle();
		Message msg = handler.obtainMessage(ON_POST_EXEC, result);
		// msg.setData(data);
		msg.sendToTarget();

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		handler.obtainMessage().sendToTarget();
	}
}
