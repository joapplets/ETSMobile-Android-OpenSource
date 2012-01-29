package com.applets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.applets.adapters.InteractiveAdapter;
import com.applets.baseactivity.BaseActivity;
import com.applets.models.TourStop;
import com.markupartist.android.widget.actionbar.R;

/**
 * Points of interrest shows a grid view to pick a interesting place in the
 * school
 * 
 * @author Philippe David
 * 
 */
public class InterrestGridActivity extends BaseActivity implements
	OnItemClickListener {

    private GridView grid;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	// set the view
	setContentView(R.layout.interrest_points_layout);
	// create action bar
	createActionBar(getString(R.string.interactive_tour_title),
		R.id.interactive_tour_actionbar);

	grid = (GridView) findViewById(R.id.gridview);
	grid.setOnItemClickListener(this);
	grid.setAdapter(new InteractiveAdapter(this));
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View v,
	    final int position, final long id) {
	startTour(position);
    }

    /**
     * Start the interactive tour with the specified stop
     * 
     * @param tourStop
     *            The stop to load
     */
    private void startTour(final int tourStop) {
	final Intent intent = new Intent(this, InterrestActivity.class);
	intent.putExtra(TourStop.class.getName(), tourStop);
	startActivity(intent);
    }
}
