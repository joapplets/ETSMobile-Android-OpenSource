package ca.etsmtl.applets.etsmobile.models;

import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.TimeZone;

public class CurrentCalendar extends Observable {

	Calendar current;

	public CurrentCalendar() {
		current = Calendar.getInstance(TimeZone.getTimeZone("Canada/Eastern"),
				Locale.CANADA_FRENCH);
	}

	public CurrentCalendar(final Calendar current) {
		this.current = current;
	}

	public Calendar getCalendar() {
		return current;
	}

	public void nextMonth() {
		current.add(Calendar.MONTH, 1);

		super.setChanged();
		this.notifyObservers(current);
	}
<<<<<<< HEAD
=======
	
	public void nextWeek(){
		current.add(Calendar.DAY_OF_YEAR, +7);
		super.setChanged();
		super.notifyObservers(current);
	}
>>>>>>> origin/lauCalendar

	public void previousMonth() {
		current.add(Calendar.MONTH, -1);
		super.setChanged();
		super.notifyObservers(current);
	}

<<<<<<< HEAD
=======
	public void previousWeek(){
		current.add(Calendar.DAY_OF_YEAR, -7);
		super.setChanged();
		super.notifyObservers(current);
	}
>>>>>>> origin/lauCalendar
	@Override
	public void setChanged() {
		super.setChanged();
	}

	public void setToday() {
		current = Calendar.getInstance(TimeZone.getTimeZone("Canada/Eastern"),
				Locale.CANADA_FRENCH);

		super.setChanged();
		this.notifyObservers(current);
	}
	
	public void setDate(int year, int month, int day){
		current.set(year, month, day);
		super.setChanged();
		super.notifyObservers(current);
	}

}
