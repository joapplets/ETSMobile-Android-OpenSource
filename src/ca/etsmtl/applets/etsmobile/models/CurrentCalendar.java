package ca.etsmtl.applets.etsmobile.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.TimeZone;

public class CurrentCalendar extends Observable {

	Calendar current;
	private ArrayList<ActivityCalendar> obj = new ArrayList<ActivityCalendar>();

	public CurrentCalendar() {
		current = Calendar.getInstance(TimeZone.getTimeZone("Canada/Eastern"),
				Locale.CANADA_FRENCH);
	}
	
	public CurrentCalendar(Calendar current) {
		this.current = current;
	}

	public Calendar getCalendar() {
		return current;
	}

	public void nextMonth() {
		current.add(Calendar.MONTH, 1);

		super.setChanged();
		this.notifyObservers(obj);
	}

	public void previousMonth() {
		current.add(Calendar.MONTH, -1);
		super.setChanged();
		super.notifyObservers(obj);
	}

	@Override
	public void setChanged() {
		super.setChanged();
	}

	public ArrayList<ActivityCalendar> getActivities() {
		return obj;
	}

	public void setActivities(ArrayList<ActivityCalendar> obj) {

		this.obj = obj;
	}
}
