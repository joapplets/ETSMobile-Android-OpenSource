package ca.etsmtl.applets.etsmobile.ctrls;

import java.util.Calendar;
import java.util.Date;

import android.util.Log;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel;
import ca.etsmtl.applets.etsmobile.models.LocalListModel;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel.EventType;
import ca.etsmtl.applets.etsmobile.utils.CourseList;
import ca.etsmtl.applets.etsmobile.utils.EventHandler;

public class AddEventCtrl {
	private LocalListModel locaux;
	private CourseList courseList;
	
	public AddEventCtrl(){
		locaux = LocalListModel.getInstance();
		courseList = CourseList.getInstance();
	}
	
	
	public void createEvent(String type, String courseSymbol, String titre, Calendar date, Calendar endTime, String salle, String info){
		
		EventDetailsModel event = new EventDetailsModel(); 
		event = setEventDetail(event, type, courseSymbol, titre, date, endTime, salle,  info );
		EventHandler.getInstance().add(event);
		if(!checkLocal(salle)){
			addLocal(salle);
		}
	}
	
	public void modifyEvent(String type, String courseSymbol,String titre,Calendar date, Calendar endTime, String salle, String info,int eventID){
		
		EventDetailsModel event = EventHandler.getInstance().getEventById(eventID);
		event = setEventDetail(event, type, courseSymbol, titre, date, endTime, salle,  info );
		if(!checkLocal(salle)){
			addLocal(salle);
		}
	}
	
	public EventDetailsModel setEventDetail(EventDetailsModel event, String type, String courseSymbol, String titre, Calendar date, Calendar endTime, String salle, String info ){
		
		event.setBeginDateTime((Calendar)(date.clone()));
		event.setEndDateTime((Calendar)(endTime.clone()));
		event.setCourseSymbol(courseSymbol);
		event.setTitle(titre);
		event.setDescription(info);
		event.setLocal(salle);
		
		if(type.equals(EventType.DEADLINE.getDescription())){
			event.setEventType(EventType.DEADLINE);
		}
		else if(type.equals(EventType.COURSE.getDescription())){
			event.setEventType(EventType.COURSE);
		}
		else if(type.equals(EventType.TP.getDescription())){
			event.setEventType(EventType.TP);
		}
		else if(type.equals(EventType.MEETING.getDescription())){
			event.setEventType(EventType.MEETING);
		}
		else if(type.equals(EventType.PERSONAL.getDescription())){
			event.setEventType(EventType.PERSONAL);
		}
		else if(type.equals(EventType.REVIEW.getDescription())){
			event.setEventType(EventType.REVIEW);
		}
		return event;
	}
		
	public String[] getLocauxString(){
		return locaux.getListLocaux();
	}
	
	public boolean checkLocal(String local){
		Log.v("checkLocal", "checkLocalOK ");
		return locaux.checkLocal(local);
	}
	
	public void addLocal(String local){
		Log.v("addLocal", "addLocalOK ");
		locaux.addLocal(local);
	}
	
	public String[] getCourseSymbols(){
		
		CourseList courseList = CourseList.getInstance();
		String[] courseSymbolList = new String[courseList.getCount()];
		
		for(int i = 0; i < courseList.getCount(); i++){
			courseSymbolList[i] = courseList.getItem(i).getCourseSymbol();
		}
		return courseSymbolList;
	}
	

}
