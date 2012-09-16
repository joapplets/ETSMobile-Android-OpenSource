package ca.etsmtl.applets.etsmobile.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ca.etsmtl.applets.etsmobile.ETSMobileActivity;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.CalendarActivity;
import ca.etsmtl.applets.etsmobile.ctrls.EventListAdapter;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel.EventType;
import ca.etsmtl.applets.etsmobile.utils.EventHandler;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AllEventActivity extends Activity{
	
	private ListView listView;
	private ArrayList<EventDetailsModel> list;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        LinearLayout linearLayout = new LinearLayout(this.getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        
        TextView text = new TextView(this.getApplicationContext());
        text.setText("Evenements");
        text.setTextSize(35);
        linearLayout.addView(text);
        
        View lineDivider1 = new View(this.getApplicationContext());
        lineDivider1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider1.setBackgroundColor(Color.WHITE);
        linearLayout.addView(lineDivider1);
        
        listView = new ListView(this.getApplicationContext());
        listView.setScrollbarFadingEnabled(false);
        linearLayout.addView(listView);
        //context = this.getApplicationContext();
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        	public void onItemClick(AdapterView<?> parentView, View childeView, int position,
        			long id) {
        		//call eventDetailActivity...
        		//ici on appelle une nouvelle activity pour affichier les details de l'evenement
        		Log.v("onListViewClick_allEvent", "message1, position:" + position);
    	    	
        		Intent eventDetails = new Intent(AllEventActivity.this, EventDetailsActivity.class);
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
            	Intent menuActivity = new Intent(AllEventActivity.this, ETSMobileActivity.class);
    	    	startActivityForResult(menuActivity, 0);
                return true;
            case R.id.add_even:
                Intent addEventActivity = new Intent(AllEventActivity.this, AjoutEventActivity.class);
    	    	startActivity(addEventActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	
	public void updateList(int year, int month, int day) {
    	
    	
    	Log.v("updateListL", "list updated: date:" + year + "/" + Integer.toString(month+1) + "/" + day);
    	
    	EventHandler ehm = EventHandler.getInstance();
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(year, month, day);
    	list = ehm.getListFromDate(calendar);
        EventListAdapter adapter = new EventListAdapter(list, this.getApplicationContext());
        listView.setAdapter(adapter);
        
    }
	
	 @Override
	    protected void onResume(){
	    	super.onResume();
	    	 Calendar currentDate = Calendar.getInstance();
	    	 updateList(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
	    }

}
