package ca.etsmtl.applets.etsmobile.adapters;

import java.util.List;

import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.CalendarEvent;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CalendarEventsAdapter extends ArrayAdapter<CalendarEvent>{

	  Context context; 
	  int layoutResourceId;  
	  List<CalendarEvent> events;
	
	public CalendarEventsAdapter(Context context, int textViewResourceId,
			List<CalendarEvent> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.layoutResourceId = textViewResourceId;
		this.events = objects;
		this.context = context;
		
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(this.layoutResourceId, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.txt_title);

		Resources res = this.context.getResources();
		
		textView.setText(String.format(res.getString(
				R.string.calendar_event_list_item_title), 
				this.events.get(position).getStartDate().getHours(), 
				this.events.get(position).getStartDate().getMinutes(), 
				this.events.get(position).getName()));
		
		
		textView = (TextView) rowView.findViewById(R.id.txt_subtitle);
		
		textView.setText(String.format(res.getString(
				R.string.calendar_event_list_item_subtitle), 
				this.events.get(position).getEventType(), 
				this.events.get(position).getLocation()));

	
		return rowView;
	}
	



}
