package ca.etsmtl.applets.etsmobile.utils;

import java.util.Calendar;

import ca.etsmtl.applets.etsmobile.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import ca.etsmtl.applets.etsmobile.models.CourseModel;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel.EventType;
import ca.etsmtl.applets.etsmobile.views.CourseActivity;
import ca.etsmtl.applets.etsmobile.views.CourseDetailsActivity;

public class EventViewFactory {

	
	public static View getViewForList(EventDetailsModel event, Context context){
		LayoutInflater layout = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layout.inflate(R.layout.row, null);
		
		if(event.getEventType() == EventType.COURSE || event.getEventType() == EventType.TP){
			getViewForCourseEvent(view, event);
		}
		else if(event.getEventType() == EventType.DEADLINE){
			getViewForDeadlineEvent(view, event);
		}
		else 
		if(event instanceof EventDetailsModel){
			getViewForEvent(view, event);
		}
		
		return view;
	}
	
	private static void getViewForEvent(View view, EventDetailsModel event){
		TextView text_Main = (TextView) view.findViewById(R.id.text_Main);
		TextView text_Type = (TextView) view.findViewById(R.id.text_Type);
		TextView text_Room = (TextView) view.findViewById(R.id.text_Room);
		TextView text_Start = (TextView) view.findViewById(R.id.text_StartTime);
		TextView text_End = (TextView) view.findViewById(R.id.text_EndTime);
		TextView text_Date = (TextView) view.findViewById(R.id.text_Date);
		text_Date.setVisibility(View.VISIBLE);
		
		
		text_Main.setText(event.getDescription());
		text_Type.setText(event.getEventType().getDescription());
		text_Room.setText(event.getLocal());
		text_Start.setText(String.format("%tR", event.getBeginDateTime()));
		text_End.setText(String.format("%tR", event.getEndDateTime()));
		text_Date.setText(String.format("%te", event.getBeginDateTime())+" "+String.format("%tb", event.getBeginDateTime()));

		
	}
	
	private static void getViewForCourseEvent(View view, EventDetailsModel event){
			
		
		TextView text_Main = (TextView) view.findViewById(R.id.text_Main);
		TextView text_Type = (TextView) view.findViewById(R.id.text_Type);
		TextView text_Room = (TextView) view.findViewById(R.id.text_Room);
		TextView text_Start = (TextView) view.findViewById(R.id.text_StartTime);
		TextView text_End = (TextView) view.findViewById(R.id.text_EndTime);
		TextView text_Date = (TextView) view.findViewById(R.id.text_Date);
		text_Date.setVisibility(View.VISIBLE);
		text_Date.setText(String.format("%te", event.getBeginDateTime())+" "+String.format("%tb", event.getBeginDateTime()));

		
		text_Main.setText(event.getCourseSymbol());
		
		if(event.getEventType().equals(EventType.COURSE))
		{
			text_Type.setText("Cours");
		}
		else if(event.getEventType().equals(EventType.TP))
		{
			text_Type.setText("Tp");
		}
		text_Room.setText(event.getLocal());
		
		text_Start.setText(String.format("%tR", event.getBeginDateTime()));
		
		text_End.setText(String.format("%tR", event.getEndDateTime()));
		
	}
	
	private static void getViewForDeadlineEvent(View view, EventDetailsModel event){
		
		TextView text_Main = (TextView) view.findViewById(R.id.text_Main);
		TextView text_Type = (TextView) view.findViewById(R.id.text_Type);
		TextView text_Room = (TextView) view.findViewById(R.id.text_Room);
		TextView text_Start = (TextView) view.findViewById(R.id.text_StartTime);
		TextView text_End = (TextView) view.findViewById(R.id.text_EndTime);
		TextView text_Date = (TextView) view.findViewById(R.id.text_Date);
		text_Date.setVisibility(View.VISIBLE);
		
		text_Main.setText(event.getTitle());
		text_Type.setText(event.getEventType().getDescription());
		text_Room.setText(event.getLocal());
		text_Start.setText("Avant");
		text_End.setText(String.format("%tR", event.getBeginDateTime()));
		
		text_Date.setText(String.format("%te", event.getBeginDateTime())+" "+String.format("%tb", event.getBeginDateTime()));

		
	}
	
	public static View getViewForDetail(Context context, EventDetailsModel event){
		
		
		if(event.getEventType() == EventType.COURSE || event.getEventType() == EventType.TP){
			return getViewDetailsForCourse(context, event);
		}
		else if(event.getEventType() == EventType.DEADLINE){
			return getViewDetailsForDeadline(context, event);
		}
		else
		{
			return getViewDetailsUnknownType(context, event);
		}
		
		
	}
	
