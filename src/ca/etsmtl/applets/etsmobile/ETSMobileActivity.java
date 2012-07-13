package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import ca.etsmtl.applets.etsmobile.adapters.ETSMobileAdapter;

import ca.etsmtl.applets.etsmobile.R;

public class ETSMobileActivity extends Activity implements OnItemClickListener, OnTouchListener {
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ETSMobileAdapter(getApplicationContext()));

		gridview.setOnItemClickListener(this);
		gridview.setOnTouchListener(this);
	}

	@Override
	public void onItemClick(final AdapterView<?> arg0, final View arg1,
			final int position, final long id) {
		Intent intent = null;
		switch (position) {
		case 0:
			intent = new Intent(this, StudentProfileActivity.class);
			break;
		case 1:
			intent = new Intent(this, NewsListActivityV2.class);
			break;
		case 2:
			//intent = new Intent(this, SecurityActivity.class);
			break;
		case 3:
			//intent = new Intent(this, ScheduleActivity.class);
			break;
		case 4:
			//intent = new Intent(this, DirectoryActivity.class);
			break;
		case 5:
			//intent = new Intent(this, MyCoursesActivity.class);
			break;
		default:
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}
	}

	// Disable scrolling of the main view
	@Override
	public boolean onTouch(final View v, final MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			return true;
		default:
			break;
		}
		return false;
	}
}