package ca.etsmtl.applets.etsmobile;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ScrollView;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class AboutActivity extends Activity {

    private ScrollView scrollView;

    private Handler handler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setContentView(R.layout.about);
	final NavBar navBar = (NavBar) findViewById(R.id.navBar1);
	navBar.setTitle(R.drawable.navbar_title);
	navBar.hideLoading();
	navBar.hideRightButton();
	navBar.hideHome();
	scrollView = (ScrollView) findViewById(R.id.scrollView1);
	handler = new Handler();
	final TimerTask scrollerSchedule = new TimerTask() {

	    @Override
	    public void run() {

		handler.post(new Runnable() {

		    @Override
		    public void run() {
			if (scrollView.getScrollY() < scrollView.getMaxScrollAmount() * 9) {
			    scrollView.smoothScrollTo(0, scrollView.getScrollY() + 4);
			} else {
			    scrollView.smoothScrollTo(0, 0);
			}
		    }
		});
	    }
	};
	final Timer t = new Timer();
	t.schedule(scrollerSchedule, 1000, 50);

    }
}
