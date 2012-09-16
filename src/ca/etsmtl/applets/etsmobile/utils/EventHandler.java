package ca.etsmtl.applets.etsmobile.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import android.util.Log;

import ca.etsmtl.applets.etsmobile.models.EventDetailsModel;

public class EventHandler {

	/** Event handler of all event known in the system */
	private static EventHandler allEvent;
	
	/** Concrete container of a handler */
	private ArrayList<EventDetailsModel> eventList = new ArrayList<EventDetailsModel>();
	
	private EventHandler () {
		
	}
	
	public static EventHandler getInstance() {
		if(allEvent == null)
		{
			allEvent = new EventHandler();
		}
		return allEvent;	
	}
	
	public void add(EventDetailsModel eventDetailsToAdd) {
		eventList.add(eventDetailsToAdd);
		Log.v("checkdate", String.format("%tc", eventDetailsToAdd.getBeginDateTime()));
	}
	
//	public void remove(EventDetailsModel eventDetailsToRemove) {
//		eventList.remove(eventDetailsToRemove);
//	}
//	
//	public EventDetailsModel getEventModel(int index) {
//		return eventList.get(index);
//	}
//	
//	public ArrayList<EventDetailsModel> getList()
//	{
//		return eventList;
//	}
//	
//	public int getCount() {
//		return eventList.size();
//	}
//	
//	public EventDetailsModel getItem(int index) {
//		return eventList.get(index);
//	}
//	
//	public boolean isEmpty(){
//		return eventList.isEmpty();
//	}
	
	public EventDetailsModel getEventById(int id){
		
		EventDetailsModel event = null;
		for(EventDetailsModel eventDetailsModel: eventList) {
			if(eventDetailsModel.getId() == id){
				event =  eventDetailsModel;
			}
		}
		
		return event;
	}
	
	public void deleteEventById(int id){
		EventDetailsModel event = null;
		for(EventDetailsModel eventDetailsModel: eventList) {
			if(eventDetailsModel.getId() == id){
				event =eventDetailsModel;
			}
		}
		eventList.remove(event);
	}
	
	
	private void sortList(ArrayList<EventDetailsModel> rtn){

		for(int k =0; k < rtn.size(); k++)
		{
			for(int i = 0; i < rtn.size(); i++){

				for(int j = 0; j < rtn.size()-1; j++){


					if(rtn.get(i).getBeginDateTime().compareTo(rtn.get(j).getBeginDateTime()) > 0){
						
						EventDetailsModel tmp = rtn.get(i);
						rtn.set(i, rtn.get(j));
						rtn.set(j, tmp);

					}
					
				}

			}
			
		}
		
		Collections.reverse(rtn);
		
	}

	
	
	public ArrayList<EventDetailsModel> getListAtDateOnly(Calendar date) {
		
		ArrayList<EventDetailsModel> rtn = new ArrayList<EventDetailsModel>();
		date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0, 0);
		
		Calendar endDate = Calendar.getInstance();
		endDate.setTimeInMillis(date.getTimeInMillis());
		endDate.add(Calendar.HOUR_OF_DAY,+23);
		endDate.add(Calendar.MINUTE,+59);
		endDate.add(Calendar.SECOND,+59);
		
		for(EventDetailsModel eventDetailsModel: eventList) {
			if(eventDetailsModel.getBeginDateTime().after(date) && eventDetailsModel.getBeginDateTime().before(endDate)) {
				rtn.add(eventDetailsModel);
			}
		}		
		
		sortList(rtn);
		return rtn;
		
	}
	
	public ArrayList<EventDetailsModel> getListFromDate(Calendar date) {
		
		ArrayList<EventDetailsModel> rtn = new ArrayList<EventDetailsModel>();
		date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0);
		
		for(EventDetailsModel eventDetailsModel: eventList) {
			if(eventDetailsModel.getBeginDateTime().after(date)) {
				rtn.add(eventDetailsModel);
			}
		}
		
		sortList(rtn);
		return rtn;
		
	}
	
	public ArrayList<EventDetailsModel> getListFromDateForClass(Calendar date, String courseSymbol) {
		
		ArrayList<EventDetailsModel> rtn = new ArrayList<EventDetailsModel>();
		date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0);
		
		for(EventDetailsModel eventDetailsModel: eventList) {
			if(eventDetailsModel.getBeginDateTime().after(date)) {
				if(eventDetailsModel.getCourseSymbol().equals(courseSymbol)) {
					rtn.add(eventDetailsModel);
				}					
			}
		}
		
		sortList(rtn);
		return rtn;
		
	}
	
}
