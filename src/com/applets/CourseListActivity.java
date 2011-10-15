package com.applets;

import java.util.ArrayList;

import android.os.Bundle;

import com.applets.adapters.CourseListAdapter;
import com.applets.baseactivity.BaseListActivity;
import com.applets.models.Course;
import com.applets.models.CourseList;

public class CourseListActivity extends BaseListActivity {

	private ArrayList<Course> courseList = new ArrayList<Course>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);

		initActionBar(getString(R.string.courses_list_title),
				R.id.base_list_actionbar);
		initCourseList();

		setListAdapter(new CourseListAdapter(this, courseList));
		registerForContextMenu(getListView());
	}
	
	private void initCourseList() {
		courseList = new CourseList(getString(R.string.courses_list_feed), this);
	}
}
