package ca.etsmtl.applets.etsmobile.views;

import java.util.Calendar;

import ca.etsmtl.applets.etsmobile.ETSMobileActivity;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.CalendarActivity;
import ca.etsmtl.applets.etsmobile.models.EventDetailsModel;
import ca.etsmtl.applets.etsmobile.utils.EventHandler;
import ca.etsmtl.applets.etsmobile.utils.EventViewFactory;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class EventDetailsActivity extends Activity{

	private  int eventID =0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	    setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 Bundle extras = getIntent().getExtras();
		 
        if(extras != null) {
        	eventID = extras.getInt("EVENT_ID");
        }
        
        EventDetailsModel event = EventHandler.getInstance().getEventById(eventID);
		
		if(event != null){
			
			setContentView(EventViewFactory.getViewForDetail(this.getApplicationContext(), event));
		}
		else
		{
			finish();
		}
		
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_eventdetails, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.return_menu:
            	Intent menuActivity = new Intent(EventDetailsActivity.this, ETSMobileActivity.class);
    	    	startActivity(menuActivity);
                return true;
            case R.id.modify_event:
                Intent addEventActivity = new Intent(EventDetailsActivity.this, AjoutEventActivity.class);
                addEventActivity.putExtra("FROM_EVEN_MODIFY", "FROM_EVEN_MODIFY");
                addEventActivity.putExtra("EVENT_ID", eventID);
                          
                startActivity(addEventActivity);
                return true;
            case R.id.delete_event:
            	 alertDialog().show();         	
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
	public AlertDialog.Builder alertDialog(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    	alertDialogBuilder.setTitle("Alerte");
    	alertDialogBuilder.setMessage("Voulez-vous vraiment supprimé cet événement?")
    	.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				 EventHandler.getInstance().deleteEventById(eventID);	
				 EventDetailsActivity.this.finish();
			}
		})
		.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
    	return alertDialogBuilder;
	}

	 @Override
	    protected void onResume(){
	    	super.onResume();
	    	 EventDetailsModel event = EventHandler.getInstance().getEventById(eventID);
	    	 setContentView(EventViewFactory.getViewForDetail(this.getApplicationContext(), event));
	    }

}
