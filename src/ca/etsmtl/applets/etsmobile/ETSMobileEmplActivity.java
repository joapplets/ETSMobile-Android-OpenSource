package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import ca.etsmtl.applets.etsmobile.adapters.ETSMobileAdapter;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class ETSMobileEmplActivity extends Activity implements OnItemClickListener, OnTouchListener {

    private NavBar navBar;
    private static final int LOGIN = 0;

    /**
     * MAIN
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.main);

	// home made nav bar; hide all
	navBar = (NavBar) findViewById(R.id.navBarMain);
	navBar.setTitle(R.drawable.navbar_title);
	navBar.hideLoading();
	navBar.hideRightButton();
	navBar.hideHome();

	// Custom GridAdapter, each icon is the main entry to a "module"
	final GridView gridview = (GridView) findViewById(R.id.gridview);
	gridview.setAdapter(new ETSMobileAdapter(getApplicationContext()));

	// load credentials from sharePrefs; No prefs, show login
	final UserCredentials creds = new UserCredentials(
		PreferenceManager.getDefaultSharedPreferences(this));

	if (creds.getPassword() != null && creds.getUsername() != null
		&& "".equals(creds.getPassword()) && "".equals(creds.getUsername())) {
	    showDialog(ETSMobileEmplActivity.LOGIN);
	}

	gridview.setOnItemClickListener(this);
	gridview.setOnTouchListener(this);

	// about bnt
	((ImageButton) findViewById(R.id.imgBtnabout)).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		final Intent intent = new Intent(v.getContext(), AboutActivity.class);
		startActivity(intent);
	    }
	});
    }

    /**
     * Grid Item Click for employees
     */
    @Override
    public void onItemClick(final AdapterView<?> arg0, final View arg1, final int position,
	    final long id) {
	Intent intent = null;
	switch (position) {
	case 0:
	    // intent = new Intent(this, ProfileActivity.class);
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
	    // intent = new Intent(this, MyCourseSessionActivity.class);
	    break;
	case 6:
	    intent = new Intent(this, BibliothequeActivity.class);
	    break;
	case 7:
	    // contact; Ask to open email app; prefill email info
	    intent = new Intent(android.content.Intent.ACTION_SEND);
	    intent.setType("plain/text");
	    intent.putExtra(android.content.Intent.EXTRA_EMAIL,
		    new String[] { "applets@ens.etsmtl.ca" });
	    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ETSMobile-Android : Commentaire");
	    intent.putExtra(android.content.Intent.EXTRA_TEXT, "Bravo ! 10/10 !");
	    break;
	case 8:
	    intent = new Intent(this, EmailWebView.class);
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