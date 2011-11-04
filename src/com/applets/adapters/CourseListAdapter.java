package com.applets.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.applets.R;
import com.applets.adapters.wrappers.CourseWrapper;
import com.applets.models.Course;

public class CourseListAdapter extends ArrayAdapter<Course> {
    private Activity context;
    private ArrayList<Course> list;

    public CourseListAdapter(Activity context, ArrayList<Course> courses) {
	super(context, R.layout.program_list_row, courses);
	this.context = context;
	list = courses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View row = convertView;
	CourseWrapper wrapper;

	if (row == null) {
	    LayoutInflater inflater = context.getLayoutInflater();
	    row = inflater.inflate(R.layout.program_list_row, null);
	    wrapper = new CourseWrapper(row);
	    row.setTag(wrapper);
	} else {
	    wrapper = (CourseWrapper) row.getTag();
	}
	Course course = list.get(position);
	wrapper.setTitle(course.getName());
	wrapper.setDescription(course.getDescription());

	return row;
    }

}