	private static View getViewDetailsUnknownType(final Context context, final EventDetailsModel event){
		LinearLayout linearLayoutEvent = new LinearLayout(context);
		linearLayoutEvent.setOrientation(LinearLayout.VERTICAL);
		
		TextView textTitle = new TextView(context);
		textTitle.setText(event.getTitle());
		textTitle.setTextSize(35);
		
		textTitle.setClickable(true);
		textTitle.setOnClickListener(new View.OnClickListener() {
			
		
			public void onClick(View v) {
	
				Intent cours = new Intent(context, CourseDetailsActivity.class);
        		cours.putExtra("COURS_SYMBOL", event.getCourseSymbol());
        		cours.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	    	context.startActivity(cours);
			}
		});
		
		linearLayoutEvent.addView(textTitle);
		
		View lineDivider = new View(context);
        lineDivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider.setBackgroundColor(Color.WHITE);
        linearLayoutEvent.addView(lineDivider);
		
		LayoutInflater layoutDateTime = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutDateTime.inflate(R.layout.row, null);
		
		TextView text_Main = (TextView) view.findViewById(R.id.text_Main);
		TextView text_Type = (TextView) view.findViewById(R.id.text_Type);
		TextView text_Room = (TextView) view.findViewById(R.id.text_Room);
		TextView text_Start = (TextView) view.findViewById(R.id.text_StartTime);
		TextView text_End = (TextView) view.findViewById(R.id.text_EndTime);
		TextView text_Date = (TextView) view.findViewById(R.id.text_Date);
		text_Date.setVisibility(View.GONE);
		
		
		text_Main.setText(event.getLocal());
		
		text_Type.setText(event.getEventType().getDescription());
		
		text_Room.setText(String.format("%te", event.getBeginDateTime())
				+ " " + String.format("%tb", event.getBeginDateTime())
				+ " " + String.format("%tY", event.getBeginDateTime()));
		
		text_Start.setText(String.format("%tR", event.getBeginDateTime()));
		
		text_End.setText(String.format("%tR", event.getEndDateTime()));
		linearLayoutEvent.addView(view);
		
		View lineDivider1 = new View(context);
        lineDivider1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider1.setBackgroundColor(Color.WHITE);
        linearLayoutEvent.addView(lineDivider1);
		
		LinearLayout layoutDescription = new LinearLayout(context);
		layoutDescription.setOrientation(LinearLayout.HORIZONTAL);
		android.widget.LinearLayout.LayoutParams linParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		linParam.setMargins(20, 30, 20, 0);
		layoutDescription.setLayoutParams(linParam);
		
		TextView textDescription = new TextView(context);
		textDescription.setText("Info: ");
		textDescription.setTextAppearance(context,android.R.style.TextAppearance_Medium);
		layoutDescription.addView(textDescription);
		
		TextView textDescriptionDetails = new TextView(context);
		textDescriptionDetails.setText(event.getDescription());
		android.widget.LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textParam.setMargins(20, 30, 20, 0);
		textDescriptionDetails.setTextAppearance(context,android.R.style.TextAppearance_Medium);
		textDescriptionDetails.setLayoutParams(textParam);
		layoutDescription.addView(textDescriptionDetails);
		
		linearLayoutEvent.addView(layoutDescription);
		
		return linearLayoutEvent;
	}
	
	private static View getViewDetailsForCourse(final Context context, final EventDetailsModel event){
		LinearLayout linearLayoutEvent = new LinearLayout(context);
		linearLayoutEvent.setOrientation(LinearLayout.VERTICAL);
		
		TextView textTitle = new TextView(context);
		textTitle.setText(event.getCourseSymbol());
		textTitle.setTextSize(35);
		
		
		textTitle.setClickable(true);
		textTitle.setOnClickListener(new View.OnClickListener() {
			
		
			public void onClick(View v) {
	
				Intent cours = new Intent(context, CourseDetailsActivity.class);
        		cours.putExtra("COURS_SYMBOL", event.getCourseSymbol());
        		cours.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	    	context.startActivity(cours);
			}
		});
		
		linearLayoutEvent.addView(textTitle);
		
		View lineDivider = new View(context);
        lineDivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider.setBackgroundColor(Color.WHITE);
        linearLayoutEvent.addView(lineDivider);
		
		LayoutInflater layoutDateTime = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutDateTime.inflate(R.layout.row, null);
		
		TextView text_Main = (TextView) view.findViewById(R.id.text_Main);
		TextView text_Type = (TextView) view.findViewById(R.id.text_Type);
		TextView text_Room = (TextView) view.findViewById(R.id.text_Room);
		TextView text_Start = (TextView) view.findViewById(R.id.text_StartTime);
		TextView text_End = (TextView) view.findViewById(R.id.text_EndTime);
		TextView text_Date = (TextView) view.findViewById(R.id.text_Date);
		text_Date.setVisibility(View.GONE);
		
		CourseModel course = CourseList.getInstance().getCourse(event.getCourseSymbol());

		text_Main.setText(event.getLocal());

		
		text_Type.setText(event.getEventType().getDescription());
		
		text_Room.setText(String.format("%te", event.getBeginDateTime())
				+ " " + String.format("%tb", event.getBeginDateTime())
				+ " " + String.format("%tY", event.getBeginDateTime()));
		
		text_Start.setText(String.format("%tR", event.getBeginDateTime()));
		
		text_End.setText(String.format("%tR", event.getEndDateTime()));
		linearLayoutEvent.addView(view);
		
		View lineDivider1 = new View(context);
        lineDivider1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider1.setBackgroundColor(Color.WHITE);
        linearLayoutEvent.addView(lineDivider1);
		
		LinearLayout layoutDescription = new LinearLayout(context);
		layoutDescription.setOrientation(LinearLayout.HORIZONTAL);
		android.widget.LinearLayout.LayoutParams linParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		linParam.setMargins(20, 30, 20, 0);
		layoutDescription.setLayoutParams(linParam);
		
		TextView textDescription = new TextView(context);
		textDescription.setText("Info: ");
		textDescription.setTextAppearance(context, android.R.style.TextAppearance_Medium);
		layoutDescription.addView(textDescription);
		
		TextView textDescriptionDetails = new TextView(context);
		textDescriptionDetails.setText(event.getDescription());
		android.widget.LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textParam.setMargins(20, 30, 20, 0);
		textDescriptionDetails.setTextAppearance(context,android.R.style.TextAppearance_Medium);
		layoutDescription.addView(textDescriptionDetails);
		textDescriptionDetails.setLayoutParams(textParam);
		
		
		linearLayoutEvent.addView(layoutDescription);
		
		return linearLayoutEvent;
	}
	
