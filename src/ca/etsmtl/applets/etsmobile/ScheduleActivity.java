package ca.etsmtl.applets.etsmobile;

import java.text.SimpleDateFormat;
import java.util.Locale;




import ca.etsmtl.applets.etsmobile.ctrls.CalendarTextView;
import ca.etsmtl.applets.etsmobile.models.CurrentCalendar;
import ca.etsmtl.applets.etsmobile.views.NumGridView;
import ca.etsmtl.applets.etsmobile.views.NumGridView.OnCellTouchListener;





import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import android.widget.Button;;

public class ScheduleActivity extends Activity {
	
CurrentCalendar current; 
NumGridView mNumGridView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view);
        
        
        Resources res = getResources();
        String[] day_names = res.getStringArray(R.array.day_names);
        
        GridView grid = (GridView) findViewById(R.id.gridDayNames);
        grid.setAdapter( new ArrayAdapter<String>(this, R.layout.day_name,day_names));
        
       
      
        this.current = new CurrentCalendar();
        
        Button btn_previous = (Button) findViewById(R.id.btn_previous);
        Button btn_next = (Button) findViewById(R.id.btn_next);
        
       
        btn_previous.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               
            	current.previousMonth();
            	
            }
        });
        
        btn_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               
            	current.nextMonth();
            	
            }
        });
        
        mNumGridView= (NumGridView)findViewById(R.id.numgridview);
      
        mNumGridView.setOnCellTouchListener(mNumGridView_OnCellTouchListener);
        
        CalendarTextView txtcalendar_title = (CalendarTextView)findViewById(R.id.calendar_title);
      
        
        ListView lst_cours = (ListView) findViewById(R.id.lst_cours);
        
        String[] cours_names_examples = {"Cours 1", "Cours 2"};
        lst_cours.setAdapter( new ArrayAdapter<String>(this, R.layout.day_name,cours_names_examples));
        
      
        this.current.addObserver(mNumGridView);
        this.current.addObserver(txtcalendar_title);
       
        this.current.setChanged();
        this.current.notifyObservers(this.current.getCalendar());
        
    }
    
    OnCellTouchListener mNumGridView_OnCellTouchListener = new OnCellTouchListener() {
        @Override public void onCellTouch( NumGridView v, int x, int y ) {
        	v.setSelectedCell(new Point(x,y));
           v.invalidate();
        }
    };
    


   
}
