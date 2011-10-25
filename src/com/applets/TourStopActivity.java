package com.applets;

import android.os.Bundle;
import android.widget.TextView;

import com.applets.baseactivity.BaseActivity;
import com.applets.models.TourStop;

public class TourStopActivity extends BaseActivity {

    private TourStop stopInfo;
    private TextView title;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.tour_stop);

	final Bundle b = getIntent().getExtras();
	stopInfo = (TourStop) b.getParcelable(TourStop.class.getName());

	createActionBar(stopInfo.getName(), R.id.tour_stop_actionbar);
    }

}
