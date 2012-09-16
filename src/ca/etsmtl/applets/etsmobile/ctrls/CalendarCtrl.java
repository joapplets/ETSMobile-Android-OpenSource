package ca.etsmtl.applets.etsmobile.ctrls;


import java.util.Calendar;

import android.content.Context;
import  ca.etsmtl.applets.etsmobile.CalendarActivity;
import  ca.etsmtl.applets.etsmobile.CalendarView;
import  ca.etsmtl.applets.etsmobile.CalendarView.OnCellTouchListener;
import  ca.etsmtl.applets.etsmobile.Cell;

public class CalendarCtrl implements OnCellTouchListener {
	
	private CalendarView calendarView = null;
	private CalendarActivity calendarActivity = null;
	
	public CalendarCtrl(Context context, CalendarActivity calendarActivity) {
        // calendarView = (CalendarView)findViewById(R.id.calendar);
		calendarView = new CalendarView(context);
        calendarView.setOnCellTouchListener(this);
        this.calendarActivity = calendarActivity;
	}

	public void onTouch(Cell cell) {
		int year  = calendarView.getYear();
		int month = calendarView.getMonth();
		int day   = cell.getDayOfMonth();
		
		// FIX issue 6: make some correction on month and year
		if(cell instanceof CalendarView.GrayCell) {
			// oops, not pick current month...				
			if (day < 15) {
				// pick one beginning day? then a next month day
				if(month==11)
				{
					month = 0;
					year++;
				} else {
					month++;
				}
				
			} else {
				// otherwise, previous month
				if(month==0) {
					month = 11;
					year--;
				} else {
					month--;
				}
			}
		}
		
		calendarView.refreshToSelectedDate(cell);
		calendarView.getDate().set(year, month, day);
		
		calendarActivity.updateList(year, month, day);
		
		return;

	}

	public CalendarView getCalendarView() {
		return calendarView;
	}
	
	public void previousMonth(){
		calendarView.previousMonth();
	}

	
	public CalendarActivity getCalendarActivity(){
		return calendarActivity;
	}

	public void nextMonth(){
		calendarView.nextMonth();
	}
	
	public Calendar getCurrentDate(){
		return calendarView.getDate();
	}
	
}
