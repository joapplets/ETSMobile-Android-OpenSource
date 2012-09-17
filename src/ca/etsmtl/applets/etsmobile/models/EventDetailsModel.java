package ca.etsmtl.applets.etsmobile.models;

import java.util.Calendar;
import java.util.Date;

/**
 * This class holds the data related to an event to be shown. EventDetailsModel
 * can be specialized to hold special kind of event. Special subclasses can
 * override one or more method to change the behavior. To do not interfer with
 * user 
 * @author franck
 *
 */
public class EventDetailsModel {


	public enum EventType {
		DEADLINE	("Remise"), 
		COURSE		("Cours"), 
		TP			("TP"), 
		MEETING		("Réunion"),
		PERSONAL	("Personnel"),
		REVIEW		("Examen");

		private final String description;

		
		EventType(String description) {
			this.description = description;
		}

		public String getDescription() { return description; }

	}
	
	public EventDetailsModel(){
		setId(this.hashCode());
	}	

	/** unique id of the event */
	private int id = 0;
	/** set the unique id of the event */
	public void setId(int value) {
		this.id = value;
	}
	/** return the unique id of the event */
	public int getId() {
		return this.id;
	}
	
	/**   Title of event  */
	private String title = "";
	/** set title of event */
	public void setTitle(String title){
		this.title = title;
	}
	/** return title of event */
	public String getTitle(){
		return title;
	}
	
	
	
	/** begin date and time of the event */
	protected Calendar beginDateTime;
	/** set the begin date and time of the event */
	public void setBeginDateTime(Calendar value) {
		this.beginDateTime = value;
	}
	/** return the begin date and time of the event */
	public Calendar getBeginDateTime() {
		return this.beginDateTime;
	}

	/** end date and time of the event */
	protected Calendar endDateTime;
	/** set the end date and time of the event */
	public void setEndDateTime(Calendar value) {
		this.endDateTime = value;
	}
	/** return the end date and time of the event */
	public Calendar getEndDateTime() {
		return this.endDateTime;
	}

	/** computed description as per a sub class of the event */
	protected String computedDescription = "";
	/** set the computed description as per a sub class of the event */
	public void setComputedDescription(String value) {
		this.computedDescription = "";
	}
	/** return the computed description as per a sub class of the event */
	public String getComputedDescription() {
		return this.computedDescription;
	}

	/** user description of the event */
	private String description = "";
	/** set the user description of the event */
	public void setDescription(String value) {
		this.description = value;
	}
	/** return the user description of the event */
	public String getDescription() {
		return this.description;
	}

	/** next reminder date time of the event */
	private Calendar nextReminder;
	/** set the next reminder date time of the event */
	public void setNextReminder(Calendar value) {
		this.nextReminder = value;
	}
	/** return the next reminder date time of the event */
	public Calendar getNextReminder() {
		return this.nextReminder;
	}

	/** Course symbol; ex: MAT145 */
	private String courseSymbol = "";
	public void setCourseSymbol(String courseSymbol) {
		this.courseSymbol = courseSymbol;
	}	
	public String getCourseSymbol() {
		return this.courseSymbol;
	}
	
	/** Course type; can be "course" or "lab" */
	private EventType eventType;
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public EventType getEventType() {
		return this.eventType;
	}
	
	private String local;
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}

	
	

}

/**

CourseList courseList = CourseList.getInstance();
CourseModel courseModel = courseList.getCourse(courseSymbol);

// Set information as if it is a course
if(courseType == CourseType.COURSE) {
	this.computedDescription =
			String.format("Cours: %s-%s, %s\n%s, %s",
					courseModel.getCourseSymbol(),
					courseModel.getGroupId(),
					courseModel.getCourseTitle(),
					courseModel.getCourseLocal(),
					courseModel.getCourseTeacher());			
}

// Set information as if it is a lab
if(courseType == CourseType.LAB){
	this.computedDescription =
			String.format("Lab: %s-%s, %s\n%s, %s",
					courseModel.getCourseSymbol(),
					courseModel.getGroupId(),
					courseModel.getCourseTitle(),
					courseModel.getLabLocal(),
					courseModel.getLabTeacher());	
*/
