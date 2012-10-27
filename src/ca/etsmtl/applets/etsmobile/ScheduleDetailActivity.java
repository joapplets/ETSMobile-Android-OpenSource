package ca.etsmtl.applets.etsmobile;

import ca.etsmtl.applets.etsmobile.views.NavBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ScheduleDetailActivity extends Activity{
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_event_detail);
		
		
		NavBar navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.setTitle(R.drawable.navbar_horaire_title);
		navBar.hideRightButton();
		
		TextView txt = (TextView) findViewById(R.id.txt_cours);
		
		txt.setText(getIntent().getExtras().getString("cours"));
		
		txt = (TextView) findViewById(R.id.txt_local);
		
		txt.setText(getIntent().getExtras().getString("local"));
		
		txt = (TextView) findViewById(R.id.txt_date);
		
		txt.setText(getIntent().getExtras().getString("date"));
		
		txt = (TextView) findViewById(R.id.txt_hours);
		
		txt.setText(getIntent().getExtras().getString("hours"));
	
		txt = (TextView) findViewById(R.id.txt_name);
		
		txt.setText(getIntent().getExtras().getString("name"));	
		
	}
}
