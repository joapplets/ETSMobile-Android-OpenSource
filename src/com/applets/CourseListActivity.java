package com.applets;

import java.util.ArrayList;

import android.os.Bundle;

import com.applets.adapters.CourseListAdapter;
import com.applets.baseactivity.BaseListActivity;
import com.applets.models.Course;
import com.applets.models.CourseList;
import com.applets.utils.xml.IAsyncTaskListener;
import com.markupartist.android.widget.actionbar.R;

public class CourseListActivity extends BaseListActivity implements
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

	initActionBar(getString(R.string.courses_list_title),
		R.id.base_list_actionbar);
	initCourseList();

	registerForContextMenu(getListView());
    }

    @Override
    public void onPostExecute() {
	setListAdapter(new CourseListAdapter(this, courseList));
    }

}
