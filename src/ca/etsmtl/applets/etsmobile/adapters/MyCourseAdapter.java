package ca.etsmtl.applets.etsmobile.adapters;

import java.util.List;

import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.Course;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCourseAdapter extends ArrayAdapter<Course> {
	private static final int ITEM_VIEW_TYPE_LIST_ITEM = 0;
	private static final int ITEM_VIEW_TYPE_SEPARATOR = 1;
	private static final int ITEM_VIEW_TYPE_COUNT = 2;
	
	public MyCourseAdapter(Context context, int textViewResourceId, List<Course> objects) {
		super(context, textViewResourceId, objects);
	}
	
	@Override
	public int getViewTypeCount() {
		return ITEM_VIEW_TYPE_COUNT;
	}
	
	@Override
	public int getItemViewType(int position) {
		return position != 0 ? ITEM_VIEW_TYPE_LIST_ITEM : ITEM_VIEW_TYPE_SEPARATOR;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		if (super.getCount() > 0) {
			count = super.getCount() + 1;
		}
		
		return count;
	}
	
	@Override
	public Course getItem(int position) {
		return super.getItem(position - 1);
	}
	
	@Override
	public boolean isEnabled(int position) {
		return getItemViewType(position) != ITEM_VIEW_TYPE_SEPARATOR;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout view;
		final int type = getItemViewType(position);

		if (convertView == null) {
			view = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(type == ITEM_VIEW_TYPE_LIST_ITEM ? R.layout.course_list_item : R.layout.list_separator, view, true);
		} else {
			view = (LinearLayout) convertView;
		}
		
		if (type == ITEM_VIEW_TYPE_SEPARATOR) {
			((TextView) view.findViewById(R.id.textViewSeparator)).setText(getContext().getString(R.string.mesCours));
		} else {
			((TextView) view.findViewById(R.id.title)).setText(getItem(position).toString());
			((TextView) view.findViewById(R.id.subtitle)).setText(getItem(position).getTitreCours());
		}

		return view;
	}
}
