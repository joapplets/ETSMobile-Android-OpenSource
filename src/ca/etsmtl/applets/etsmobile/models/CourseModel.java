package ca.etsmtl.applets.etsmobile.models;

import java.util.Calendar;
import java.util.Calendar;

public class CourseModel {

	/** unique id of the course */
	private long id = 0;
	/** set the unique id of the course */
	public void setId(long value) {
		this.id = value;
	}
	/** return the unique id of the course */
	public long getId() {
		return this.id;
	}


	/** course symbol; ex: "MAT-145" */
	private String courseSymbol;
	public String getCourseSymbol() {
		return courseSymbol;
	}
	public void setCourseSymbol(String courseSymbol) {
		this.courseSymbol = courseSymbol;
	}
	
	/** course group id; ex: 1 */
	private int groupId;
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	/** course session id; ex: "H12", "E12", "A12" */
	private Calendar session;
	public Calendar getSession() {
		return session;
	}
	public void setSession(Calendar session) {
		this.session = session;
	}

	/** course title; ex: "Calcul diff�rentiel/int�gral" */
	private String courseTitle;
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	/** course Calendar time in a week; ex: Monday 8h30 ->
	 * 		= new Calendar(0, 0, 1, 8, 30, 0) */
	private Calendar courseSession;
	public Calendar getCourseSession() {
		return courseSession;
	}
	public void setCourseSession(Calendar courseSession) {
		this.courseSession = courseSession;
	}

	/** course lacal; ex: "B-1502" */
	private String courseLocal;
	public String getCourseLocal() {
		return courseLocal;
	}
	public void setCourseLocal(String courseLocal) {
		this.courseLocal = courseLocal;
	}

	/** course Calendar time in a week; ex: Tuesday 8h30 ->
	 * 		= new Calendar(0, 0, 2, 8, 30, 0) */
	private Calendar labSession;
	public Calendar getLabSession() {
		return labSession;
	}
	public void setLabSession(Calendar labSession) {
		this.labSession = labSession;
	}

	/** lab lacal; ex: "B-1504" */
	private String labLocal;
	public String getLabLocal() {
		return labLocal;
	}
	public void setLabLocal(String labLocal) {
		this.labLocal = labLocal;
	}

	/** course teacher; ex: "Michael McGuffin" */
	private String courseTeacher;
	public String getCourseTeacher() {
		return courseTeacher;
	}
	public void setCourseTeacher(String courseTeacher) {
		this.courseTeacher = courseTeacher;
	}

	/** course teacher; ex: "Francis Cardinal" */
	private String labTeacher;
	public String getLabTeacher() {
		return labTeacher;
	}
	public void setLabTeacher(String labTeacher) {
		this.labTeacher = labTeacher;
	}

	/** course number of credits; ex: 4 */
	private int nbOfCredits;
	public int getNbOfCredits() {
		return nbOfCredits;
	}
	public void setNbOfCredits(int nbOfCredits) {
		this.nbOfCredits = nbOfCredits;
	}
	
	/** course description*/
	private String courseDescription;
	public String getcourseDescription() {
		return courseDescription;
	}
	public void setcourseDescription(String courseDescription){
		this.courseDescription = courseDescription;
	}

}
