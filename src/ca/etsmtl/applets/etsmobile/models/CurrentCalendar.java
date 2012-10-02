package ca.etsmtl.applets.etsmobile.models;

import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.TimeZone;

public class CurrentCalendar extends Observable implements Comparable<Calendar> {

	Calendar current;

	public CurrentCalendar() {
		this.current = Calendar.getInstance(
				TimeZone.getTimeZone("Canada/Eastern"), Locale.CANADA_FRENCH);

	}

	public void nextMonth() {
		this.current.add(Calendar.MONTH, 1);

		super.setChanged();
		this.notifyObservers(this.current);
	}

	public void previousMonth() {
		this.current.add(Calendar.MONTH, -1);
		super.setChanged();
		super.notifyObservers(this.current);
	}

	public void setChanged() {
		super.setChanged();
	}

	public Calendar getCalendar() {
		return this.current;
	}

	@Override
	public int compareTo(Calendar another) {

		return this.current.get(Calendar.MONTH) == another.get(Calendar.MONTH) ? 0
				: 1;
	}

	
}
