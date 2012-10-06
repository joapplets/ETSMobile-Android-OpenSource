package ca.etsmtl.applets.etsmobile.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;




public class CalendarCell extends Observable implements Collection<CalendarEvent>{
	
	List<CalendarEvent> events;
	Date date;
	

	
	public CalendarCell()
	{
		this.events = new ArrayList<CalendarEvent>();


	}
	
	public CalendarCell(Date date)
	{
		this.events = new ArrayList<CalendarEvent>();
		
		this.date = date;
	
	}

	public void setChanged()
	{
		super.setChanged();
	}
	
	public void notifyObservers()
	{
		this.notifyObservers(this.events);
	}
	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	@Override
	public boolean add(CalendarEvent object) {
		// TODO Auto-generated method stub
		return this.events.add(object);
	}

	@Override
	public boolean addAll(Collection<? extends CalendarEvent> arg0) {
		// TODO Auto-generated method stub
		return this.events.addAll(arg0);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.events.clear();
	}

	@Override
	public boolean contains(Object object) {
		// TODO Auto-generated method stub
		return this.events.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return this.events.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return this.events.isEmpty();
	}

	@Override
	public Iterator<CalendarEvent> iterator() {
		// TODO Auto-generated method stub
		return this.events.iterator();
	}

	@Override
	public boolean remove(Object object) {
		// TODO Auto-generated method stub
		return this.events.remove(object);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return this.events.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return this.events.retainAll(arg0);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.events.size();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return this.events.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		// TODO Auto-generated method stub
		return this.events.toArray(array);
	}

}
