package ca.etsmtl.applets.etsmobile.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    private final static int[] dots = new int[] { R.drawable.kal_marker_red_small,
	    R.drawable.kal_marker_fuchsia_small, R.drawable.kal_marker_green_small,
	    R.drawable.kal_marker_lime_small, R.drawable.kal_marker_maroon_small,
	    R.drawable.kal_marker_navy_small, R.drawable.kal_marker_aqua_small,
	    R.drawable.kal_marker_yellow_small, R.drawable.kal_marker_black_small };

    public CalendarTask(final CalendarTaskHandler handler) {
	this.handler = handler;

    }

    @Override
    protected ArrayList<Session> doInBackground(final Object... params) {
	onPreExecute();
	final ArrayList<Session> sessions = getSessions((UserCredentials) params[0]);
	List<ActivityCalendar> activities;

	for (final Session s : sessions) {
	    activities = removeDuplicates(getCoursIntervalSession((UserCredentials) params[0], s));

	    setColors(activities);

	    if (!activities.isEmpty()) {
		String jour = activities.get(0).getCours();
		int start = 0, end = 0;
		for (final ActivityCalendar a : activities) {

		    if (!jour.equals(a.getJour())) {
			s.setActivities(jour, activities.subList(start, end));
			jour = a.getJour();
			start = end;
		    }
		    end++;
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