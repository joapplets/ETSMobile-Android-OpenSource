package ca.etsmtl.applets.etsmobile.models;

import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.TimeZone;

public class CurrentCalendar extends Observable implements Comparable<Calendar> {

	Calendar current;

	public CurrentCalendar() {
		current = Calendar.getInstance(TimeZone.getTimeZone("Canada/Eastern"),
				Locale.CANADA_FRENCH);
	}

	@Override
	public int compareTo(final Calendar another) {

		return current.get(Calendar.MONTH) == another.get(Calendar.MONTH) ? 0
				: 1;
	}

	public Calendar getCalendar() {
		return current;
	}

	public void nextMonth() {
		current.add(Calendar.MONTH, 1);

		super.setChanged();
		this.notifyObservers(current);
	}

	public void previousMonth() {
		current.add(Calendar.MONTH, -1);
		super.setChanged();
		super.notifyObservers(current);
	}

	@Override
	public void setChanged() {
		super.setChanged();
	}
}
