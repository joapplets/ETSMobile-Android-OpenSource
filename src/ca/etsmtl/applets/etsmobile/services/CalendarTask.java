package ca.etsmtl.applets.etsmobile.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Message;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.ScheduleActivity.CalendarTaskHandler;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applets.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;
import ca.etsmtl.applets.etsmobile.models.JoursRemplaces;
import ca.etsmtl.applets.etsmobile.models.Session;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.providers.ETSMobileContentProvider;
import ca.etsmtl.applets.etsmobile.tools.db.SessionTableHelper;

import com.google.gson.annotations.SerializedName;

public class CalendarTask extends AsyncTask<Object, Void, ArrayList<Session>> {
    private class ListeHorraireEtProf {
	@SerializedName("motPasse")
	private final String password;

	@SerializedName("codeAccesUniversel")
	private final String username;

	@SerializedName("pSession")
	private final String session;

	public ListeHorraireEtProf(final UserCredentials cred, final Session currentSession) {
	    password = cred.getPassword();
	    username = cred.getUsername();
	    session = currentSession.getShortName();
	}
    }

    private class LireJoursRemplaces {

	@SerializedName("pSession")
	private final String session;

	public LireJoursRemplaces(final Session currentSession) {
	    session = currentSession.getShortName();
	}
    }

    public static final int ON_POST_EXEC = 10;
    private final CalendarTaskHandler handler;

    private final static int[] dots = new int[] { R.drawable.kal_marker_red,
	    R.drawable.kal_marker_fuchsia, R.drawable.kal_marker_green, R.drawable.kal_marker_lime,
	    R.drawable.kal_marker_maroon, R.drawable.kal_marker_navy, R.drawable.kal_marker_aqua,
	    R.drawable.kal_marker_yellow, R.drawable.kal_marker_black };
    public static final int SHOW_DATA = 11;
    private final Context ctx;
    private final ContentResolver cv;

    public CalendarTask(final Context ctx, final CalendarTaskHandler handler) {
	this.ctx = ctx;
	this.cv = this.ctx.getContentResolver();
	this.handler = handler;

    }

    @Override
    protected ArrayList<Session> doInBackground(final Object... params) {
	onPreExecute();
	final UserCredentials creds = (UserCredentials) params[0];

	// get session from sqlite with user_id=Creds.Username as param
	final Cursor mSessionCursor = cv.query(ETSMobileContentProvider.CONTENT_URI_SESSION, null,
		SessionTableHelper.SESSIONS_USER_ID + "=?", new String[] { creds.getUsername() },
		null);
	final ArrayList<Session> mySessions = new ArrayList<Session>();
	while (mSessionCursor.moveToNext()) {
	    final Session session = new Session(mSessionCursor);

	    // get Jours remplacés
	    final Cursor mJourCursor = cv.query(
		    ETSMobileContentProvider.CONTENT_URI_JOURS_REMPLACE, null, null, null, null);
	    final ArrayList<JoursRemplaces> joursRemplaces = new ArrayList<JoursRemplaces>();
	    while (mJourCursor.moveToNext()) {
		final JoursRemplaces j = new JoursRemplaces(mJourCursor);
		joursRemplaces.add(j);
	    }
	    session.setJoursRemplaces(joursRemplaces);
	    mJourCursor.close();

	    // get activité
	    final Cursor mActivityCursor = cv.query(
		    ETSMobileContentProvider.CONTENT_URI_ACTIVITY_CALENDAR, null, null, null, null);
	    final ArrayList<ActivityCalendar> acts = new ArrayList<ActivityCalendar>();
	    while (mActivityCursor.moveToNext()) {
		final ActivityCalendar a = new ActivityCalendar(mActivityCursor);
		acts.add(a);
	    }
	    session.setActivities("", acts);
	    mActivityCursor.close();

	    mySessions.add(session);
	}
	mSessionCursor.close();
	handler.obtainMessage(SHOW_DATA, mySessions).sendToTarget();

	final ArrayList<Session> sessions = getSessions(creds);
	List<ActivityCalendar> activities;

	for (final Session s : sessions) {
	    activities = removeDuplicates(getCoursIntervalSession((UserCredentials) params[0], s));

	    setColors(activities);

	    if (!activities.isEmpty()) {
		String jour = activities.get(0).getCours();
		int start = 0, end = 0;
		for (final ActivityCalendar a : activities) {

		    if (!jour.equals(a.getJour())) {

			if (s.getMaxActivities() < activities.subList(start, end).size()) {
			    s.setMaxActivities(activities.subList(start, end).size());
			}

			s.setActivities(jour, activities.subList(start, end));
			jour = a.getJour();
			start = end;
		    }
		    end++;
		}

		if (s.getMaxActivities() < activities.subList(start, end).size()) {
		    s.setMaxActivities(activities.subList(start, end).size());
		}
		s.setActivities(jour, activities.subList(start, end));
	    }

	    s.setJoursRemplaces(getJoursRemplacesSession(s));
	}

	return sessions;
    }

