package com.applets;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;

import com.applets.models.Course;

public class CourseListActivity extends ListActivity {

	private final ArrayList<Course> courseList = new ArrayList<Course>();

	private void initCourseList() {
		// courseList = new CourseList(getString(R.string.host)
		// + getString(R.string.api_cours), this, this);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);

		initCourseList();

		registerForContextMenu(getListView());
	}
}
