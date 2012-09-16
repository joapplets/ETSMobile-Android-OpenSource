
package ca.etsmtl.applets.etsmobile.utils;

import java.util.ArrayList;

import ca.etsmtl.applets.etsmobile.models.CourseModel;

public class CourseList {

	private ArrayList<CourseModel> courseList;
	
	private static CourseList instance;
	
	private CourseList(){
		courseList = new ArrayList<CourseModel>();
	}
	
	public static CourseList getInstance(){
		if ( instance == null){
			instance = new CourseList();
		}
		return instance;	
	}
	
	public CourseModel getNewCourseModel() {
		
		CourseModel courseModel = new CourseModel();
		courseList.add(courseModel);
		return courseModel;
		
	}
	
	public CourseModel getCourse(String CourseSymbol){
		CourseModel rtn =null; 
		for (CourseModel courseModel : courseList){
			if (courseModel.getCourseSymbol().compareTo(CourseSymbol)==0){
				rtn = courseModel;
				break;
			}
		}
		return rtn;
	}
	
	public void addCourse(CourseModel course) {
		courseList.add(course);
	}
		
	public int getCount() {
		return courseList.size();
	}

	public CourseModel getItem(int position) {
		return courseList.get(position);
	}

	public long getItemId(int position) {
		return courseList.get(position).getId();
	}
	

	public boolean isEmpty() {
		return courseList.isEmpty();
	}

}