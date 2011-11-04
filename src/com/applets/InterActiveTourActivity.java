package com.applets;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.applets.adapters.InteractiveAdapter;
import com.applets.baseactivity.BaseActivity;
import com.applets.models.TourStop;

/**
 * Interactive Tour, shows a grid view to pick a "stop"
 * 
 * @author Philippe David
 * 
 */
public class InterActiveTourActivity extends BaseActivity implements
	OnItemClickListener {

    private GridView grid;
    private ArrayList<TourStop> stopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	// set the view
	setContentView(R.layout.interactive_tour_layout);
	// create action bar
	createActionBar(getString(R.string.interactive_tour_title),
		R.id.interactive_tour_actionbar);
	stopList = new ArrayList<TourStop>();

	// create test stop
	initTourTest();

	grid = (GridView) findViewById(R.id.gridview);
	grid.setOnItemClickListener(this);
	grid.setAdapter(new InteractiveAdapter(this));
    }

    /**
     * Fake data for testing, should be replaced by a SQLite query
     */
    private void initTourTest() {
	stopList.add(new TourStop("Titre 1", getString(R.string.loremipsum),
		R.drawable.sample_2, "REF_1"));
	stopList.add(new TourStop("Titre 2", getString(R.string.loremipsum),
		R.drawable.sample_3, "REF_1"));
	stopList.add(new TourStop("Titre 3", getString(R.string.loremipsum),
		R.drawable.sample_4, "REF_1"));
	stopList.add(new TourStop("Titre 4", getString(R.string.loremipsum),
		R.drawable.sample_5, "REF_1"));
	stopList.add(new TourStop("Titre 5", getString(R.string.loremipsum),
		R.drawable.sample_6, "REF_1"));
	stopList.add(new TourStop("Titre 6", getString(R.string.loremipsum),
		R.drawable.sample_7, "REF_1"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	startTour(stopList.get(position));
    }

    /**
     * Start the interactive tour with the specified stop
     * 
     * @param tourStop
     *            The stop to load
     */
    private void startTour(TourStop tourStop) {
	Intent intent = new Intent(this, TourStopActivity.class);
	intent.putExtra(TourStop.class.getName(), tourStop);
	startActivity(intent);
    }
}
