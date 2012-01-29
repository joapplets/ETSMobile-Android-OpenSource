package com.applets.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.applets.adapters.wrappers.CourseWrapper;
import com.applets.models.Course;
import com.markupartist.android.widget.actionbar.R;

public class CourseListAdapter extends ArrayAdapter<Course> {
    private final Activity context;
    private final ArrayList<Course> list;

    public CourseListAdapter(final Activity context,
	    final ArrayList<Course> courses) {
	super(context, R.layout.basic_row, courses);
	this.context = context;
	list = courses;
    }

    @Override
    public View getView(final int position, final View convertView,
	    final ViewGroup parent) {
	View row = convertView;
	CourseWrapper wrapper;

	if (row == null) {
	    final LayoutInflater inflater = context.getLayoutInflater();
	    row = inflater.inflate(R.layout.basic_row, null);
	    wrapper = new CourseWrapper(row);
	    row.setTag(wrapper);
	} else {
	    wrapper = (CourseWrapper) row.getTag();
	}
	final Course course = list.get(position);
	wrapper.setTitle(course.getName());
	wrapper.setDescription(course.getDescription());

	return row;
    }

}