	private static View getViewDetailsForDeadline(final Context context, final EventDetailsModel event){
		LinearLayout linearLayoutEvent = new LinearLayout(context);
		linearLayoutEvent.setOrientation(LinearLayout.VERTICAL);
		
		TextView textTitle = new TextView(context);
		textTitle.setText("Devoir pour " + event.getCourseSymbol());
		linearLayoutEvent.addView(textTitle);
		textTitle.setTextSize(35);
		
		textTitle.setClickable(true);
		textTitle.setOnClickListener(new View.OnClickListener() {
			
	
			public void onClick(View v) {
	
				Intent cours = new Intent(context, CourseDetailsActivity.class);
        		cours.putExtra("COURS_SYMBOL", event.getCourseSymbol());
        		cours.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	    	context.startActivity(cours);
			}
		});
		
		View lineDivider = new View(context);
        lineDivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider.setBackgroundColor(Color.WHITE);
        linearLayoutEvent.addView(lineDivider);
		
		LayoutInflater layoutDateTime = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutDateTime.inflate(R.layout.row, null);
		
		TextView text_Main = (TextView) view.findViewById(R.id.text_Main);
		TextView text_Type = (TextView) view.findViewById(R.id.text_Type);
		TextView text_Room = (TextView) view.findViewById(R.id.text_Room);
		TextView text_Start = (TextView) view.findViewById(R.id.text_StartTime);
		TextView text_End = (TextView) view.findViewById(R.id.text_EndTime);
		TextView text_Date = (TextView) view.findViewById(R.id.text_Date);
		text_Date.setVisibility(View.GONE);

		
		text_Type.setText("");
		text_Main.setText(String.format("%te", event.getBeginDateTime())
				+ " " + String.format("%tb", event.getBeginDateTime())
				+ " " + String.format("%tY", event.getBeginDateTime()));
		
		text_Room.setText(event.getLocal());
		text_Start.setText("Avant");
		text_End.setText(String.format("%tR", event.getBeginDateTime()));
		
		linearLayoutEvent.addView(view);
		
		LinearLayout layoutDescription = new LinearLayout(context);
		layoutDescription.setOrientation(LinearLayout.HORIZONTAL);
		android.widget.LinearLayout.LayoutParams linParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		linParam.setMargins(20, 30, 20, 0);
		layoutDescription.setLayoutParams(linParam);
		
		View lineDivider1 = new View(context);
        lineDivider1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider1.setBackgroundColor(Color.WHITE);
        linearLayoutEvent.addView(lineDivider1);
		
		TextView textDescription = new TextView(context);
		textDescription.setText("Info: ");
		textDescription.setTextAppearance(context,android.R.style.TextAppearance_Medium);
		layoutDescription.addView(textDescription);
		
		TextView textDescriptionDetails = new TextView(context);
		textDescriptionDetails.setText(event.getDescription());
		android.widget.LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textParam.setMargins(20, 30, 20, 0);
		textDescriptionDetails.setTextAppearance(context,android.R.style.TextAppearance_Medium);
		layoutDescription.addView(textDescriptionDetails);
		textDescriptionDetails.setLayoutParams(textParam);
		
		linearLayoutEvent.addView(layoutDescription);
		
		return linearLayoutEvent;
		
		
	}
	
}
