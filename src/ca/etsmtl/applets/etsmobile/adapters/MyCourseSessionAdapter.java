package ca.etsmtl.applets.etsmobile.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.Session;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCourseSessionAdapter extends ArrayAdapter<Session> {

	private static final int ITEM_VIEW_TYPE_LIST_ITEM = 0;
	private static final int ITEM_VIEW_TYPE_SEPARATOR = 1;
	private static final int ITEM_VIEW_TYPE_COUNT = 2;
	private ArrayList<Integer> separatorPositions;
	
	public MyCourseSessionAdapter(Context context, int textViewResourceId, List<Session> objects) {
		super(context, textViewResourceId, objects);
		
		Collections.sort(objects, new Comparator<Session>() {
		    public int compare(Session s1, Session s2) {
		        return s2.getDateDebut().compareTo(s1.getDateDebut());
		    }
	    });
		
		separatorPositions = new ArrayList<Integer>();
		int year = 0;
		for (int i = 0; i < objects.size(); i++) {
			Session session = objects.get(i);
			if (year != session.getDateDebut().getYear()) {
				separatorPositions.add(i + separatorPositions.size());
				year = session.getDateDebut().getYear();
			}
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return ITEM_VIEW_TYPE_COUNT;
	}
	
	@Override
	public int getItemViewType(int position) {
		return separatorPositions.indexOf(position) == -1 ? ITEM_VIEW_TYPE_LIST_ITEM : ITEM_VIEW_TYPE_SEPARATOR;
	}
	
	@Override
	public int getCount() {
		return separatorPositions.size() + super.getCount();
	}
	
	@Override
	public Session getItem(int position) {
		int positionOffset = 0;
		
		for (int separatorPosition : separatorPositions) {
			if (separatorPosition < position)
				positionOffset++;
		}
		
		return super.getItem(position - positionOffset);
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
			li.inflate(type == ITEM_VIEW_TYPE_LIST_ITEM ? R.layout.session_list_item : R.layout.list_separator, view, true);
		} else {
			view = (LinearLayout) convertView;
		}
		
		if (type == ITEM_VIEW_TYPE_SEPARATOR) {
			SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
			((TextView) view.findViewById(R.id.textViewSeparator)).setText(simpleDateformat.format(getItem(position).getDateDebut()));
		} else {
			((TextView) view.findViewById(R.id.textViewSession)).setText(getItem(position).toString());
		}

		return view;
	}
}
