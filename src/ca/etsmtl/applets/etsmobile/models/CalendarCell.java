package ca.etsmtl.applets.etsmobile.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class CalendarCell extends Observable implements
		Collection<CalendarEvent> {

	List<CalendarEvent> events;
	Date date;

	public CalendarCell() {
		events = new ArrayList<CalendarEvent>();

	}

	public CalendarCell(final Date date) {
		events = new ArrayList<CalendarEvent>();

		this.date = date;

	}

	@Override
	public boolean add(final CalendarEvent object) {
		// TODO Auto-generated method stub
		return events.add(object);
	}

	@Override
	public boolean addAll(final Collection<? extends CalendarEvent> arg0) {
		// TODO Auto-generated method stub
		return events.addAll(arg0);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		events.clear();
	}

	@Override
	public boolean contains(final Object object) {
		// TODO Auto-generated method stub
		return events.contains(object);
	}

	@Override
	public boolean containsAll(final Collection<?> arg0) {
		// TODO Auto-generated method stub
		return events.containsAll(arg0);
	}

	public Date getDate() {
		return date;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return events.isEmpty();
	}

	@Override
	public Iterator<CalendarEvent> iterator() {
		// TODO Auto-generated method stub
		return events.iterator();
	}

	@Override
	public void notifyObservers() {
		this.notifyObservers(events);
	}

	@Override
	public boolean remove(final Object object) {
		// TODO Auto-generated method stub
		return events.remove(object);
	}

	@Override
	public boolean removeAll(final Collection<?> arg0) {
		// TODO Auto-generated method stub
		return events.removeAll(arg0);
	}

	@Override
	public boolean retainAll(final Collection<?> arg0) {
		// TODO Auto-generated method stub
		return events.retainAll(arg0);
	}

	@Override
	public void setChanged() {
		super.setChanged();
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return events.size();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return events.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] array) {
		// TODO Auto-generated method stub
		return events.toArray(array);
	}

}
