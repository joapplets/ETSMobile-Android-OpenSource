package com.applets.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.applets.R;
import com.applets.models.Course;

public class CourseListAdapter extends ArrayAdapter<Course>{
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

		if ( row == null ) {
			LayoutInflater inflater = context.getLayoutInflater( );
			row = inflater.inflate( R.layout.program_list_row, null );
			wrapper = new CourseWrapper( row );
			row.setTag( wrapper );
		} else {
			wrapper = (CourseWrapper) row.getTag( );
		}
		Course course = list.get( position );
		wrapper.setTitle( course.getName());
		wrapper.setDescription( course.getDescription( ) );		

		return row;
	}

	private class CourseWrapper {
		private View		view;
		private TextView	title		= null;
		private TextView	description	= null;
		private ImageView	image;

		public CourseWrapper(View base) {
			view = base;
		}

		public void setImage(String image) {
			getImage().setImageResource( R.drawable.spacer_middle );
		}

		private ImageView getImage() {
			if(image == null) {
				image = (ImageView)view.findViewById( R.id.program_list_row_image );
			}
			return image;
		}

		private TextView getTitle() {
			if ( title == null ) {
				title = (TextView) view.findViewById( R.id.program_list_row_title );
			}
			return title;
		}

		public TextView getDescription() {
			if ( description == null ) {
				description = (TextView) view.findViewById( R.id.program_list_row_desc );
			}
			return description;
		}

		public void setDescription(String description) {
			getDescription( ).setText( description);
		}

		public void setTitle(String title) {
			getTitle( ).setText( title );
		}
	}

}
