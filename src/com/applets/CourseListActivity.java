package com.applets;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;

import com.applets.adapters.CourseListAdapter;
import com.applets.models.Course;
import com.applets.models.CourseList;
import com.applets.utils.xml.IAsyncTaskListener;

public class CourseListActivity extends ListActivity implements
		IAsyncTaskListener {

	private ArrayList<Course> courseList = new ArrayList<Course>();

	private void initCourseList() {
		courseList = new CourseList(getString(R.string.host)
				+ getString(R.string.api_cours), this, this);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);

		initCourseList();

		registerForContextMenu(getListView());
	}

	@Override
	public void onPostExecute() {
		setListAdapter(new CourseListAdapter(this, courseList));
	}

}
