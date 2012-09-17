/*
 * Copyright (C) 2011 Chris Gao <chris@exina.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.etsmtl.applets.etsmobile;

import java.util.ArrayList;
import java.util.Calendar;

import org.xml.sax.helpers.AttributesImpl;

import com.etsmt.applets.etsmobile.views.NavBar;

import ca.etsmtl.applets.etsmobile.ctrls.CalendarCtrl;
import ca.etsmtl.applets.etsmobile.ctrls.EventListAdapter;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel;
import ca.etsmtl.applets.etsmobile.utils.EventHandler;
import ca.etsmtl.applets.etsmobile.utils.TouchEvent;
import ca.etsmtl.applets.etsmobile.views.AjoutEventActivity;
import ca.etsmtl.applets.etsmobile.views.EventDetailsActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class CalendarActivity extends Activity {
	public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.exina.android.calendar.date";
	Handler mHandler = new Handler();
	
	private ListView listView;
	private CalendarCtrl calendarCtrl;
	private Calendar dateSelect;
	private Calendar dateMonthShown;
	private ArrayList<EventDetailsModel> list;
	private TextView actualMonth;

	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(savedInstanceState);
        
        calendarCtrl = new CalendarCtrl(this.getApplicationContext(), this);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LinearLayout linearLayoutCalendar = new LinearLayout(this.getApplicationContext());
        linearLayoutCalendar.setOrientation(LinearLayout.VERTICAL);
        linearLayoutCalendar.setBackgroundResource(R.drawable.home_background);
        linearLayoutCalendar.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                TouchEvent touch = TouchEvent.getInstance();
                int res = touch.motionEvent(event);
                if(res!=0){
                	setNextMonth(res);
                }
                return true;
            }
        });
        
     
        NavBar nav_bar = new NavBar(this);
        nav_bar.hideRightButton();
        nav_bar.setTitle(R.drawable.navbar_horaire_title);

        LinearLayout linearLayoutCalendarTitle = new LinearLayout(this.getApplicationContext());
        linearLayoutCalendarTitle.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutCalendar.addView(nav_bar);
        linearLayoutCalendar.addView(linearLayoutCalendarTitle);
     
        
        //text of the actual month (over the calendar)
        actualMonth = new TextView(this.getApplicationContext());
        actualMonth.setText("Actual Month");
        actualMonth.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
        actualMonth.setGravity(Gravity.CENTER);
        

        //left button to switch month
        Button leftMonthButton = new Button(this.getApplicationContext());
        leftMonthButton.setBackgroundResource(R.drawable.left);
        leftMonthButton.setPadding(40, 5, 40, 5);
        leftMonthButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        
        Button rightMonthButton = new Button(this.getApplicationContext());
        rightMonthButton.setBackgroundResource(R.drawable.right);        
/*
        rightMonthButton.setPadding(40, 5, 40, 5);        
        rightMonthButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
*/
        leftMonthButton.setPadding(50, 5, 50, 5);
        rightMonthButton.setPadding(50, 5, 50, 5);

        actualMonth.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
        leftMonthButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        rightMonthButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        actualMonth.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        actualMonth.setGravity(Gravity.CENTER);

       
       
        
        linearLayoutCalendarTitle.addView(leftMonthButton);
        linearLayoutCalendarTitle.addView(actualMonth);
        linearLayoutCalendarTitle.addView(rightMonthButton);       
        
   
        linearLayoutCalendar.addView(calendarCtrl.getCalendarView());
        actualMonth.setText(String.format("%tB", calendarCtrl.getCurrentDate())+" "+calendarCtrl.getCurrentDate().get(Calendar.YEAR));
        
        View lineDividerBlank = new View(this.getApplicationContext());
        lineDividerBlank.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 15 ));
        linearLayoutCalendar.addView(lineDividerBlank);
        
      //leftMonthButton
        View lineDivider = new View(this.getApplicationContext());
        lineDivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider.setBackgroundColor(Color.WHITE);
        
        linearLayoutCalendar.addView(lineDivider);
        
        Calendar currentDate = Calendar.getInstance();
        dateSelect=currentDate;
        
        dateMonthShown = Calendar.getInstance();
        
        leftMonthButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setNextMonth(-1);
				//calendarCtrl.updateTextViewMonth();
			}
		});
        
        //rightMonthButton
        rightMonthButton.setOnClickListener(new OnClickListener() {
			
		
			public void onClick(View v) {
				setNextMonth(1);
			}
		});
	
		
        listView = new ListView(this.getApplicationContext());
        listView.setScrollbarFadingEnabled(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        	public void onItemClick(AdapterView<?> parentView, View childeView, int position,
        			long id) {
        		Log.v("test", Integer.toString(list.get(position).getId()));
        		
        		Intent eventDetails = new Intent(CalendarActivity.this, EventDetailsActivity.class);
        		eventDetails.putExtra("EVENT_ID", list.get(position).getId());
    	    	startActivity(eventDetails);
        	}
		});
        
        updateList(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        
        linearLayoutCalendar.addView(listView);
        
        setContentView(linearLayoutCalendar);

    }
	
    /*
     * set next month ou previous month
     * with addMonth =-1 for previous and addMonth =1 for next
     */
    public void setNextMonth(int addMonth){
    	if(addMonth!=-1)
    		calendarCtrl.nextMonth();
    	else
    		calendarCtrl.previousMonth();
		dateMonthShown.add(Calendar.MONTH, addMonth);		
		actualMonth.setText(String.format("%tB", dateMonthShown)+" "+""+dateMonthShown.get(Calendar.YEAR));
		updateList(calendarCtrl.getCurrentDate().get(Calendar.YEAR), calendarCtrl.getCurrentDate().get(Calendar.MONTH), 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.return_menu:
            	Intent menuActivity = new Intent(CalendarActivity.this, ETSMobileActivity.class);
    	    	startActivityForResult(menuActivity, 0);
                return true;
            case R.id.add_even:
                Intent addEventActivity = new Intent(CalendarActivity.this, AjoutEventActivity.class);
                addEventActivity.putExtra("DATE_TIME", this.dateSelect.getTimeInMillis());
                addEventActivity.putExtra("FROM_CALENDAR", "FROM_CALENDAR");
                startActivity(addEventActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void updateList(int year, int month, int day) {
    	dateSelectionner = Calendar.getInstance();
    	dateSelectionner.set(year, month, day);
    	
    	Log.v("updateListL", "list updated: date:" + year + "/" + Integer.toString(month+1) + "/" + day);
    	dateSelect.set(year, month, day);
    	EventHandler ehm = EventHandler.getInstance();
    
    	list = ehm.getListAtDateOnly(dateSelectionner);
        EventListAdapter adapter = new EventListAdapter(list, this.getApplicationContext());
        listView.setAdapter(adapter);
        
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	this.updateList(dateSelectionner.get(Calendar.YEAR), dateSelectionner.get(Calendar.MONTH), dateSelectionner.get(Calendar.DATE));
    	
    }
  
    
    /**
     * Date sélectionner
     */
    private Calendar dateSelectionner;
	public Calendar getDateSelectionner() {
		return dateSelectionner;
	}

	public void setDateSelectionner(Calendar dateSelectionner) {
		this.dateSelectionner = dateSelectionner;
	}

    
}