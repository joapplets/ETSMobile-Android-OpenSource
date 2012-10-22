package ca.etsmtl.applets.etsmobile.adapters;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.CalendarEvent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
		Resources res = this.context.getResources();
		
		//set title
		TextView textView = (TextView) rowView.findViewById(R.id.txt_title);

		
		textView.setText(String.format(res.getString(
				R.string.calendar_event_list_item_title), 
				new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.CANADA_FRENCH).
					format(this.events.get(position).getStartDate()),
				this.events.get(position).getName()));
		
		//set subtitle
		textView = (TextView) rowView.findViewById(R.id.txt_subtitle);
		
		textView.setText(String.format(res.getString(
				R.string.calendar_event_list_item_subtitle), 
				this.events.get(position).getEventType(), 
				this.events.get(position).getLocation()));
		
		
		//set indicator
		ShapeDrawable indicator = new ShapeDrawable(new  OvalShape());
		indicator.getPaint().setColor(this.events.get(position).getEventColor());
		indicator.getPaint().setStyle(Style.FILL);
		
		ImageView img = (ImageView) rowView.findViewById(R.id.img_indicator);
		
		img.setBackgroundDrawable(indicator);
	
		img.setAlpha(0);
	
	
		return rowView;
	}
	



}