    public List<ActivityCalendar> removeDuplicates(List<ActivityCalendar> activities) {
	final List<ActivityCalendar> removed = new ArrayList<ActivityCalendar>();

	ActivityCalendar activity, anotherActivity;

	for (int i = 0; i < activities.size() - 1; i++) {
	    activity = activities.get(i);
	    anotherActivity = activities.get(i + 1);

	    if (activity.compareTo(anotherActivity) == 0) {
		if (activity.getStartDate().compareTo(anotherActivity.getStartDate()) == 0
			&& activity.getEndDate().compareTo(anotherActivity.getEndDate()) == 0
			&& activity.getLocation().compareTo(anotherActivity.getLocation()) != 0) {
		    activity.setLocation(activity.getLocation() + "; "
			    + anotherActivity.getLocation());
		    removed.add(anotherActivity);
		}

	    }

	}

	activities.removeAll(removed);
	return activities;

    }

    public void setColors(List<ActivityCalendar> session_activites) {
	final ArrayList<String> activites = new ArrayList<String>();
	int color_index = 0;

	// set colors before notifying ui
	for (int i = 0; i < session_activites.size(); i++) {

	    if (activites.contains(session_activites.get(i).getCours())) {
		session_activites.get(i).setDrawableResId(
			session_activites.get(
				activites.indexOf(session_activites.get(i).getCours()))
				.getDrawableResId());
		activites.add(session_activites.get(i).getCours());
	    } else {
		session_activites.get(i).setDrawableResId(dots[color_index]);
		color_index++;
		activites.add(session_activites.get(i).getCours());
	    }
	}
    }

    /**
     * Donne la liste des cours dans l'interval d'une session donnée
     * 
     * @param creds
     * @param currentSession
     * 
     * @return
     */
    private ArrayList<ActivityCalendar> getCoursIntervalSession(final UserCredentials creds,
	    final Session currentSession) {
	try {
	    final ListeHorraireEtProf listeHoraireEtProf = new ListeHorraireEtProf(creds,
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
     * Donne la liste des jours remplaces dans une session
     * 
     * @param creds
     * @param currentSession
     * 
     * @return
     */
    private ArrayList<JoursRemplaces> getJoursRemplacesSession(final Session currentSession) {

	try {
	    final LireJoursRemplaces listeJoursRemplaces = new LireJoursRemplaces(currentSession);
	    final SignetBackgroundThread<ArrayList<JoursRemplaces>, JoursRemplaces> signetBackgroundThead = new SignetBackgroundThread<ArrayList<JoursRemplaces>, JoursRemplaces>(
		    "https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx",
		    "lireJoursRemplaces", listeJoursRemplaces, JoursRemplaces.class,
		    FetchType.ARRAY, "listeJours");

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

	Collections.sort(result);

	// Bundle data = new Bundle();
	final Message msg = handler.obtainMessage(CalendarTask.ON_POST_EXEC, result);
	msg.sendToTarget();

    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	handler.obtainMessage().sendToTarget();
    }
}