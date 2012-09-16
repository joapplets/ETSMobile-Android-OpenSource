package ca.etsmtl.applets.etsmobile.views;

import java.sql.Date;
import java.util.Calendar;

import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.CalendarActivity;
import ca.etsmtl.applets.etsmobile.ctrls.AddEventCtrl;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel.EventType;
import ca.etsmtl.applets.etsmobile.utils.EventHandler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;



public class AjoutEventActivity extends Activity{

		private String[] arrayLocaux;
		private String[] arrayCourseSymbol;
		private String[] arrayType;
		private DatePickerDialog datePickerDialog;
		private TimePickerDialog timePickerDialog;
		private TimePickerDialog timePickerDialogTo;
		private AddEventCtrl addEvent;
		private Calendar beginTime;
		private Calendar endTime;
		private int idTimePicker=0;
		private boolean ifModif=false;
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        super.onCreate(savedInstanceState);
	        this.addEvent = new AddEventCtrl();      
	        this.arrayLocaux= addEvent.getLocauxString();
	        setContentView(R.layout.activity_evenements);
	        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        
	        ArrayAdapter<String> adapterLocal = new ArrayAdapter<String>(this,
	                 android.R.layout.simple_dropdown_item_1line, arrayLocaux);
	        AutoCompleteTextView textView = (AutoCompleteTextView)
	                 findViewById(R.id.autoCompleteTextViewSalle);
	        
	        textView.setThreshold(1);
	        textView.setAdapter(adapterLocal);
	        
	        this.arrayCourseSymbol = addEvent.getCourseSymbols();
	   
	        ArrayAdapter<String> adapterCour = new ArrayAdapter<String>(this,
	                 android.R.layout.simple_dropdown_item_1line, arrayCourseSymbol);
	     
	        Spinner spinnerCour = (Spinner) findViewById(R.id.spinnerCour);
	        spinnerCour.setAdapter(adapterCour);
	        
	        arrayType = new String[ EventType.values().length ];
	        
