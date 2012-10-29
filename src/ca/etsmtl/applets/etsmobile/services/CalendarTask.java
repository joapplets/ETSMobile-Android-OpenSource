package ca.etsmtl.applets.etsmobile.services;

import java.util.ArrayList;
import java.util.Date;
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
		AsyncTask<Object, Void, ArrayList<ActivityCalendar>> {
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
			session = currentSession.getLongName();
		}
	}

	public static final int ON_POST_EXEC = 10;
	private static final String TAG = "CalendarTask";
	private final CalendarTaskHandler handler;
	private final int[] colors = new int[] { Color.RED, Color.BLUE,
			Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA };

	public CalendarTask(final CalendarTaskHandler handler) {
		this.handler = handler;
	}

	@Override
	protected ArrayList<ActivityCalendar> doInBackground(final Object... params) {
		onPreExecute();
		final ArrayList<Session> session = getSessions((UserCredentials) params[0]);
		Log.v(CalendarTask.TAG, "Nbr session" + session.size());
		// onProgressUpdate(null);
		final Session currentSession = findAndInitCurrentSession(session);
		Log.v(CalendarTask.TAG,
				"Current session" + currentSession.getLongName());

		if (currentSession != null) {
			// Contient les activitées (tp et cours d'un étudiant )
			final ArrayList<ActivityCalendar> arrayActivity = removeDuplicates(getCoursIntervalSession(
					(UserCredentials) params[0], currentSession));
			Log.v(CalendarTask.TAG, "Nbr d'activité " + arrayActivity.size());
			return arrayActivity;
		}
		return new ArrayList<ActivityCalendar>();
	}

	/**
	 * Trouve la session en cours parmis les sessions accessibles, initialise
	 * les paramêtre d'affichage de session
	 * 
	 * @param sessions
	 * @return
	 */
	private Session findAndInitCurrentSession(final ArrayList<Session> sessions) {
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
			final UserCredentials creds, final Session currentSession) {
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

	/**
	 * Obtien la liste des sessions acceccibles
	 * 
	 * @param creds
	 */
	private ArrayList<Session> getSessions(final UserCredentials creds) {
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

	@Override
	protected void onPostExecute(final ArrayList<ActivityCalendar> result) {
		super.onPostExecute(result);

		// set colors before notifying ui
		for (int i = 0; i < result.size(); i++) {
			result.get(i).setEventColor(colors[i]);
		}

		// Bundle data = new Bundle();
		final Message msg = handler.obtainMessage(CalendarTask.ON_POST_EXEC,
				result);
		// msg.setData(data);
		msg.sendToTarget();

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		handler.obtainMessage().sendToTarget();
	}

	private ArrayList<ActivityCalendar> removeDuplicates(
			final ArrayList<ActivityCalendar> activityList) {
		for (int i = 0; i < activityList.size(); i++) {
			for (int j = 0; j < activityList.size(); j++) {
				if (i == j) {
				} else if (activityList.get(j).getCours()
						.equals(activityList.get(i).getCours())
						&& activityList.get(j).getName()
								.equals(activityList.get(i).getName())) {
					activityList.get(i).setLocation(
							activityList.get(i).getLocation() + "/"
									+ activityList.get(j).getLocation());
					activityList.remove(j);
				}
			}
		}
		return activityList;
	}
}
