package com.applets;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.applets.baseactivity.BaseActivity;
import com.applets.models.TourStop;

public class InterrestActivity extends BaseActivity {

    private TourStop stopInfo;

    private ArrayList<Intent> intents;
    private ArrayList<TourStop> stopList;

    private int pos = 0;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 200;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.tour_stop);

	// create test stop
	initTourTest();
	retreiveIntents();

	final Bundle b = getIntent().getExtras();
	int index = b.getInt(TourStop.class.getName());
	stopInfo = stopList.get(index);

	createActionBar(stopInfo.getName(), R.id.tour_stop_actionbar);

	gestureDetector = new GestureDetector(new GestureListener());
	findViewById(R.id.tour_stop_scroller_layout).setOnTouchListener(
		new View.OnTouchListener() {

		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
			return gestureDetector.onTouchEvent(event);
		    }
		});

    }

    private void retreiveIntents() {
	intents = new ArrayList<Intent>();
	// intents.add(new Intent(this, InterrestActivity.class).putExtra(
	// TourStop.class.getName(), new TourStop("title 3",
	// "aaaaaaaaaaa", R.drawable.sample_2, "")));
	for (Iterator<TourStop> i = stopList.iterator(); i.hasNext();) {
	    intents.add(new Intent(this, InterrestActivity.class).putExtra(
		    TourStop.class.getName(), i.next()));
	}
    }

    /**
     * Fake data for testing, should be replaced by a SQLite query
     */
    private void initTourTest() {
	stopList = new ArrayList<TourStop>();
	stopList.add(new TourStop("Titre 0", getString(R.string.loremipsum),
		R.drawable.sample_0, "REF_0"));
	stopList.add(new TourStop("Titre 1", getString(R.string.loremipsum),
		R.drawable.sample_1, "REF_1"));
	stopList.add(new TourStop("Titre 2", getString(R.string.loremipsum),
		R.drawable.sample_2, "REF_2"));
	stopList.add(new TourStop("Titre 3", getString(R.string.loremipsum),
		R.drawable.sample_3, "REF_3"));
	stopList.add(new TourStop("Titre 4", getString(R.string.loremipsum),
		R.drawable.sample_4, "REF_4"));
	stopList.add(new TourStop("Titre 5", getString(R.string.loremipsum),
		R.drawable.sample_5, "REF_5"));
	stopList.add(new TourStop("Titre 6", getString(R.string.loremipsum),
		R.drawable.sample_6, "REF_6"));
	stopList.add(new TourStop("Titre 7", getString(R.string.loremipsum),
		R.drawable.sample_7, "REF_7"));
    }

    private class GestureListener extends SimpleOnGestureListener {
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
		float velocityY) {
	    // final Intent intent = getNextIntext(pos);

	    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
		return false;
	    }

	    // right to left swipe
	    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
		    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		startActivity(getNextIntext(pos++));
		InterrestActivity.this.overridePendingTransition(
			R.anim.slide_in_right, R.anim.slide_out_left);
		// right to left swipe
	    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
		    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		startActivity(getNextIntext(pos--));
		InterrestActivity.this.overridePendingTransition(
			R.anim.slide_in_left, R.anim.slide_out_right);
	    }

	    return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2,
		float distanceX, float distanceY) {
	    return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
	    return true;
	}
    }

    public Intent getNextIntext(int pos) {
	return intents.get(pos);
    }

}
