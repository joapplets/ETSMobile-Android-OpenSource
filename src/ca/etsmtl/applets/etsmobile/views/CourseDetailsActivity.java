package ca.etsmtl.applets.etsmobile.views;

import java.util.ArrayList;
import java.util.Calendar;

import ca.etsmtl.applets.etsmobile.ETSMobileActivity;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.ctrls.EventListAdapter;
import ca.etsmtl.applets.etsmobile.models.CourseModel;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel;
import ca.etsmtl.applets.etsmobile.utils.CourseList;
import ca.etsmtl.applets.etsmobile.utils.EventHandler;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CourseDetailsActivity extends Activity {
	
		private ListView listView;
	private ArrayList<EventDetailsModel> list;
	private CourseModel course;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LinearLayout linearLayout = new LinearLayout(this.getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        
        String courseSymbol = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
        	courseSymbol = extras.getString("COURS_SYMBOL");
        }
        
        course = CourseList.getInstance().getCourse(courseSymbol);
        
        
        
        
        linearLayout.addView(getCourseInfo());
        
        listView = new ListView(this.getApplicationContext());
        listView.setScrollbarFadingEnabled(false);
        linearLayout.addView(listView);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        	public void onItemClick(AdapterView<?> parentView, View childeView, int position,
        			long id) {
        		Log.v("onListViewClick_allEvent", "message1, position:" + position);
    	    	
        		Intent eventDetails = new Intent(CourseDetailsActivity.this, EventDetailsActivity.class);
        		eventDetails.putExtra("EVENT_ID", list.get(position).getId());
    	    	startActivity(eventDetails);
    	    	
        	}
		});
        
       Calendar currentDate = Calendar.getInstance();
       updateList(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        
        
        setContentView(linearLayout);
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
            	Intent menuActivity = new Intent(CourseDetailsActivity.this, ETSMobileActivity.class);
            	startActivityForResult(menuActivity, 0);
                return true;
            case R.id.add_even:
                Intent addEventActivity = new Intent(CourseDetailsActivity.this, AjoutEventActivity.class);
                addEventActivity.putExtra("FROM_COURS_ADD", "FROM_COURS_ADD");
                addEventActivity.putExtra("COURS", course.getCourseSymbol());
                startActivity(addEventActivity);
      
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	public void onResume(){
		super.onResume();
		
		Calendar currentDate = Calendar.getInstance();
	    updateList(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
		
	}
	
	public void updateList(int year, int month, int day) {
    	
    	
    	Log.v("updateListL", "list updated: date:" + year + "/" + Integer.toString(month+1) + "/" + day);
    	
    	EventHandler ehm = EventHandler.getInstance();
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(year, month, day);
    	list = ehm.getListFromDateForClass(calendar, course.getCourseSymbol());
        EventListAdapter adapter = new EventListAdapter(list, this.getApplicationContext());
        listView.setAdapter(adapter);
        
    }
	
	private LinearLayout getCourseInfo(){
		LinearLayout layout = new LinearLayout(this.getApplicationContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		
		TextView coursSymbol = new TextView(this.getApplicationContext());
        coursSymbol.setText(course.getCourseSymbol());
        coursSymbol.setTextSize(35);
        layout.addView(coursSymbol);
        
        
        View lineDivider = new View(this.getApplicationContext());
        lineDivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider.setBackgroundColor(Color.WHITE);
        layout.addView(lineDivider);
        
        LinearLayout linearLayoutDetails = new LinearLayout(this.getApplicationContext());
        linearLayoutDetails.setOrientation(LinearLayout.VERTICAL);
        
        TextView coursTitle = new TextView(this.getApplicationContext());
        coursTitle.setText(course.getCourseTitle());
        layout.addView(coursTitle);
        
        View lineDivider2 = new View(this.getApplicationContext());
        lineDivider2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider2.setBackgroundColor(Color.WHITE);
        layout.addView(lineDivider2);
        
        TextView coursTeacher = new TextView(this.getApplicationContext());
        coursTeacher.setText(course.getCourseTeacher());
        coursTeacher.setTextSize(25);
        linearLayoutDetails.addView(coursTeacher);
        
        linearLayoutDetails.addView(getTableLayoutInfoCourse());
        
        TextView labTeacher = new TextView(this.getApplicationContext());
        labTeacher.setText(course.getLabTeacher());
        labTeacher.setTextSize(25);
        linearLayoutDetails.addView(labTeacher);
        
        linearLayoutDetails.addView(getTableLayoutInfoLab());
        
        TableRow layoutCourseDescription = new TableRow(this.getApplicationContext());
        layoutCourseDescription.setOrientation(LinearLayout.HORIZONTAL);
        
        TextView textInfo = new TextView(this.getApplicationContext());
        textInfo.setText("Description: ");
        textInfo.setLayoutParams( new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) );
        layoutCourseDescription.addView(textInfo);
        
        TextView textInfoDetails = new TextView(this.getApplicationContext());
        textInfoDetails.setText(course.getcourseDescription());
        textInfoDetails.setLayoutParams( new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) );
        layoutCourseDescription.addView(textInfoDetails);
        
        linearLayoutDetails.addView(layoutCourseDescription);
        
        ScrollView courseInfo = new ScrollView(this.getApplicationContext());
        courseInfo.addView(linearLayoutDetails);
        courseInfo.setScrollbarFadingEnabled(false);
           
        courseInfo.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 300));
        
        layout.addView(courseInfo);
        
        View lineDivider3 = new View(this.getApplicationContext());
        lineDivider3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider3.setBackgroundColor(Color.WHITE);
        layout.addView(lineDivider3);
        
        return layout;
	}
	
	private TableLayout getTableLayoutInfoCourse(){
		//http://stackoverflow.com/questions/8489907/android-dynamically-add-textview-on-a-tablelayout-with-fixed-width
        //****************************************************************************************           
        TableLayout table = new TableLayout(this.getApplicationContext());
        table.setColumnStretchable(0, true);
        
        //row 1*******************************************************************************************************************
        TableRow rowTeacherOffice = new TableRow(this.getApplicationContext());
        
        LinearLayout layoutTeacherOffice = new LinearLayout(this.getApplicationContext());
        layoutTeacherOffice.setOrientation(LinearLayout.HORIZONTAL);
        
        TextView coursTeacherOffice = new TextView(this.getApplicationContext());
        coursTeacherOffice.setText("Bureau: ");
        coursTeacherOffice.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
        
        TextView coursTeacherOfficeDetail = new TextView(this.getApplicationContext());
        coursTeacherOfficeDetail.setText("(info here)");
        coursTeacherOfficeDetail.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 4 ) );
        
        layoutTeacherOffice.addView(coursTeacherOffice);
        layoutTeacherOffice.addView(coursTeacherOfficeDetail);
       
        rowTeacherOffice.addView(layoutTeacherOffice);
        table.addView(rowTeacherOffice);
        //*******************************************************************************************************************
        
        //row 2*******************************************************************************************************************
        TableRow rowTeacherPhone = new TableRow(this.getApplicationContext());
        
        LinearLayout layoutTeacherPhone = new LinearLayout(this.getApplicationContext());
        layoutTeacherOffice.setOrientation(LinearLayout.HORIZONTAL);
        
        TextView coursTeacherPhone = new TextView(this.getApplicationContext());
        coursTeacherPhone.setText("Téléphone: ");
        coursTeacherPhone.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
        
        TextView coursTeacherPhoneDetail = new TextView(this.getApplicationContext());
        coursTeacherPhoneDetail.setText("(info here)");
        coursTeacherPhoneDetail.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 4 ) );
        
        layoutTeacherPhone.addView(coursTeacherPhone);
        layoutTeacherPhone.addView(coursTeacherPhoneDetail);
       
        rowTeacherPhone.addView(layoutTeacherPhone);
        table.addView(rowTeacherPhone);
        //*******************************************************************************************************************
        
        //row 3*******************************************************************************************************************
        TableRow rowTeacherEmail = new TableRow(this.getApplicationContext());
        
        LinearLayout layoutTeacherEmail = new LinearLayout(this.getApplicationContext());
        layoutTeacherEmail.setOrientation(LinearLayout.HORIZONTAL);
        
        TextView coursTeacherEmail = new TextView(this.getApplicationContext());
        coursTeacherEmail.setText("Courriel: ");
        coursTeacherEmail.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
        
        TextView coursTeacherPhoneEmail = new TextView(this.getApplicationContext());
        coursTeacherPhoneEmail.setText("(info here)");
        coursTeacherPhoneEmail.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 4 ) );
        
        layoutTeacherEmail.addView(coursTeacherEmail);
        layoutTeacherEmail.addView(coursTeacherPhoneEmail);
       
        rowTeacherEmail.addView(layoutTeacherEmail);
        table.addView(rowTeacherEmail);
        //*******************************************************************************************************************
        
        
        return table;
	}
	
	private TableLayout getTableLayoutInfoLab(){
		//http://stackoverflow.com/questions/8489907/android-dynamically-add-textview-on-a-tablelayout-with-fixed-width
        //****************************************************************************************           
        TableLayout table = new TableLayout(this.getApplicationContext());
        table.setColumnStretchable(0, true);
        
        //row 1*******************************************************************************************************************
        TableRow rowTeacherOffice = new TableRow(this.getApplicationContext());
        
        LinearLayout layoutTeacherOffice = new LinearLayout(this.getApplicationContext());
        layoutTeacherOffice.setOrientation(LinearLayout.HORIZONTAL);
        
        TextView coursTeacherOffice = new TextView(this.getApplicationContext());
        coursTeacherOffice.setText("Bureau: ");
        coursTeacherOffice.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
        
        TextView coursTeacherOfficeDetail = new TextView(this.getApplicationContext());
        coursTeacherOfficeDetail.setText("(info here)");
        coursTeacherOfficeDetail.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 4 ) );
        
        layoutTeacherOffice.addView(coursTeacherOffice);
        layoutTeacherOffice.addView(coursTeacherOfficeDetail);
       
        rowTeacherOffice.addView(layoutTeacherOffice);
        table.addView(rowTeacherOffice);
        //*******************************************************************************************************************
        
        //row 2*******************************************************************************************************************
        TableRow rowTeacherPhone = new TableRow(this.getApplicationContext());
        
        LinearLayout layoutTeacherPhone = new LinearLayout(this.getApplicationContext());
        layoutTeacherOffice.setOrientation(LinearLayout.HORIZONTAL);
        
        TextView coursTeacherPhone = new TextView(this.getApplicationContext());
        coursTeacherPhone.setText("Téléphone: ");
        coursTeacherPhone.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
        
        TextView coursTeacherPhoneDetail = new TextView(this.getApplicationContext());
        coursTeacherPhoneDetail.setText("(info here)");
        coursTeacherPhoneDetail.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 4 ) );
        
        layoutTeacherPhone.addView(coursTeacherPhone);
        layoutTeacherPhone.addView(coursTeacherPhoneDetail);
       
        rowTeacherPhone.addView(layoutTeacherPhone);
        table.addView(rowTeacherPhone);
        //*******************************************************************************************************************
        
        //row 3*******************************************************************************************************************
        TableRow rowTeacherEmail = new TableRow(this.getApplicationContext());
        
        LinearLayout layoutTeacherEmail = new LinearLayout(this.getApplicationContext());
        layoutTeacherEmail.setOrientation(LinearLayout.HORIZONTAL);
        
        TextView coursTeacherEmail = new TextView(this.getApplicationContext());
        coursTeacherEmail.setText("Courriel: ");
        coursTeacherEmail.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
        
        TextView coursTeacherPhoneEmail = new TextView(this.getApplicationContext());
        coursTeacherPhoneEmail.setText("(info here)");
        coursTeacherPhoneEmail.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 4 ) );
        
        layoutTeacherEmail.addView(coursTeacherEmail);
        layoutTeacherEmail.addView(coursTeacherPhoneEmail);
       
        rowTeacherEmail.addView(layoutTeacherEmail);
        table.addView(rowTeacherEmail);
        //*******************************************************************************************************************
        
        
        return table;
	}

}
