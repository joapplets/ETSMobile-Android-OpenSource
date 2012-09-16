package ca.etsmtl.applets.etsmobile.ctrls;

import java.util.ArrayList;

import ca.etsmtl.applets.etsmobile.models.EventDetailsModel;
import ca.etsmtl.applets.etsmobile.utils.EventViewFactory;
import android.R;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class EventListAdapter implements ListAdapter {
	
	//private EventHandlerModel eventHandler;
	private ArrayList<EventDetailsModel> eventList;
	private Context context;
	
	public EventListAdapter (ArrayList<EventDetailsModel> eventList, Context context) {
		this.eventList = eventList;
		this.context = context;
	}

	public int getCount() {
		return eventList.size();
	}

	public Object getItem(int position) {
		return eventList.get(position);
	}

	public long getItemId(int position) {
		return eventList.get(position).getId();
	}

	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		//http://stackoverflow.com/questions/4382176/what-is-convertview-parameter-in-arrayadapter-getview-method
		View view = convertView;
		
		if(view == null){			
			
			EventDetailsModel event = eventList.get(position);
			view = EventViewFactory.getViewForList(event, context);
			
		}

		return view;

	}
	

	

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		return eventList.isEmpty();
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}

}
