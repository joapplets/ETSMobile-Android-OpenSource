package com.applets;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.applets.baseactivity.BaseActivity;
import com.applets.models.TourStop;
import com.markupartist.android.widget.actionbar.R;

public class InterrestActivity extends BaseActivity {

	private class GestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onDown(final MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(final MotionEvent e1, final MotionEvent e2,
				final float velocityX, final float velocityY) {
			// final Intent intent = getNextIntext(pos);

			if (Math.abs(e1.getY() - e2.getY()) > InterrestActivity.SWIPE_MAX_OFF_PATH) {
				return false;
			}

			// right to left swipe
			if (e1.getX() - e2.getX() > InterrestActivity.SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > InterrestActivity.SWIPE_THRESHOLD_VELOCITY) {
				startActivity(getNextIntext(pos++));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				// right to left swipe
			} else if (e2.getX() - e1.getX() > InterrestActivity.SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > InterrestActivity.SWIPE_THRESHOLD_VELOCITY) {
				startActivity(getNextIntext(pos--));
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}

			return false;
		}

		@Override
		public boolean onScroll(final MotionEvent e1, final MotionEvent e2,
				final float distanceX, final float distanceY) {
			return true;
		}
	}

	private static final int SWIPE_MAX_OFF_PATH = 200;
	private static final int SWIPE_MIN_DISTANCE = 120;

	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	private ArrayList<Intent> intents;
	private int pos = 0;
	private TourStop stopInfo;

	private ArrayList<TourStop> stopList;

	public Intent getNextIntext(final int pos) {
		return intents.get(pos);
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

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tour_stop);

		// create test stop
		initTourTest();
		retreiveIntents();

		final Bundle b = getIntent().getExtras();
		final int index = b.getInt(TourStop.class.getName());
		stopInfo = stopList.get(index);

		createActionBar(stopInfo.getName(), R.id.tour_stop_actionbar);

		gestureDetector = new GestureDetector(new GestureListener());
		findViewById(R.id.tour_stop_scroller_layout).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(final View v, final MotionEvent event) {
						return gestureDetector.onTouchEvent(event);
					}
				});

	}

	private void retreiveIntents() {
		intents = new ArrayList<Intent>();
		// intents.add(new Intent(this, InterrestActivity.class).putExtra(
		// TourStop.class.getName(), new TourStop("title 3",
		// "aaaaaaaaaaa", R.drawable.sample_2, "")));
		for (final TourStop tourStop : stopList) {
			intents.add(new Intent(this, InterrestActivity.class).putExtra(
					TourStop.class.getName(), tourStop));
		}
	}

}
