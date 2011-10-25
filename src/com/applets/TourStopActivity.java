package com.applets;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.TextView;

import com.applets.baseactivity.BaseActivity;
import com.applets.models.TourStop;

public class TourStopActivity extends BaseActivity {

    private TourStop stopInfo;
    private TextView title;
    private TextView text;
    private ArrayList<Intent> intents;
    private int pos = 0;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.tour_stop);

	final Bundle b = getIntent().getExtras();
	stopInfo = (TourStop) b.getParcelable(TourStop.class.getName());

	createActionBar(stopInfo.getName(), R.id.tour_stop_actionbar);

	retreiveIntents();

	gestureDetector = new GestureDetector(new GestureListener());
	findViewById(R.id.tour_stop_scroller).setOnTouchListener(
		new View.OnTouchListener() {

		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
			return gestureDetector.onTouchEvent(event);
		    }
		});

    }

    private void retreiveIntents() {
	intents = new ArrayList<Intent>();
	intents.add(new Intent(this, TourStopActivity.class).putExtra(
		TourStop.class.getName(), new TourStop("title 3",
			"aaaaaaaaaaa", R.drawable.sample_2, "")));
    }

    private class GestureListener extends SimpleOnGestureListener {
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
		float velocityY) {
	    final Intent intent = getNextIntext(pos);

	    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
		return false;
	    }

	    // right to left swipe
	    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
		    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		startActivity(intent);
		TourStopActivity.this.overridePendingTransition(
			R.anim.slide_in_right, R.anim.slide_out_left);
		// right to left swipe
	    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
		    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		startActivity(intent);
		TourStopActivity.this.overridePendingTransition(
			R.anim.slide_in_left, R.anim.slide_out_right);
	    }

	    return false;
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
