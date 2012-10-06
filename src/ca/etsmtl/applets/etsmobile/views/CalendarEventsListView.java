package ca.etsmtl.applets.etsmobile.views;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.adapters.CalendarEventsAdapter;
import ca.etsmtl.applets.etsmobile.models.CalendarEvent;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CalendarEventsListView extends ListView implements Observer{

	
	Context context;
	public CalendarEventsListView(Context context) {
		super(context);
		
		this.context = context;
		// TODO Auto-generated constructor stub
		
	}
	
	public CalendarEventsListView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    this.context = context;
	}

	public CalendarEventsListView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    this.context = context;
	}


	@SuppressWarnings("unchecked")
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		List<CalendarEvent> events = (List<CalendarEvent>) data;
		
		setAdapter(new CalendarEventsAdapter(this.context, R.layout.calendar_event_list_item,
				events));
		
	}



}
