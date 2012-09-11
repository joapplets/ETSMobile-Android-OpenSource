package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import ca.etsmtl.applets.etsmobile.adapters.ETSMobileAdapter;

public class ETSMobileActivity extends Activity implements OnItemClickListener,
		OnTouchListener, OnClickListener {

	/**
	 * About
	 */
	@Override
	public void onClick(final View arg0) {
		final Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ETSMobileAdapter(getApplicationContext()));

		gridview.setOnItemClickListener(this);
		gridview.setOnTouchListener(this);

		// about bnt
		((ImageButton) findViewById(R.id.imgBtnabout)).setOnClickListener(this);
	}

	/**
	 * Grid Item Click
	 */
	@Override
	public void onItemClick(final AdapterView<?> arg0, final View arg1,
			final int position, final long id) {
		Intent intent = null;
		switch (position) {
		case 0:
			intent = new Intent(this, ProfileActivity.class);
			break;
		case 1:
			intent = new Intent(this, NewsListActivity.class);
			break;
		case 2:
			intent = new Intent(this, SecurityActivity.class);
			break;
		case 3:
			// intent = new Intent(this, ScheduleActivity.class);
			break;
		case 4:
			intent = new Intent(this, BottinListActivity.class);
			break;
		case 5:
			intent = new Intent(this, MyCourseSessionActivity.class);
			break;
		case 6:
			intent = new Intent(this,CoopActivity.class);
			break;
		default:
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}
	}

	/** Disable scrolling of the main view */
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