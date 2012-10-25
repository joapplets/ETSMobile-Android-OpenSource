package ca.etsmtl.applets.etsmobile;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ScrollView;

public class AboutActivity extends Activity {

	private ScrollView scrollView;

	private Handler handler;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.about);

		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		handler = new Handler();
		final TimerTask scrollerSchedule = new TimerTask() {

			@Override
			public void run() {

				handler.post(new Runnable() {

					@Override
					public void run() {
						if (scrollView.getScrollY() < scrollView
								.getMaxScrollAmount() * 9) {
							scrollView.smoothScrollTo(0,
									scrollView.getScrollY() + 1);
						} else {
							scrollView.smoothScrollTo(0, 0);
						}
					}
				});
			}
		};
		final Timer t = new Timer();
		t.schedule(scrollerSchedule, 1000, 100);

		// home btn
		((ImageButton) findViewById(R.id.empty_nav_bar_home_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View view) {
						finish();
					}
				});
	}
}
