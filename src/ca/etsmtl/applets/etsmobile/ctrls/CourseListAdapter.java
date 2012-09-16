package ca.etsmtl.applets.etsmobile.ctrls;

import java.util.ArrayList;


import ca.etsmtl.applets.etsmobile.models.CourseModel;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel;
import ca.etsmtl.applets.etsmobile.utils.CourseList;
import ca.etsmtl.applets.etsmobile.utils.EventViewFactory;
import ca.etsmtl.applets.etsmobile.R;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CourseListAdapter implements ListAdapter {

	private CourseList courseList;
	private Context context;
	
	public CourseListAdapter(Context context) {
		courseList = CourseList.getInstance();
		this.context = context;
	}
	
	
	public int getCount() {
		return courseList.getCount();
	}


	public Object getItem(int position) {
		return courseList.getItem(position);
	}

	public long getItemId(int position) {
		return courseList.getItemId(position);
	}


	public int getItemViewType(int position) {
		return 1;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		
		if(view == null) {						
			
			CourseModel courseModel = courseList.getItem(position);
			
			LayoutInflater layout = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layout.inflate(R.layout.course_row, null);
			
			TextView txtViewLine1 = (TextView)view.findViewById(R.id.txtViewLine1);
			txtViewLine1.setText(courseModel.getCourseSymbol());
			txtViewLine1.setTextSize(35);
			
		}

		return view;
	}

	public int getViewTypeCount() {
		return 1;
	}


	public boolean hasStableIds() {
		return true;
	}


	public boolean isEmpty() {
		return courseList.isEmpty();
	}


	public void registerDataSetObserver(DataSetObserver observer) {
	}


	public void unregisterDataSetObserver(DataSetObserver observer) {
	}


	public boolean areAllItemsEnabled() {
		return true;
	}

	
	public boolean isEnabled(int arg0) {
		return true;
	}

}
