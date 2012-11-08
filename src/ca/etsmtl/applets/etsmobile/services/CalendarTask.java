package ca.etsmtl.applets.etsmobile.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Message;
import ca.etsmtl.applets.etsmobile.ScheduleActivity.CalendarTaskHandler;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;

import com.google.gson.annotations.SerializedName;

public class CalendarTask extends AsyncTask<Object, Void, ArrayList<Session>> {
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
	private final CalendarTaskHandler handler;

	public CalendarTask(final CalendarTaskHandler handler) {
		this.handler = handler;

	}

	@Override
	protected ArrayList<Session> doInBackground(final Object... params) {
		onPreExecute();
		final ArrayList<Session> sessions = getSessions((UserCredentials) params[0]);

		for (final Session s : sessions) {
			s.setActivities(getCoursIntervalSession(
					(UserCredentials) params[0], s));
		}

		return sessions;
	}

	/**
	 * Donne la liste des cours dans l'interval d'une session donnée
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
	protected void onPostExecute(final ArrayList<Session> result) {
		super.onPostExecute(result);

		final ArrayList<String> activites = new ArrayList<String>();

		for (final Session s : result) {
			int color_index = 0;
			activites.clear();
			s.removeDuplicates();

			// set colors before notifying ui
			for (int i = 0; i < s.getActivities().size(); i++) {

				if (activites.indexOf(s.getActivities().get(i).getCours()) != -1) {
					s.getActivities()
							.get(i)
							.setDrawableResId(
									s.getActivities()
											.get(activites.indexOf(s
													.getActivities().get(i)
													.getCours()))
											.getDrawableResId());
					activites.add(s.getActivities().get(i).getCours());
				} else {
					s.getActivities().get(i).setDrawableResId(color_index++);
					activites.add(s.getActivities().get(i).getCours());
				}
			}
		}

		Collections.sort(result);

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
}
