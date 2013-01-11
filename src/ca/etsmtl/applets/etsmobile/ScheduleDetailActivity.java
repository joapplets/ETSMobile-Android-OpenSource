package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class ScheduleDetailActivity extends Activity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.calendar_event_detail);

	final NavBar navBar = (NavBar) findViewById(R.id.navBar1);
	navBar.setTitle(R.drawable.navbar_horaire_title);
	navBar.hideRightButton();
	navBar.hideLoading();

	TextView txt = (TextView) findViewById(R.id.txt_cours);

	txt.setText(getIntent().getExtras().getString("cours"));

	txt = (TextView) findViewById(R.id.txt_local);

	if (getIntent().getExtras().getString("local") != null) {
	    txt.setText(getIntent().getExtras().getString("local"));
	} else {
	    txt.setVisibility(View.GONE);
	}

	txt = (TextView) findViewById(R.id.txt_date);

	txt.setText(getIntent().getExtras().getString("date"));

	txt = (TextView) findViewById(R.id.txt_hours);

	if (getIntent().getExtras().getString("hours") != null) {
	    txt.setText(getIntent().getExtras().getString("hours"));
	} else {
	    txt.setVisibility(View.GONE);
	}

	txt = (TextView) findViewById(R.id.txt_name);

	if (getIntent().getExtras().getString("name") != null) {
	    txt.setText(getIntent().getExtras().getString("name"));
	} else {
	    txt.setVisibility(View.GONE);
	}

    }
}