	        int i=0;
	        for(EventType evenType:EventType.values()){
	        	arrayType[i]=evenType.getDescription();
	        	i++;
	        }
	        
	        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
	                 android.R.layout.simple_dropdown_item_1line, arrayType);
	     
	        Spinner spinnerType = (Spinner) findViewById(R.id.spinnerType);
	        spinnerType.setAdapter(adapterType);
	        spinnerType.setOnItemSelectedListener(onItemSelectedListener);
	        
	        beginTime = Calendar.getInstance();
	        endTime  = Calendar.getInstance();
	        
	        Bundle extras = getIntent().getExtras();
	     
	        if(extras != null) {
	        	if(extras.containsKey("FROM_CALENDAR")){
		        	beginTime.setTimeInMillis(extras.getLong("DATE_TIME"));
		        	endTime.setTimeInMillis(extras.getLong("DATE_TIME"));
		        	endTime.add(Calendar.HOUR_OF_DAY, +1);
		       	}
	        	else if(extras.containsKey("FROM_COURS_ADD")){
	        		String cours = extras.getString("COURS");
	        		this.setSpinnerSelectedString(cours, adapterCour, spinnerCour);
	        		endTime.add(Calendar.HOUR_OF_DAY, +1);
	           	}
	        	else if(extras.containsKey("FROM_EVEN_MODIFY")){
	        		 ifModif = true;
	        		 int eventID = extras.getInt("EVENT_ID");
	        		 EventDetailsModel event = EventHandler.getInstance().getEventById(eventID);
	        		 this.setSpinnerSelectedString(event.getEventType().getDescription(), adapterType, spinnerType);
	        		 this.setSpinnerSelectedString(event.getCourseSymbol(), adapterCour, spinnerCour);
	        		 beginTime.setTimeInMillis(event.getBeginDateTime().getTimeInMillis());
	        		 endTime.setTimeInMillis(event.getEndDateTime().getTimeInMillis());
	        		 EditText editTitre = (EditText) findViewById(R.id.editTextTitre);
	        		 editTitre.setText(event.getTitle());
	        		 EditText editSalle = (EditText) findViewById(R.id.autoCompleteTextViewSalle);
	        		 editSalle.setText(event.getLocal());
	        		 EditText editInfo = (EditText) findViewById(R.id.editTextInfo);
	        		 editInfo.setText(event.getDescription());
	        		 
	        		 Button buttonModifier = (Button) findViewById(R.id.buttonCreer);
	        		 
	        		 Resources res = getResources();
	        	     String ok = String.format(res.getString(R.string.ok));
	        		 buttonModifier.setText(ok);
	        		 
	        		 TextView text = (TextView) findViewById(R.id.textView_Evenement);
	        		 String modify = String.format(res.getString(R.string.modify));
	        		 text.setText(modify);
	        	}
	        }
	    
	        this.datePickerDialog = new DatePickerDialog(this,mDateSetListener , beginTime.get(Calendar.YEAR), beginTime.get(Calendar.MONTH), beginTime.get(Calendar.DATE));	       
	        EditText editDate = (EditText) findViewById(R.id.editTextDate);
			editDate.setText(String.format("%td", beginTime)+"/"+String.format("%tm", beginTime)+"/"+beginTime.get(Calendar.YEAR));

			timePickerDialog = new TimePickerDialog(this, mTimeSetListener, beginTime.get(Calendar.HOUR_OF_DAY),beginTime.get(Calendar.MINUTE),true);
			EditText editTimeFrom = (EditText) findViewById(R.id.editTextDe);
			editTimeFrom.setText(beginTime.get(Calendar.HOUR_OF_DAY)+":"+String.format("%tM",beginTime));
			
			
			timePickerDialogTo = new TimePickerDialog(this, mTimeSetListener, endTime.get(Calendar.HOUR_OF_DAY),endTime.get(Calendar.MINUTE),true);
			EditText editTimeTo = (EditText) findViewById(R.id.editTextTo);
			editTimeTo.setText(endTime.get(Calendar.HOUR_OF_DAY)+":"+String.format("%tM",endTime));
		}
		
		public void setSpinnerSelectedString(String elem,  ArrayAdapter<String> adapter, Spinner spinner){
    		int position = adapter.getPosition(elem);
    		spinner.setSelection(position);
		}
				
		private DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener(){
			public void onDateSet(DatePicker datepicker, int year, int month, int day) {
				beginTime.set(year, month, day);
				EditText editDate = (EditText) findViewById(R.id.editTextDate);
				editDate.setText(String.format("%td", beginTime)+"/"+String.format("%tm", beginTime)+"/"+year);
			}
        	
        };
        
      private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> parentView, View selecItem, int position, long id) {
				// TODO Auto-generated method stub
				TableRow tabTo = (TableRow) findViewById(R.id.tableTo);
				TableRow tabTitre = (TableRow) findViewById(R.id.tableTitre);
				if(((String)parentView.getSelectedItem()).equals(EventType.DEADLINE.getDescription()))
				{
					tabTo.setVisibility(View.GONE);
					TextView viewTimeDe = (TextView) findViewById(R.id.textViewDe);
					viewTimeDe.setText("À");
					tabTitre.setVisibility(View.VISIBLE);
				}
				else
				{	
					tabTo.setVisibility(View.VISIBLE);
					TextView viewTimeDe = (TextView) findViewById(R.id.textViewDe);
					viewTimeDe.setText("De");
					
					if(((String)parentView.getSelectedItem()).equals(EventType.COURSE.getDescription()) || 
							((String)parentView.getSelectedItem()).equals(EventType.TP.getDescription())){
						tabTitre.setVisibility(View.GONE);
					}
					else
						tabTitre.setVisibility(View.VISIBLE);
				}
			}

			public void onNothingSelected(AdapterView<?> parentView) {
				// TODO Auto-generated method stub
				
			}
      	
      };
      
      private TimePickerDialog.OnTimeSetListener mTimeSetListener = new OnTimeSetListener(){
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		
				if( idTimePicker == R.id.editTextDe ){
					EditText editTimeFrom = (EditText) findViewById(R.id.editTextDe);
					beginTime.set(beginTime.get(Calendar.YEAR),beginTime.get(Calendar.MONTH), beginTime.get(Calendar.DATE), hourOfDay, minute);
					editTimeFrom.setText(hourOfDay+":"+String.format("%tM",beginTime));
				}
				else if(idTimePicker == R.id.editTextTo ){
					EditText editTimeTo = (EditText) findViewById(R.id.editTextTo);
					endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DATE), hourOfDay, minute);
					editTimeTo.setText(hourOfDay+":"+String.format("%tM",endTime));
				}
			}
        };
        
	
		public void popupDate(View v){
			datePickerDialog.show();
		}
		
		public void popupTime(View v){
			if(v.getId() == R.id.editTextDe){
				idTimePicker = R.id.editTextDe;
				this.timePickerDialog.show();
			}
			else if(v.getId() == R.id.editTextTo){
				idTimePicker = R.id.editTextTo;
				this.timePickerDialogTo.show();
			}
		}
	    
		public void actionDone(View v){
			EditText editInfo = (EditText) findViewById(R.id.editTextInfo);
			InputMethodManager inputMethod = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
			inputMethod.hideSoftInputFromWindow(editInfo.getWindowToken(), 0);
		}
		
	    public void creerEvenement(View v){
	    	
	    	Spinner spinnerType = (Spinner) findViewById(R.id.spinnerType);
	    	String type = (String)spinnerType.getSelectedItem();
	    	
	    	Spinner spinnerCour = (Spinner) findViewById(R.id.spinnerCour);
	    	String courseSymbol = (String)spinnerCour.getSelectedItem();
	    	
	    	EditText editTitre = (EditText) findViewById(R.id.editTextTitre);
	    	String titre = editTitre.getText().toString();
	    		
	    	EditText editSalle = (EditText) findViewById(R.id.autoCompleteTextViewSalle);
	    	String salle = editSalle.getText().toString();
	    	
	    	EditText editInfo = (EditText) findViewById(R.id.editTextInfo);
	    	String info = editInfo.getText().toString();

	    	if(!ifModif){
	    		this.addEvent.createEvent(type,courseSymbol,titre, beginTime, endTime,salle, info);
	    	}
	    	else{
	    		Bundle extras = getIntent().getExtras();
	    		int eventID = extras.getInt("EVENT_ID");
	    		this.addEvent.modifyEvent(type,courseSymbol,titre, beginTime, endTime,salle, info,eventID);
	    	}
	    	
	    	this.finish();
	    	
	    }
	    
	    public void cancel(View view){
	    	this.finish();
	    }
	    

}
